package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.GameType;

public interface GameService {

	void startGame(int opponentId, GameType type);
}