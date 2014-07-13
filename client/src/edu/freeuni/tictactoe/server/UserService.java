package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;

public interface UserService {

	void login(LoginRequest request);

	void register(RegistrationRequest registrationRequest);
}