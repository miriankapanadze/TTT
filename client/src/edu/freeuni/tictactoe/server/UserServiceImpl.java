package edu.freeuni.tictactoe.server;

import android.util.Log;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.ServerStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserServiceImpl implements UserService {
	private static String loggerMarker = "userServiceImpl";

	private static String serverIp = "192.168.0.101";
	private static int serverPort = 8080;
	private static Socket socket;

	@Override
	public ServerStatus login(LoginRequest request) {
		ServerStatus status = new ServerStatus();
		status.setStatus(ServerStatus.Status.FAILURE);

		try {
			if (socket == null) socket = new Socket(serverIp, serverPort);

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", request.getUserName());
			jsonObject.put("password", request.getPassword());

			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(jsonObject);

			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			JSONObject response = (JSONObject) in.readObject();
			status.setStatus(ServerStatus.Status.valueOf(response.getString("status")));
			status.setAdditionalInfo(response.getString("additionalInfo"));

			return status;

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException ignored) {
		}
		return status;
	}

	@Override
	public ServerStatus register(RegistrationRequest registrationRequest) {
		new RegistrationThread(registrationRequest).start();
		return null;
	}

	private class RegistrationThread extends Thread {

		private RegistrationRequest request;

		public RegistrationThread(RegistrationRequest registrationRequest) {
			this.request = registrationRequest;
		}

		@Override
		public void run() {
			ServerStatus status = new ServerStatus();
			status.setStatus(ServerStatus.Status.FAILURE);
			try {
				Log.i(loggerMarker, "try registration");
				if (socket == null) {
					socket = new Socket(serverIp, serverPort);
				}
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("username", request.getUserName());
				jsonObject.put("name", request.getName());
				jsonObject.put("password", request.getPassword());

				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				Log.i(loggerMarker, "ObjectOutputStream created");
				out.writeObject(jsonObject);
				Log.i(loggerMarker, "written object");

				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				JSONObject response = (JSONObject) in.readObject();
				status.setStatus(ServerStatus.Status.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));

			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException ignored){}
		}
	}
}