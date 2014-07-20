package edu.freeuni.tictactoe.processing;

import edu.freeuni.tictactoe.messages.Messages;
import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.History;
import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.SocketHolder;
import edu.freeuni.tictactoe.model.StatusType;
import edu.freeuni.tictactoe.model.User;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.service.HistoryManager;
import edu.freeuni.tictactoe.service.UsersManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainProcess {

	private static Map<User, SocketHolder> socketsMap;

	public static void main(String args[]) {
		try {
			ServerSocket mainSocket = new ServerSocket(8080);
			socketsMap = new ConcurrentHashMap<>();
			//noinspection InfiniteLoopStatement
			while (true) {
				System.out.println("Waiting for incoming connection...");
				Socket socket = mainSocket.accept();
				System.out.println("Accepted incoming connection...");
				new CommunicationThread(socket).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static class CommunicationThread extends Thread {

		private Socket socket;
		private DataInputStream inputStream;
		private DataOutputStream outputStream;

		private User user;

		public CommunicationThread(Socket socket) {
			try {
				this.socket = socket;
				this.outputStream = new DataOutputStream(socket.getOutputStream());
				this.inputStream = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				//noinspection InfiniteLoopStatement
				while (true) {
					String receivedString = inputStream.readUTF();
					JSONObject receivedJSON = new JSONObject(receivedString);
					RequestType type = RequestType.valueOf(receivedJSON.getString("requestType"));

					switch (type) {
						case REGISTRATION:
							onRegistration(receivedJSON);
							return;
						case LOGIN:
							if (!onLogin(receivedJSON)) return;
							break;
						case START_GAME:
							System.out.println("calling onStartGame");
							onStartGame(receivedJSON);
							break;
						case INVITATION_ANSWER:
							System.out.println("calling onInvitationAnswer");
							onInvitationAnswer(receivedJSON);
							break;
						case MOVE:
							onMove(receivedJSON);
							break;
						case GAME_OVER:
							onGameOver(receivedJSON);
							break;
					}
				}
			} catch (Exception e) {
				onConnectionClose();
			}
		}

		private void onRegistration(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				User user = new User(receivedJSON.getString("name"), receivedJSON.getString("username"), receivedJSON.getString("password"));
				UsersManager.getInstance().registerUser(user);

				responseJSON.put("status", StatusType.SUCCESS.name());
				responseJSON.put("additionalInfo", Messages.get("registrationSuccessful"));
				outputStream.writeUTF(responseJSON.toString());

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private boolean onLogin(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				User dbUser = UsersManager.getInstance()
						.findUserByCredentials(new User(receivedJSON.getString("username"), receivedJSON.getString("password")));

				user = dbUser;
				UserMode status = UserMode.valueOf(receivedJSON.getString("userMode"));
				user.setMode(status);
				socketsMap.put(user, new SocketHolder(socket, inputStream, outputStream));

				responseJSON.put("status", StatusType.SUCCESS.name());
				responseJSON.put("additionalInfo", Messages.get("loginSuccessful"));
				responseJSON.put("opponents", User.getJSONArray(UsersManager.getInstance().getOpponents(dbUser)).toString());
				responseJSON.put("history", History.getJSONArray(UsersManager.getInstance().getUserHistory(dbUser)).toString());

				outputStream.writeUTF(responseJSON.toString());

				return true;

			} catch (Exception e) {
				onFailure(responseJSON, e);
				return false;
			}
		}

		private void onStartGame(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				int opponentId = receivedJSON.getInt("opponentId");
				BoardType boardType = BoardType.valueOf(receivedJSON.getString("boardType"));

				User opponent = UsersManager.getInstance().findUserById(opponentId);

				if (socketsMap.get(opponent) == null) {
					throw new Exception("inactiveOpponent");
				}
				UserMode opponentMode = getOpponentMode(opponent);
				if (opponentMode == UserMode.ACTIVE) {
					throw new Exception("invalidOpponentUserMode");
				}

				System.out.println("calling sendInvitation method");
				sendInvitation(opponent, boardType);

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private UserMode getOpponentMode(User opponent) {
			for (User user : socketsMap.keySet()) {
				if (user.equals(opponent)) {
					return user.getMode();
				}
			}
			return null;
		}

		private void sendInvitation(User opponent, BoardType boardType) {
			try {
				SocketHolder holder = socketsMap.get(opponent);
				DataOutputStream outputStream = holder.getOutputStream();

				String invitation = getInvitationJSON(boardType).toString();
				System.out.println("sending invitation: " + invitation);
				outputStream.writeUTF(invitation);
				System.out.println("invitation sent");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private JSONObject getInvitationJSON(BoardType boardType) {
			JSONObject invitation = new JSONObject();
			try {
				invitation.put("status", StatusType.SUCCESS.name());
				invitation.put("opponentId", user.getId());
				invitation.put("opponentName", user.getUsername());
				invitation.put("opponentRank", user.getRank());
				invitation.put("boardType", boardType.name());

				return invitation;

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private void onInvitationAnswer(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				StatusType status = StatusType.valueOf(receivedJSON.getString("status"));
				System.out.println("received invitation answer status: " + status.name());

				int opponentId = receivedJSON.getInt("opponentId");
				System.out.println("received invitation answer opponentId: " + opponentId);

				User opponent = UsersManager.getInstance().findUserById(opponentId);

				DataOutputStream outputStream = socketsMap.get(opponent).getOutputStream();
				responseJSON.put("status", status.name());
				responseJSON.put("additionalInfo", Messages.get(receivedJSON.getString("additionalInfo")));

				System.out.println("forwarding invitation answer: " + responseJSON.toString());
				outputStream.writeUTF(responseJSON.toString());
				System.out.println("forwarded invitation answer");

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private void onMove(JSONObject receivedJSON) {
			JSONObject responseJSON = new JSONObject();
			try {
				int opponentId = receivedJSON.getInt("opponentId");
				User opponent = UsersManager.getInstance().findUserById(opponentId);

				if (socketsMap.get(opponent) == null) {
					throw new Exception("inactiveOpponent");
				}
				sendMove(opponent, receivedJSON.getInt("x"), receivedJSON.getInt("y"));

			} catch (Exception e) {
				onFailure(responseJSON, e);
			}
		}

		private void sendMove(User opponent, int x, int y) {
			try {
				SocketHolder holder = socketsMap.get(opponent);
				DataOutputStream outputStream = holder.getOutputStream();
				outputStream.writeUTF(getMoveJSON(x, y).toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private JSONObject getMoveJSON(int x, int y) {
			JSONObject move = new JSONObject();
			try {
				move.put("status", StatusType.SUCCESS.name());
				move.put("additionalInfo", "");
				move.put("x", x);
				move.put("y", y);

				return move;

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		private void onGameOver(JSONObject receivedJSON) {
			try {
				int opponentId = receivedJSON.getInt("opponentId");
				GameStatus gameStatus = GameStatus.valueOf(receivedJSON.getString("gameStatus"));
				User opponent = UsersManager.getInstance().findUserById(opponentId);

				History history = new History();
				history.setFirstUser(user);
				history.setSecondUser(opponent);
				history.setResult(gameStatus == GameStatus.WINNER_ANNOUNCED ? 1 : -1);

				HistoryManager.getInstance().addHistory(history);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private void onFailure(JSONObject responseJSON, Exception e) {
			try {
				responseJSON.put("status", StatusType.FAILURE.name());
				responseJSON.put("additionalInfo", Messages.get(e.getMessage()));
				outputStream.writeUTF(responseJSON.toString());

			} catch (Exception ignored) {
				ignored.printStackTrace();
			}
		}

		private void onConnectionClose() {
			try {
				socket.close();
				if (user != null) {
					socketsMap.remove(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
