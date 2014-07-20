package edu.freeuni.tictactoe.server;

import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.game.Board;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class AppController {
	public static Socket SOCKET;
	public static DataInputStream INPUT_STREAM;
	public static DataOutputStream OUTPUT_STREAM;
	public static Board BOARD;

	public static String SERVER_IP = "192.168.0.102";
	public static int SERVER_PORT = 8080;

	public static String USERNAME;
	public static UserMode USER_MODE;
}