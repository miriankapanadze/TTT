package edu.freeuni.tictactoe.server.test;

import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

import java.util.ArrayList;
import java.util.List;

public class TestUserServiceImpl implements UserService {

	@Override
	public void login(LoginRequest request) {
		ServerStatus serverStatus = new ServerStatus();
		serverStatus.setStatus(ServerStatus.Status.SUCCESS);
		serverStatus.setAdditionalInfo("SUCCESS");

		List<UserEntry> userEntries = new ArrayList<UserEntry>();
		UserEntry user = new UserEntry();
		user.setRank(3);
		user.setName("Test");
		user.setUsername("tesT");
		userEntries.add(user);

		ServicesFactory.notifyLoginListeners(serverStatus, userEntries, UserMode.ACTIVE);
	}

	@Override
	public void register(RegistrationRequest registrationRequest) {

	}

	@Override
	public void logout() {

	}
}