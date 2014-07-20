package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.Status;

public interface GameMoveListener {

	void onOpponentMove(Status status, int x, int y);
}