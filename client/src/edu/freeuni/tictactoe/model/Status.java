package edu.freeuni.tictactoe.model;

public class Status {

	private Type type;

	private String additionalInfo;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public enum Type {
		SUCCESS,
		FAILURE,
		WARN
	}
}