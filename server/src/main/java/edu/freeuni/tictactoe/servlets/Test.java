package edu.freeuni.tictactoe.servlets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Test {

	public static void main(String args[]) {
		try {
			final ServerSocket serverSocket = new ServerSocket(8080);
			Thread newThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Socket socket = serverSocket.accept();
					} catch (IOException e) {
						e.printStackTrace();
					}

					System.out.println("aaaaaa");
				}
			});
			newThread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
