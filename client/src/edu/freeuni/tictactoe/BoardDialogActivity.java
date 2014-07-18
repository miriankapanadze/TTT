package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import edu.freeuni.tictactoe.listeners.GameStartListener;
import edu.freeuni.tictactoe.model.BoardType;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.ServicesFactory;

@SuppressWarnings("ConstantConditions")
public class BoardDialogActivity extends Activity implements GameStartListener {

	private int opponentId;
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board_dialog);

		Button nine = (Button) findViewById(R.id.nineGrid);
		Button twentyFive = (Button) findViewById(R.id.twentyFiveGrid);
		opponentId = getIntent().getExtras().getInt("opponent");

		nine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ServicesFactory.getGameService().startGame(opponentId, BoardType.BOARD_3X3);
			}
		});

		twentyFive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ServicesFactory.getGameService().startGame(opponentId, BoardType.BOARD_5X5);
			}
		});
		ServicesFactory.addGameStartListener(this);
		handler = new Handler();
	}

	@Override
	public void startGame(int boardSize, final Status status) {
		if (status.getType() == Status.Type.SUCCESS) {
			Intent intent = new Intent(BoardDialogActivity.this, BoardActivity.class);
			intent.putExtra("size", boardSize);
			intent.putExtra("mode", getIntent().getExtras().getString("mode"));
			intent.putExtra("opponentId", opponentId);
			startActivity(intent);
		} else {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(BoardDialogActivity.this, status.getAdditionalInfo(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}