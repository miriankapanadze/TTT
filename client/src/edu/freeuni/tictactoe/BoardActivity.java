package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import edu.freeuni.tictactoe.listeners.GameMoveListener;
import edu.freeuni.tictactoe.listeners.GameOverListener;
import edu.freeuni.tictactoe.model.GameStatus;
import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.AppController;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.game.Board;

@SuppressWarnings("ConstantConditions")
public class BoardActivity extends Activity implements GameMoveListener, GameOverListener {

	private int size;
	private int self;
	private int opponent;
	private int opponentId;

	private GridAdapter adapter;
	private GridView grid;

	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		opponentId = getIntent().getExtras().getInt("opponentId");

		size = getIntent().getExtras().getInt("size");
		AppController.BOARD = new Board(size);
		adapter = new GridAdapter(this, size, AppController.BOARD.getValues());

		UserMode mode = AppController.USER_MODE;
		self = mode == UserMode.ACTIVE ? 1 : 2;
		opponent = mode == UserMode.ACTIVE ? 2 : 1;

		grid = (GridView) findViewById(R.id.gridView);
		grid.setNumColumns(size);
		grid.setAdapter(adapter);
		handler = new Handler();
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if (AppController.BOARD.get(position) != 0) {
					return;
				}
				if (AppController.BOARD.getTurn() == self) {
					adapter.getValues().set(position, self);
					adapter.notifyDataSetChanged();
					ServicesFactory.getGameService().makeMove(opponentId, position / size, position % size);
					AppController.BOARD.set(position);
				}
			}
		});

		ListenersManager.addGameMoveListener(this);
		ListenersManager.addGameOverListener(this);

		if (mode == UserMode.PASSIVE) {
			ServicesFactory.getGameService().waitForOpponentMove();
		}
		String fireName = mode == UserMode.ACTIVE ? AppController.USERNAME : AppController.getUserById(opponentId).getUsername();
		String waterName = mode != UserMode.ACTIVE ? AppController.USERNAME : AppController.getUserById(opponentId).getUsername();

		((TextView) findViewById(R.id.fire)).setText(fireName);
		((TextView) findViewById(R.id.water)).setText(waterName);
	}

	@Override
	protected void onPause() {
		if (grid.isEnabled()) {
			ServicesFactory.getGameService().cancelGame(opponentId);
		}
		super.onPause();
	}

	@Override
	public void onOpponentMove(final Status status, final int x, final int y) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (status.getType() != Status.Type.FAILURE) {
					adapter.getValues().set(x * size + y, opponent);
					adapter.notifyDataSetChanged();
				} else {
					grid.setEnabled(false);
					Toast.makeText(BoardActivity.this, status.getAdditionalInfo(), Toast.LENGTH_LONG).show();
					BoardActivity.this.finish();

					if (AppController.USER_MODE == UserMode.PASSIVE) {
						ServicesFactory.getGameService().waitForInvitation();
					}
				}
			}
		});
	}

	@Override
	public void onGameOver(final GameStatus gameStatus) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				grid.setEnabled(false);
				String message = gameStatus == GameStatus.DRAW ?
						getResources().getString(R.string.draw) :
							(AppController.BOARD.getTurn() != self ?
								getResources().getString(R.string.youWin) :
								getResources().getString(R.string.youLose));
				Toast.makeText(BoardActivity.this, message, Toast.LENGTH_LONG).show();

				HistoryEntry historyEntry = new HistoryEntry();
				historyEntry.setResult(gameStatus == GameStatus.DRAW ? 0 : (AppController.BOARD.getTurn() != self ? 1 : -1));
				UserEntry userEntry = AppController.getUserById(opponentId);
				userEntry.setRank(userEntry.getRank() - historyEntry.getResult());
				if (userEntry.getRank() < 0) {
					userEntry.setRank(0);
				}
				historyEntry.setOpponentUsername(userEntry.getUsername());

				AppController.HISTORY.add(historyEntry);
			}
		});
	}
}