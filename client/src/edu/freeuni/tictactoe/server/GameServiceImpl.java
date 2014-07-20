package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.RequestType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.game.Referee;
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
					AppController.OUTPUT_STREAM.writeUTF(requestJSON.toString());
					System.out.println("starting game request written to stream");

					System.out.println("waiting for response for starting game request");
					String responseString = AppController.INPUT_STREAM.readUTF();
					System.out.println("starting game response received");

					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));

					System.out.println("should notify starting game listeners");
					ListenersManager.notifyStartGameListeners(type == BoardType.BOARD_3X3 ? 3 : 5, status);
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
					AppController.OUTPUT_STREAM.writeUTF(requestJSON.toString());
					System.out.println("move made");

					GameStatus gameStatus = Referee.checkGameStatus(AppController.BOARD, x, y);
					if (gameStatus != GameStatus.IN_PROGRESS) {
						ListenersManager.notifyGameOverListeners(gameStatus);
						notifyServerOnGameOver(gameStatus, opponentId);

						if (AppController.USER_MODE == UserMode.PASSIVE) {
							waitForInvitation();
						}
					} else {
						waitForOpponentMove();
					}
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
					String responseString = AppController.INPUT_STREAM.readUTF();
					System.out.println("opponent moved");
					JSONObject responseJSON = new JSONObject(responseString);

					Status status = new Status();
					status.setType(Status.Type.valueOf(responseJSON.getString("status")));
					status.setAdditionalInfo(responseJSON.getString("additionalInfo"));
					int x = status.getType() != Status.Type.FAILURE ? responseJSON.getInt("x") : 0;
					int y = status.getType() != Status.Type.FAILURE ? responseJSON.getInt("y") : 0;
					AppController.BOARD.set(x, y);

					System.out.println("should notify game move listeners");
					ListenersManager.notifyGameMoveListeners(status, x, y);
					System.out.println("notified game move listeners");

					if (status.getType() == Status.Type.FAILURE) {
						return;
					}

					GameStatus gameStatus = Referee.checkGameStatus(AppController.BOARD, x, y);
					if (gameStatus != GameStatus.IN_PROGRESS) {
						ListenersManager.notifyGameOverListeners(gameStatus);

						if (AppController.USER_MODE == UserMode.PASSIVE) {
							waitForInvitation();
						}
					}

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
					String requestSTR = AppController.INPUT_STREAM.readUTF();
					System.out.println("received invitation: " + requestSTR);

					JSONObject requestJSN = new JSONObject(requestSTR);
					int opponentId = requestJSN.getInt("opponentId");
					String opponentName = requestJSN.getString("opponentName");
					int rank = requestJSN.getInt("opponentRank");
					BoardType boardType = BoardType.valueOf(requestJSN.getString("boardType"));

					System.out.println("should notify game invitation listeners");
					ListenersManager.notifyGameInvitationListeners(opponentId, opponentName, rank, boardType == BoardType.BOARD_3X3 ? 3 : 5);
					System.out.println("notified game invitation listeners");
				} catch (Exception e) {
					System.out.println("waiting for invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void rejectInvitation(final int opponentId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("requestType", RequestType.INVITATION_ANSWER.name());
					responseJSN.put("status", Status.Type.FAILURE.name());
					responseJSN.put("additionalInfo", "invitationRejected");
					responseJSN.put("opponentId", opponentId);

					System.out.println("rejected should be sent");
					AppController.OUTPUT_STREAM.writeUTF(responseJSN.toString());
					System.out.println("rejected sent");
				} catch (Exception e) {
					System.out.println("reject invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void acceptInvitation(final int opponentId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("requestType", RequestType.INVITATION_ANSWER.name());
					responseJSN.put("status", Status.Type.SUCCESS.name());
					responseJSN.put("additionalInfo", "accepted");
					responseJSN.put("opponentId", opponentId);

					System.out.println("accepted should be sent");
					AppController.OUTPUT_STREAM.writeUTF(responseJSN.toString());
					System.out.println("accepted sent");
				} catch (Exception e) {
					System.out.println("accept invitation corrupted");
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void notifyServerOnGameOver(final GameStatus gameStatus, final int opponentId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("requestType", RequestType.GAME_OVER.name());
					responseJSN.put("status", Status.Type.SUCCESS.name());
					responseJSN.put("gameStatus", gameStatus.name());
					responseJSN.put("opponentId", opponentId);

					AppController.OUTPUT_STREAM.writeUTF(responseJSN.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void cancelGame(final int opponentId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject responseJSN = new JSONObject();
					responseJSN.put("requestType", RequestType.GAME_CANCELLED.name());
					responseJSN.put("opponentId", opponentId);

					AppController.OUTPUT_STREAM.writeUTF(responseJSN.toString());

					if (AppController.USER_MODE == UserMode.PASSIVE) {
						waitForInvitation();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}