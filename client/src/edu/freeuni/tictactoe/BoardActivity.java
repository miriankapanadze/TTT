package edu.freeuni.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import edu.freeuni.tictactoe.listeners.GameMoveListener;
import edu.freeuni.tictactoe.listeners.GameOverListener;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.game.Board;

@SuppressWarnings("ConstantConditions")
public class BoardActivity extends Activity implements GameMoveListener, GameOverListener {

	private int size;
	private int self;
	private int opponent;
	private int opponentId;

	public static Board board;
	private GridAdapter adapter;
	private GridView grid;

	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		opponentId = getIntent().getExtras().getInt("opponentId");

		size = getIntent().getExtras().getInt("size");
		board = new Board(size);
		adapter = new GridAdapter(this, size, board.getValues());

		UserMode mode = UserMode.valueOf(getIntent().getExtras().getString("mode"));
		self = mode == UserMode.ACTIVE ? 1 : 2;
		opponent = mode == UserMode.ACTIVE ? 2 : 1;

		grid = (GridView) findViewById(R.id.gridView);
		grid.setNumColumns(size);
		grid.setAdapter(adapter);
		handler = new Handler();

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (board.get(position) != 0) {
					return;
				}
				if (board.getTurn() == self) {
					adapter.getValues().set(position, self);
					adapter.notifyDataSetChanged();
					ServicesFactory.getGameService().makeMove(opponentId, position / size, position % size);
					board.set(position);
				}
			}
		});

		ListenersManager.addGameMoveListener(this);
		ListenersManager.addGameOverListener(this);

		if (mode == UserMode.PASSIVE) {
			ServicesFactory.getGameService().waitForOpponentMove();
		}
	}

	@Override
	public void onOpponentMove(final int x, final int y) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				adapter.getValues().set(x * size + y, opponent);
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	public void onGameOver(final GameStatus gameStatus) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				grid.setEnabled(false);
				Toast.makeText(BoardActivity.this, gameStatus == GameStatus.DRAW ? "ფრე" : (board.getTurn() != self ? "თქვენ მოიგეთ :-)" : "თქვენ წააგეთ :-("), Toast.LENGTH_LONG).show();
			}
		});
	}
}