package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.listeners.RegisterListener;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.UserEntry;

import java.util.ArrayList;
import java.util.List;

public class ServicesFactory {

	private static List<LoginListener> loginListeners = new ArrayList<LoginListener>();
	private static List<RegisterListener> registerListeners = new ArrayList<RegisterListener>();

	public static void addRegisterListener(RegisterListener registerListener) {
		registerListeners.add(registerListener);
	}

	public static void addLoginListener(LoginListener loginListener) {
		loginListeners.add(loginListener);
	}

	public static void notifyLoginListeners(ServerStatus status, List<UserEntry> users) {
		for (LoginListener loginListener : loginListeners) {
			loginListener.onLogin(status, users);
		}
	}

	public static void notifyRegisterListeners(ServerStatus status) {
		for (RegisterListener registerListener : registerListeners) {
			registerListener.onRegister(status);
		}
	}

	public static UserService getUserService() {
		return new UserServiceImpl();
	}
}