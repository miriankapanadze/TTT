package edu.freeuni.tictactoe.processing;

import edu.freeuni.tictactoe.model.User;
import edu.freeuni.tictactoe.service.UsersManager;
import org.json.JSONException;
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

		private Socket socket;

		public CommunicationThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				/*while (true) {
				}*/

				JSONObject response = (JSONObject) in.readObject();
				User user = new User(response.getString("name"), response.getString("username"), response.getString("password"));
				UsersManager.getInstance().updateUser(user);

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("status", "SUCCESS");
				jsonObject.put("additionalInfo", "Test");

				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.writeObject(jsonObject);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
