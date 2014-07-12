package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.ServerStatus;

public interface RegistrationService {
	ServerStatus registration(String login, String password, String name);
}