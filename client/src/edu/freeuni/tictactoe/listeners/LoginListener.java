package edu.freeuni.tictactoe.listeners;

import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;

import java.util.List;

public interface LoginListener {

	void onLogin(Status status, List<UserEntry> users, List<HistoryEntry> historyEntries, UserMode mode);
}