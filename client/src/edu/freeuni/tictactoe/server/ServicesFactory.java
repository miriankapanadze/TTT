package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.server.test.TestGameServiceImpl;
import edu.freeuni.tictactoe.server.test.TestUserServiceImpl;

public class ServicesFactory {

	public static UserService getUserService() {
		return new TestUserServiceImpl();
	}

	public static GameService getGameService() {
		return new TestGameServiceImpl();
	}
}