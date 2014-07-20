package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.GameService;
import edu.freeuni.tictactoe.server.ListenersManager;

public class TestGameServiceImpl implements GameService {

	@Override
	public void startGame(int opponentId, BoardType type) {

		Status status = new Status();
		status.setType(Status.Type.SUCCESS);
		ListenersManager.notifyStartGameListeners(type == BoardType.BOARD_5X5 ? 5 : 3, status);
	}

	@Override
	public void makeMove(int opponentId, int x, int y) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ListenersManager.notifyGameMoveListeners(2, 2);
				}
			}).start();
	}

	@Override
	public void waitForOpponentMove() {
	}

	@Override
	public void waitForInvitation() {

	}

	@Override
	public void rejectInvitation(int opponentId) {

	}

	@Override
	public void acceptInvitation(int opponentId) {

	}

	@Override
	public void notifyServerOnGameOver(GameStatus gameStatus, int opponentId) {

	}
}