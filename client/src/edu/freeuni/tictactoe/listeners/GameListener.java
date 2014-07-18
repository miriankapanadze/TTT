package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.Status;

public interface GameListener {

	void startGame(int board, Status status);

	void onOpponentMove(int x, int y);
}