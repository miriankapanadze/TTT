package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import edu.freeuni.tictactoe.listeners.GameListener;
import edu.freeuni.tictactoe.model.GameType;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.server.ServicesFactory;

@SuppressWarnings("ConstantConditions")
public class BoardDialogActivity extends Activity implements GameListener {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board_dialog);

		Button nine = (Button) findViewById(R.id.nineGrid);
		Button twentyFive = (Button) findViewById(R.id.twentyFiveGrid);
		final int opponentId = getIntent().getExtras().getInt("opponent");

		nine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ServicesFactory.getGameService().startGame(opponentId, GameType.THREE_TO_THREE);
			}
		});

		twentyFive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ServicesFactory.getGameService().startGame(opponentId, GameType.FIVE_TO_FIVE);
			}
		});
		ServicesFactory.addGameListener(this);
	}

	@Override
	public void startGame(int board, ServerStatus status) {
		Intent intent = new Intent(BoardDialogActivity.this, BoardActivity.class);
		intent.putExtra("boardSize", board);
		startActivity(intent);
	}

	@Override
	public void move() {

	}
}