package edu.freeuni.tictactoe.server;

import android.util.Log;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.UserEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {
	private static String loggerMarker = "userServiceImpl";

	private static String serverIp = "192.168.0.101";
	private static int serverPort = 8080;
	private static Socket socket;

	@Override
	public void login(LoginRequest request) {
		new LoginThread(request).start();
	}

	@Override
	public void register(RegistrationRequest registrationRequest) {
		new RegistrationThread(registrationRequest).start();
	}

	private class LoginThread extends Thread {

		private LoginRequest request;

		public LoginThread(LoginRequest loginRequest) {
			this.request = loginRequest;
		}

		@Override
		public void run() {
			ServerStatus status = new ServerStatus();
			status.setStatus(ServerStatus.Status.FAILURE);
			List<UserEntry> users = new ArrayList<UserEntry>();
			try {
				Log.i(loggerMarker, "try registration");
				if (socket == null) {
					socket = new Socket(serverIp, serverPort);
				}
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("username", request.getUserName());
				jsonObject.put("password", request.getPassword());

				ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
				Log.i(loggerMarker, "ObjectOutputStream created");
				stream.writeObject(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
				String responseString = (String) oi.readObject();
				JSONObject response = new JSONObject(responseString);
				status.setStatus(ServerStatus.Status.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));
				if (status.getStatus() == ServerStatus.Status.SUCCESS) {
					JSONArray jsonArray = response.getJSONArray("opponents");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);

						UserEntry userEntry = new UserEntry();
						userEntry.setId(object.getInt("id"));
						userEntry.setName(object.getString("name"));
						userEntry.setUsername(object.getString("username"));
						userEntry.setRank(object.getInt("rank"));

						users.add(userEntry);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ignored){
			} finally {
				ServicesFactory.notifyLoginListeners(status, users);
			}
		}
	}

	private class RegistrationThread extends Thread {

		private RegistrationRequest request;

		public RegistrationThread(RegistrationRequest registrationRequest) {
			this.request = registrationRequest;
		}

		@Override
		public void run() {
			final ServerStatus status = new ServerStatus();
			status.setStatus(ServerStatus.Status.FAILURE);
			try {
				Log.i(loggerMarker, "try registration");
				if (socket == null) {
					socket = new Socket(serverIp, serverPort);
				}
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestType", RequestType.REGISTRATION.name());
				jsonObject.put("username", request.getUserName());
				jsonObject.put("name", request.getName());
				jsonObject.put("password", request.getPassword());

				ObjectOutputStream stream = new ObjectOutputStream(socket.getOutputStream());
				Log.i(loggerMarker, "ObjectOutputStream created");
				stream.writeObject(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				ObjectInputStream oi = new ObjectInputStream(socket.getInputStream());
				String responseString = (String) oi.readObject();
				JSONObject response = new JSONObject(responseString);
				status.setStatus(ServerStatus.Status.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ignored){
			} finally {
				ServicesFactory.notifyRegisterListeners(status);
			}
		}
	}
}