package edu.freeuni.tictactoe.model;

public class Status {

	private TYPE type;

	private String additionalInfo;

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public enum TYPE {
		SUCCESS,
		FAILURE,
		WARN
	}
}