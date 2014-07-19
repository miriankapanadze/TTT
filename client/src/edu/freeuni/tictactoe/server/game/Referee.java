package edu.freeuni.tictactoe.server.game;

import edu.freeuni.tictactoe.model.GameStatus;

public class Referee {

	private static Board board;
	private static int [][] boardArray;
	private static int size;
	private static int x;
	private static int y;
	private static int value;

	public static GameStatus checkGameStatus(Board board, int x, int y) {
		Referee.board = board;
		boardArray = board.getBoardArray();
		size = boardArray.length;
		Referee.x = x;
		Referee.y = y;
		value = boardArray[x][y];

		switch (size) {
			case 3: return checkGameStatusOn3X3();
			case 5: return checkGameStatusOn5X5();
			default: return GameStatus.IN_PROGRESS;
		}
	}

	private static GameStatus checkGameStatusOn3X3() {
		if (checkColumnOn3X3() || checkRowOn3X3() || checkDiagonalOn3X3() || checkAntiDiagonalOn3X3()) {
			return GameStatus.WINNER_ANNOUNCED;
		}
		return (board.getMovesCount() == size * size) ? GameStatus.DRAW : GameStatus.IN_PROGRESS;
	}

	private static boolean checkColumnOn3X3() {
		for (int i = 0; i < size; i++) {
			if (boardArray[x][i] != value) {
				break;
			}
			if (i == size - 1) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkRowOn3X3() {
		for (int i = 0; i < size; i++) {
			if (boardArray[i][y] != value) {
				break;
			}
			if (i == size - 1) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkDiagonalOn3X3() {
		for (int i = 0; i < size; i++) {
			if (boardArray[i][i] != value) {
				break;
			}
			if (i == size - 1) {
				return true;
			}
		}
		return false;
	}

	private static boolean checkAntiDiagonalOn3X3() {
		for (int i = 0; i < size; i++) {
			if (boardArray[i][size - 1 - i] != value) {
				break;
			}
			if (i == size - 1) {
				return true;
			}
		}
		return false;
	}

	private static GameStatus checkGameStatusOn5X5() {
		if (checkColumnOn5X5() || checkRowOn5X5() || checkDiagonalOn5X5() || checkAntiDiagonalOn5X5()) {
			return GameStatus.WINNER_ANNOUNCED;
		}
		return (board.getMovesCount() == size * size) ? GameStatus.DRAW : GameStatus.IN_PROGRESS;
	}

	private static boolean checkColumnOn5X5() {
		for (int i = 1; i < size - 1; i++) {
			if (boardArray[x][i] != value) {
				break;
			}
			if (i == size - 2) {
				return boardArray[x][0] == value || boardArray[x][4] == value;
			}
		}
		return false;
	}

	private static boolean checkRowOn5X5() {
		for (int i = 1; i < size - 1; i++) {
			if (boardArray[i][y] != value) {
				break;
			}
			if (i == size - 2) {
				return boardArray[0][y] == value || boardArray[4][y] == value;
			}
		}
		return false;
	}

	private static boolean checkDiagonalOn5X5() {
		for (int i = 1; i < size - 1; i++) {
			if (boardArray[i][i] != value) {
				break;
			}
			if (i == size - 2) {
				return boardArray[0][0] == value || boardArray[4][4] == value;
			}
		}
		return false;
	}

	private static boolean checkAntiDiagonalOn5X5() {
		for (int i = 1; i < size - 1; i++) {
			if (boardArray[i][size - 1 - i] != value) {
				break;
			}
			if (i == size - 2) {
				return boardArray[0][4] == value || boardArray[4][0] == value;
			}
		}
		return false;
	}
}
