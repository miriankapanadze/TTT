package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.ServerStatus;

public interface GameListener {

	void startGame(int board, ServerStatus status);

	void move();
}