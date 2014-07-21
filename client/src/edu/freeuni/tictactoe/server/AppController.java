package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.game.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

public class AppController {

	public static Socket SOCKET;
	public static DataInputStream INPUT_STREAM;
	public static DataOutputStream OUTPUT_STREAM;
	public static Board BOARD;

	public static String SERVER_IP = "10.131.33.114";
	public static int SERVER_PORT = 8080;

	public static String USERNAME;
	public static UserMode USER_MODE;

	public static List<HistoryEntry> HISTORY;
	public static List<UserEntry> USERS;

	public static UserEntry getUserById(int id) {
		for (UserEntry entry : USERS) {
			if (id == entry.getId()) {
				return entry;
			}
		}
		return null;
	}
}