package edu.freeuni.tictactoe.server;

import android.util.Log;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

	private static String loggerMarker = "userServiceImpl";
	private static String serverIp = "192.168.0.101";
	private static int serverPort = 8080;
	public static DataInputStream INPUT_STREAM;
	public static DataOutputStream OUTPUT_STREAM;
	public static Socket LOGIN_SOCKET;

	@Override
	public void login(LoginRequest request) {
		new LoginThread(request).start();
	}

	@Override
	public void register(RegistrationRequest registrationRequest) {
		new RegistrationThread(registrationRequest).start();
	}

	@Override
	public void logout() {
//		if (LOGIN_SOCKET != null) {
//			try {
//				closeConnection();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	private class LoginThread extends Thread {

		private LoginRequest request;

		public LoginThread(LoginRequest loginRequest) {
			this.request = loginRequest;
		}

		@Override
		public void run() {
			Status status = new Status();
			status.setType(Status.Type.FAILURE);
			List<UserEntry> users = new ArrayList<>();
			try {
				Log.i(loggerMarker, "try registration");
				if (LOGIN_SOCKET != null) {
					closeConnection();
				}
				LOGIN_SOCKET = new Socket(serverIp, serverPort);
				OUTPUT_STREAM = new DataOutputStream((LOGIN_SOCKET.getOutputStream()));
				INPUT_STREAM = new DataInputStream((LOGIN_SOCKET.getInputStream()));
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestType", RequestType.LOGIN.name());
				jsonObject.put("username", request.getUserName());
				jsonObject.put("password", request.getPassword());
				jsonObject.put("userMode", request.getUserMode().name());

				Log.i(loggerMarker, "ObjectOutputStream created");
				OUTPUT_STREAM.writeUTF(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				String responseString = INPUT_STREAM.readUTF();
				JSONObject response = new JSONObject(responseString);
				status.setType(Status.Type.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));

				if (status.getType() == Status.Type.SUCCESS) {
					JSONArray jsonArray = new JSONArray(response.getString("opponents"));

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);

						UserEntry userEntry = new UserEntry();
						userEntry.setId(object.getInt("id"));
						userEntry.setName(object.getString("name"));
						userEntry.setUsername(object.getString("username"));
						userEntry.setRank(object.getInt("rank"));

						users.add(userEntry);
					}
				} else {
					closeConnection();
				}
			} catch (IOException | JSONException e) {
				e.printStackTrace();
			} finally {
				System.out.println("should notify login listeners");
				ListenersManager.notifyLoginListeners(status, users, request.getUserMode());
				System.out.println("notified login listeners");
			}
		}
	}

	private void closeConnection() throws IOException {
		LOGIN_SOCKET.close();
		INPUT_STREAM.close();
		OUTPUT_STREAM.close();

		LOGIN_SOCKET = null;
		INPUT_STREAM = null;
		OUTPUT_STREAM = null;
	}

	private class RegistrationThread extends Thread {

		private RegistrationRequest request;

		public RegistrationThread(RegistrationRequest registrationRequest) {
			this.request = registrationRequest;
		}

		@Override
		public void run() {
			final Status status = new Status();
			status.setType(Status.Type.FAILURE);
			try {
				Log.i(loggerMarker, "try registration");
				Socket socket = new Socket(serverIp, serverPort);
				DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
				DataInputStream inputStream = new DataInputStream(socket.getInputStream());
				Log.i(loggerMarker, "socketCreated");

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("requestType", RequestType.REGISTRATION.name());
				jsonObject.put("username", request.getUserName());
				jsonObject.put("name", request.getName());
				jsonObject.put("password", request.getPassword());

				Log.i(loggerMarker, "ObjectOutputStream created");
				outputStream.writeUTF(jsonObject.toString());
				Log.i(loggerMarker, "written object");

				String responseString = inputStream.readUTF();
				JSONObject response = new JSONObject(responseString);
				status.setType(Status.Type.valueOf(response.getString("status")));
				status.setAdditionalInfo(response.getString("additionalInfo"));
				socket.close();
				inputStream.close();
				outputStream.close();

			} catch (IOException | JSONException e) {
				e.printStackTrace();
			} finally {
				ListenersManager.notifyRegisterListeners(status);
			}
		}
	}
}