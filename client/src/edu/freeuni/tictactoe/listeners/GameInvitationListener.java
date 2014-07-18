package edu.freeuni.tictactoe.listeners;

public interface GameInvitationListener {

	void onGameInvitation(int opponentId, String opponentName, int opponentRank, int boardSize);
}