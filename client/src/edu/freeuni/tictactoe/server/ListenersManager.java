package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.listeners.GameInvitationListener;
import edu.freeuni.tictactoe.listeners.GameMoveListener;
import edu.freeuni.tictactoe.listeners.GameOverListener;
import edu.freeuni.tictactoe.listeners.GameStartListener;
import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.listeners.RegisterListener;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;

import java.util.List;

public class ListenersManager {
	private static LoginListener loginListener;
	private static RegisterListener registerListener;
	private static GameStartListener gameStartListener;
	private static GameMoveListener gameMoveListener;
	private static GameInvitationListener gameInvitationListener;
	private static GameOverListener gameOverListener;


	public static void addGameOverListener(GameOverListener overListener) {
		gameOverListener = overListener;
	}

	public static void addGameInvitationListener(GameInvitationListener invitationListener) {
		gameInvitationListener = invitationListener;
	}

	public static void addRegisterListener(RegisterListener registerListener) {
		ListenersManager.registerListener= registerListener;
	}

	public static void addLoginListener(LoginListener loginListener) {
		ListenersManager.loginListener = loginListener;
	}

	public static void addGameStartListener(GameStartListener gameListener) {
		ListenersManager.gameStartListener = gameListener;
	}

	public static void addGameMoveListener(GameMoveListener moveListener) {
		ListenersManager.gameMoveListener = moveListener;
	}

	public static void notifyLoginListeners(Status status, List<UserEntry> users, List<HistoryEntry> historyEntries, UserMode mode) {
		loginListener.onLogin(status, users, historyEntries, mode);
	}

	public static void notifyRegisterListeners(Status status) {
		registerListener.onRegister(status);
	}

	public static void notifyStartGameListeners(int boardSize, Status status) {
		gameStartListener.startGame(boardSize, status);
	}

	public static void notifyGameMoveListeners(int x, int y) {
		gameMoveListener.onOpponentMove(x, y);
	}

	public static void notifyGameInvitationListeners(int opponentId, String opponentName, int opponentRank, int boardSize) {
		gameInvitationListener.onGameInvitation(opponentId, opponentName, opponentRank, boardSize);
	}

	public static void notifyGameOverListeners(GameStatus gameStatus) {
		gameOverListener.onGameOver(gameStatus);
	}
}