package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.Status;

public interface GameStartListener {

	void startGame(int boardSize, Status status);
}
