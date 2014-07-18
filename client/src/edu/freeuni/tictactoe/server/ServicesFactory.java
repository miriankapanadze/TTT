package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.listeners.GameMoveListener;
import edu.freeuni.tictactoe.listeners.GameStartListener;
import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.listeners.RegisterListener;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;

import java.util.ArrayList;
import java.util.List;

public class ServicesFactory {

	private static List<LoginListener> loginListeners = new ArrayList<LoginListener>();
	private static List<RegisterListener> registerListeners = new ArrayList<RegisterListener>();
	private static List<GameStartListener> gameStartListeners = new ArrayList<GameStartListener>();
	private static List<GameMoveListener> gameMoveListeners = new ArrayList<GameMoveListener>();

	public static void addRegisterListener(RegisterListener registerListener) {
		registerListeners.add(registerListener);
	}

	public static void addLoginListener(LoginListener loginListener) {
		loginListeners.add(loginListener);
	}

	public static void addGameStartListener(GameStartListener gameListener) {
		gameStartListeners.add(gameListener);
	}

	public static void addGameMoveListener(GameMoveListener moveListener) {
		gameMoveListeners.add(moveListener);
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
		return new UserServiceImpl();
	}

	public static GameService getGameService() {
		return new GameServiceImpl();
	}

	public static void notifyStartGameListeners(int boardSize, Status status) {
		for (GameStartListener gameListener : gameStartListeners) {
			gameListener.startGame(boardSize, status);
		}
	}

	public static void notifyGameMoveListeners(int x, int y) {
		for (GameMoveListener gameListener : gameMoveListeners) {
			gameListener.onOpponentMove(x, y);
		}
	}
}