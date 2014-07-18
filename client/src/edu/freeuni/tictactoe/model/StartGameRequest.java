package edu.freeuni.tictactoe.model;

public class StartGameRequest {

	private int opponentType;
	private BoardType type;

	public int getOpponentType() {
		return opponentType;
	}

	public void setOpponentType(int opponentType) {
		this.opponentType = opponentType;
	}

	public BoardType getType() {
		return type;
	}

	public void setType(BoardType type) {
		this.type = type;
	}
}