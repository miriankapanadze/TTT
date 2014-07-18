package edu.freeuni.tictactoe.model;

public class StartGameRequest {

	private int opponentType;
	private GameType type;

	public int getOpponentType() {
		return opponentType;
	}

	public void setOpponentType(int opponentType) {
		this.opponentType = opponentType;
	}

	public GameType getType() {
		return type;
	}

	public void setType(GameType type) {
		this.type = type;
	}
}