package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.UserEntry;

import java.util.List;

public interface LoginListener {

	void onLogin(ServerStatus status, List<UserEntry> users);
}