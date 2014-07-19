package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.BoardType;

public interface GameService {

	void startGame(int opponentId, BoardType type);

	void makeMove(int opponentId, int x, int y);

	void waitForOpponentMove();

	void waitForInvitation();

	void rejectInvitation();

	void acceptInvitation();
}