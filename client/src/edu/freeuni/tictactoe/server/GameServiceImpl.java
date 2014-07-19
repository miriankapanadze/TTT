package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.Status;
import org.json.JSONObject;

public class GameServiceImpl implements GameService {

	@Override
	public void startGame(final int opponentId, final BoardType type) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject requestJSON = new JSONObject();
				try {
					requestJSON.put("requestType", RequestType.START_GAME.name());
					requestJSON.put("opponentId", opponentId);
					requestJSON.put("boardType", type.name());

					System.out.println("starting game request should be written to stream");
					UserServiceImpl.OUTPUT_STREAM.writeUTF(requestJSON.toString());
					System.out.println("starting game request written to stream");
					System.out.println("waiting for response for starting game request");
					String responseString = UserServiceImpl.INPUT_STREAM.readUTF();
					System.out.println("starting game response received");
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					System.out.println("should notify starting game listeners");
					ListenersFactory.notifyStartGameListeners(type == BoardType.BOARD_3X3 ? 3 : 5, status);
					System.out.println("notified starting game listeners");
				} catch (Exception e) {
					System.out.println("starting game thread corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void makeMove(final int opponentId, final int x, final int y) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject requestJSON = new JSONObject();
				try {
					requestJSON.put("requestType", RequestType.MOVE.name());
					requestJSON.put("opponentId", opponentId);
					requestJSON.put("x", x);
					requestJSON.put("y", y);

					System.out.println("make move should be made");
					UserServiceImpl.OUTPUT_STREAM.writeUTF(requestJSON.toString());
					System.out.println("move made");
					waitForOpponentMove();

				} catch (Exception e) {
					System.out.println("make move corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void waitForOpponentMove() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("waiting for opponent move");
					String responseString = UserServiceImpl.INPUT_STREAM.readUTF();
					System.out.println("opponent moved");
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));
					System.out.println("should notify game move listeners");
					ListenersFactory.notifyGameMoveListeners(responseJSON.getInt("x"), responseJSON.getInt("y"));
					System.out.println("notified game move listeners");
				} catch (Exception e) {
					System.out.println("waiting for opponent move corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void waitForInvitation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("waiting invitation");
					String requestSTR = UserServiceImpl.INPUT_STREAM.readUTF();
					System.out.println("received invitation");
					JSONObject requestJSN = new JSONObject(requestSTR);
					int opponentId = requestJSN.getInt("opponentId");
					String opponentName = requestJSN.getString("opponentName");
					int rank = requestJSN.getInt("opponentRank");
					BoardType boardType = BoardType.valueOf(requestJSN.getString("boardType"));

					System.out.println("should notify game invitation listeners");
					ListenersFactory.notifyGameInvitationListeners(opponentId, opponentName, rank, boardType == BoardType.BOARD_3X3 ? 3 : 5);
					System.out.println("notified game invitation listeners");
				} catch (Exception e) {
					System.out.println("waiting for invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void rejectInvitation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("status", "FAILURE");
					responseJSN.put("additionalInfo", "rejected");
					System.out.println("rejected should be sent");
					UserServiceImpl.OUTPUT_STREAM.writeUTF(responseJSN.toString());
					System.out.println("rejected sent");
//					UserServiceImpl.OUTPUT_STREAM.flush();
				} catch (Exception e) {
					System.out.println("reject invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void acceptInvitation() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("status", "SUCCESS");
					responseJSN.put("additionalInfo", "accepted");
					System.out.println("accepted should be sent");
					UserServiceImpl.OUTPUT_STREAM.writeUTF(responseJSN.toString());
					System.out.println("accepted sent");
//					UserServiceImpl.OUTPUT_STREAM.flush();
				} catch (Exception e) {
					System.out.println("accept invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}
}