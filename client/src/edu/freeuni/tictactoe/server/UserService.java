package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.ServerStatus;

public interface UserService {

	ServerStatus login(LoginRequest request);

	ServerStatus register(RegistrationRequest registrationRequest);
}
