package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.UserService;

import java.util.ArrayList;
import java.util.List;

public class TestUserServiceImpl implements UserService {

	@Override
	public void login(LoginRequest request) {
		Status serverStatus = new Status();
		serverStatus.setType(Status.Type.SUCCESS);
		serverStatus.setAdditionalInfo("SUCCESS");

		List<UserEntry> userEntries = new ArrayList<>();
		UserEntry user = new UserEntry();
		user.setRank(3);
		user.setName("Test");
		user.setUsername("tesT");
		userEntries.add(user);

		ListenersManager.notifyLoginListeners(serverStatus);
	}

	@Override
	public void register(RegistrationRequest registrationRequest) {

	}

	@Override
	public void logout() {

	}
}