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

					UserServiceImpl.outputStream.writeObject(requestJSON.toString());

					String responseString = (String) UserServiceImpl.inputStream.readObject();
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					ServicesFactory.notifyStartGameListeners(type == BoardType.BOARD_3X3 ? 3 : 5, status);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void move(final int opponentId, final int x, final int y) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				JSONObject requestJSON = new JSONObject();
				try {
					requestJSON.put("requestType", RequestType.MOVE.name());
					requestJSON.put("opponentId", opponentId);
					requestJSON.put("x", x);
					requestJSON.put("y", y);

					UserServiceImpl.outputStream.writeObject(requestJSON.toString());

					String responseString = (String) UserServiceImpl.inputStream.readObject();
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					ServicesFactory.notifyGameMoveListeners(responseJSON.getInt("x"), responseJSON.getInt("y"));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void waitForOpponent() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String requestSTR = (String)UserServiceImpl.inputStream.readObject();
					JSONObject requestJSN = new JSONObject(requestSTR);
					int opponentId = requestJSN.getInt("opponentId");
					String opponentName = requestJSN.getString("opponentName");
					int rank = requestJSN.getInt("opponentRank");
					BoardType boardType = BoardType.valueOf(requestJSN.getString("boardType"));

					ServicesFactory.notifyGameInvitationListeners(opponentId, opponentName, rank, boardType == BoardType.BOARD_3X3 ? 3 : 5);
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
					responseJSN.put("status", Status.Type.FAILURE);
					UserServiceImpl.outputStream.writeObject(responseJSN.toString());
				} catch (Exception e) {
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
					responseJSN.put("status", Status.Type.SUCCESS);
					UserServiceImpl.outputStream.writeObject(responseJSN.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}