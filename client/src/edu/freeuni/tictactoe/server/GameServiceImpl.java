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

					UserServiceImpl.OUTPUT_STREAM.writeObject(requestJSON.toString());

					String responseString = (String) UserServiceImpl.INPUT_STREAM.readObject();
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					ListenersFactory.notifyStartGameListeners(type == BoardType.BOARD_3X3 ? 3 : 5, status);

				} catch (Exception e) {
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

					UserServiceImpl.OUTPUT_STREAM.writeObject(requestJSON.toString());
					waitForOpponentMove();

				} catch (Exception e) {
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
					String responseString = (String) UserServiceImpl.INPUT_STREAM.readObject();
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					ListenersFactory.notifyGameMoveListeners(responseJSON.getInt("x"), responseJSON.getInt("y"));

				} catch (Exception e) {
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
					String requestSTR = (String)UserServiceImpl.INPUT_STREAM.readObject();
					System.out.println("received invitation");
					JSONObject requestJSN = new JSONObject(requestSTR);
					int opponentId = requestJSN.getInt("opponentId");
					String opponentName = requestJSN.getString("opponentName");
					int rank = requestJSN.getInt("opponentRank");
					BoardType boardType = BoardType.valueOf(requestJSN.getString("boardType"));

					ListenersFactory.notifyGameInvitationListeners(opponentId, opponentName, rank, boardType == BoardType.BOARD_3X3 ? 3 : 5);
				} catch (Exception e) {
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
//					responseJSN.put("additionalInfo", "rejected");
//					UserServiceImpl.OUTPUT_STREAM.reset();
					UserServiceImpl.OUTPUT_STREAM.writeObject(responseJSN.toString());
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
//					responseJSN.put("additionalInfo", "accepted");
//					UserServiceImpl.OUTPUT_STREAM.reset();
//					UserServiceImpl.OUTPUT_STREAM.writeObject(responseJSN.toString());
				} catch (Exception e) {
					System.out.println("accept invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}
}