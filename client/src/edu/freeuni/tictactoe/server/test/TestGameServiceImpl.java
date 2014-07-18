package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.GameType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.GameService;
import edu.freeuni.tictactoe.server.ServicesFactory;

public class TestGameServiceImpl implements GameService {

	@Override
	public void startGame(int opponentId, GameType type) {

		ServicesFactory.notifyStartGameListeners(type == GameType.FIVE_TO_FIVE ? 5 : 3, new Status());
	}

	@Override
	public void move(int x, int y) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.currentThread().sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ServicesFactory.notifyMoveGameListeners(2, 2);
				}
			}).start();
	}
}