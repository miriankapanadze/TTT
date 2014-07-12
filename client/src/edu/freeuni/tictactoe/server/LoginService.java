package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.ServerStatus;

public interface LoginService {

	ServerStatus login(String login, String password);
}