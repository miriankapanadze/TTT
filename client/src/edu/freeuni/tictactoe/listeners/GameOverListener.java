package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.GameStatus;

public interface GameOverListener {

	void onGameOver(GameStatus gameStatus);
}
