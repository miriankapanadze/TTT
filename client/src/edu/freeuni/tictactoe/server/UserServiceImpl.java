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

	private static String serverIp = "192.168.0.100";
	private static int serverPort = 8080;
	private static Socket socket;
	private static ObjectInputStream inputStream;
	private static ObjectOutputStream outputStream;

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
					outputStream = new ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
				}
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestType", RequestType.LOGIN.name());
				jsonObject.put("username", request.getUserName());
				jsonObject.put("password", request.getPassword());

				Log.i(loggerMarker, "ObjectOutputStream created");
				outputStream.writeObject(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				String responseString = (String) inputStream.readObject();
				JSONObject response = new JSONObject(responseString);
				status.setStatus(ServerStatus.Status.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));
				if (status.getStatus() == ServerStatus.Status.SUCCESS) {
					JSONArray jsonArray = response.getJSONArray("opponents");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = new JSONObject(((String) jsonArray.get(i)));

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
					outputStream = new ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
				}
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestType", RequestType.REGISTRATION.name());
				jsonObject.put("username", request.getUserName());
				jsonObject.put("name", request.getName());
				jsonObject.put("password", request.getPassword());

				Log.i(loggerMarker, "ObjectOutputStream created");
				outputStream.writeObject(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				String responseString = (String) inputStream.readObject();
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