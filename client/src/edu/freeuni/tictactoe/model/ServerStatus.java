package edu.freeuni.tictactoe.model;

public class ServerStatus {
	Status status;

	String additionalInfo;

	private enum Status {
		SUCCESS,
		FAILURE,
		WORN
	}
}