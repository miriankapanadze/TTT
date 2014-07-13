package edu.freeuni.tictactoe.model;

public class ServerStatus {

	private Status status;

	private String additionalInfo;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public enum Status {
		SUCCESS,
		FAILURE,
		WARN
	}
}
