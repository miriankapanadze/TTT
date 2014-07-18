package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.listeners.GameListener;
import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.listeners.RegisterListener;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.server.test.TestGameServiceImpl;
import edu.freeuni.tictactoe.server.test.TestUserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class ServicesFactory {

	private static List<LoginListener> loginListeners = new ArrayList<LoginListener>();
	private static List<RegisterListener> registerListeners = new ArrayList<RegisterListener>();
	private static List<GameListener> gameListeners = new ArrayList<GameListener>();

	public static void addRegisterListener(RegisterListener registerListener) {
		registerListeners.add(registerListener);
	}

	public static void addLoginListener(LoginListener loginListener) {
		loginListeners.add(loginListener);
	}

	public static void addGameListener(GameListener gameListener) {
		gameListeners.add(gameListener);
	}

	public static void notifyLoginListeners(Status status, List<UserEntry> users, UserMode mode) {
		for (LoginListener loginListener : loginListeners) {
			loginListener.onLogin(status, users, mode);
		}
	}

	public static void notifyRegisterListeners(Status status) {
		for (RegisterListener registerListener : registerListeners) {
			registerListener.onRegister(status);
		}
	}

	public static UserService getUserService() {
		return new TestUserServiceImpl();
	}

	public static GameService getGameService() {
		return new TestGameServiceImpl();
	}

	public static void notifyStartGameListeners(int board, Status status) {
		for (GameListener gameListener : gameListeners) {
			gameListener.startGame(board, status);
		}
	}

	public static void notifyMoveGameListeners() {

	}
}