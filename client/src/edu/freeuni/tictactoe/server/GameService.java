package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.GameStatus;

public interface GameService {

	void startGame(int opponentId, BoardType type);

	void makeMove(int opponentId, int x, int y);

	void waitForOpponentMove();

	void waitForInvitation();

	void rejectInvitation(int opponentId);

	void acceptInvitation(int opponentId);

	void notifyServerOnGameOver(GameStatus gameStatus, int opponentId);

	void cancelGame(int opponentId);
}