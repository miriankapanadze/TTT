package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.GameService;
import edu.freeuni.tictactoe.server.ListenersFactory;

public class TestGameServiceImpl implements GameService {

	@Override
	public void startGame(int opponentId, BoardType type) {

		ListenersFactory.notifyStartGameListeners(type == BoardType.BOARD_5X5 ? 5 : 3, new Status());
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
					ListenersFactory.notifyGameMoveListeners(2, 2);
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
}