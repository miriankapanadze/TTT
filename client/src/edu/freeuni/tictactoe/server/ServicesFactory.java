package edu.freeuni.tictactoe.server;

public class ServicesFactory {

	public static UserService getUserService() {
		return new UserServiceImpl();
	}
}