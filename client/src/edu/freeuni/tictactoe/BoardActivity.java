package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import edu.freeuni.tictactoe.listeners.GameListener;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.game.Board;

@SuppressWarnings("ConstantConditions")
public class BoardActivity extends Activity implements GameListener {

	int size;

	int self;
	int selfIcon;
	int opponent;
	int opponentIcon;

	Handler handler;

	Board board;
	GridView grid;
	GridAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		size = getIntent().getExtras().getInt("size");
		board = new Board(size);
		adapter = new GridAdapter(this, size, board.getValues());

		UserMode mode = UserMode.valueOf(getIntent().getExtras().getString("mode"));

		self = mode == UserMode.ACTIVE ? 1 : 2;
		selfIcon = self == 1 ? R.drawable.x : R.drawable.o;

		opponent = mode == UserMode.ACTIVE ? 2 : 1;
		opponentIcon = self == 1 ? R.drawable.o : R.drawable.x;

		grid = (GridView) findViewById(R.id.gridView);
		grid.setNumColumns(size);
		grid.setAdapter(adapter);
		handler = new Handler();

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Toast.makeText(BoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();

				if (board.get(position) != 0) {
					return;
				}
				if (board.getTurn() == self) {
					adapter.getValues().set(position, opponent);
					adapter.notifyDataSetChanged();
					ServicesFactory.getGameService().move(position / size, position % size);
					board.set(position);
				}
			}
		});

		ServicesFactory.addGameListener(this);
	}

	@Override
	public void startGame(int board, Status status) { }

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
}