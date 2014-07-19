package edu.freeuni.tictactoe.server.game;

import java.util.ArrayList;
import java.util.List;

public class Board {

	private int[][] board;
	private int size;
	private int turn = 1;

	public Board(int size) {
		this.size = size;
		board = new int[size][size];
	}

	public int get(int x, int y) {
		return board[x][y];
	}

	public int get(int index) {
		return get(index / size, index % size);
	}

	public void set(int x, int y) {
		board[x][y] = turn;
		turn = (turn == 1) ? 2 : 1;
	}

	public void set(int index) {
		set(index / size, index % size);
	}

	public int getTurn() {
		return turn;
	}

	public List<Integer> getValues() {
		List<Integer> result = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				result.add(board[i][j]);
			}
		}
		return result;
	}
}