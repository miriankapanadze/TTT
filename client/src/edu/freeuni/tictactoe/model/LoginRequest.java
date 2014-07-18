package edu.freeuni.tictactoe.model;

public class LoginRequest {

	private String userName;

	private String password;

	private UserMode mode;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserMode getUserMode() {
		return mode;
	}

	public void setUserMode(UserMode mode) {
		this.mode = mode;
	}
}