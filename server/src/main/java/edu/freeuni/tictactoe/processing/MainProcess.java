package edu.freeuni.tictactoe.processing;

import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.User;
import edu.freeuni.tictactoe.service.UsersManager;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MainProcess {

	public static void main(String args[]) {
		try {
			ServerSocket mainSocket = new ServerSocket(8080);
			//noinspection InfiniteLoopStatement
			while (true) {
				System.out.println("Waiting...");
				Socket socket = mainSocket.accept();
				System.out.println("Accepted...");
				new CommunicationThread(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class CommunicationThread extends Thread {

		private ObjectInputStream inputStream;
		private ObjectOutputStream outputStream;

		public CommunicationThread(Socket socket) {
			try {
				inputStream = new ObjectInputStream(socket.getInputStream());
				outputStream = new ObjectOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				//noinspection InfiniteLoopStatement
				while (true) {
					String receivedString = (String) inputStream.readObject();
					JSONObject receivedJSON = new JSONObject(receivedString);
					RequestType type = RequestType.valueOf(receivedJSON.getString("requestType"));

					switch (type) {
						case REGISTRATION:
							onRegistration(receivedJSON);
							break;
						case LOGIN:
							onLogin(receivedJSON);
							break;
						case SELECT_OPPONENT:
							break;
						case MOVE:
							break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onRegistration(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				User user = new User(receivedJSON.getString("name"), receivedJSON.getString("username"), receivedJSON.getString("password"));
				UsersManager.getInstance().registerUser(user);

				responseJSON.put("status", ServerStatus.Status.SUCCESS.name());
				responseJSON.put("additionalInfo", "registrationSuccessful");
				outputStream.writeObject(responseJSON.toString());

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private void onLogin(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				User user = new User(receivedJSON.getString("username"), receivedJSON.getString("password"));
				User dbUser = UsersManager.getInstance().findUser(user);

				responseJSON.put("status", ServerStatus.Status.SUCCESS.name());
				responseJSON.put("additionalInfo", "loginSuccessful");
				responseJSON.put("opponents", UsersManager.getInstance().getOpponents(dbUser));
				responseJSON.put("history", UsersManager.getInstance().getUserHistory(dbUser));
				outputStream.writeObject(responseJSON.toString());

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private void onFailure(JSONObject responseJSON, Exception e) {
			try {
				responseJSON.put("status", ServerStatus.Status.FAILURE.name());
				responseJSON.put("additionalInfo", e.getMessage());
				outputStream.writeObject(responseJSON.toString());

			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}
	}
}
