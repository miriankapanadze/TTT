package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.GameService;
import edu.freeuni.tictactoe.server.ServicesFactory;

public class TestGameServiceImpl implements GameService {

	@Override
	public void startGame(int opponentId, BoardType type) {

		ServicesFactory.notifyStartGameListeners(type == BoardType.BOARD_5X5 ? 5 : 3, new Status());
	}

	@Override
	public void move(int opponentId, int x, int y) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ServicesFactory.notifyGameMoveListeners(2, 2);
				}
			}).start();
	}

	@Override
	public void waitForOpponent() {

	}

	@Override
	public void rejectInvitation() {

	}

	@Override
	public void acceptInvitation() {

	}
}