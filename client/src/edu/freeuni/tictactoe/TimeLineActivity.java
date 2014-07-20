package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.server.AppController;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class TimeLineActivity extends Activity {

	private List<HistoryEntry> historyEntries;

	private HistoryAdapter adapter;

	private TextView numOfWinnings;
	private TextView numOfLosses;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_line_activity);
		ListView listView = (ListView) findViewById(R.id.timeLineList);
		numOfWinnings = (TextView) findViewById(R.id.sumOfWinnings);
		numOfLosses = (TextView) findViewById(R.id.sumOfLosses);

		adapter = new HistoryAdapter(this);
		listView.setAdapter(adapter);
		historyEntries = new Gson().fromJson(getIntent().getExtras().getString("history"), new TypeToken<List<HistoryEntry>>(){}.getType());
		initAdapter();
	}


	@Override
	protected void onResume() {
		super.onResume();
		initAdapter();
	}

	private void initAdapter() {
		adapter.clear();
		for (HistoryEntry historyEntry : historyEntries) {
			adapter.add(historyEntry);
		}

		numOfLosses.setText(getCount(-1));
		numOfWinnings.setText(getCount(1));
	}

	private String getCount(int value) {
		int result = 0;
		for (HistoryEntry entry : historyEntries) {
			if (entry.getResult() == value) {
				result++;
			}
		}
		return String.valueOf(result);
	}

	private class HistoryAdapter extends ArrayAdapter<HistoryEntry> {

		private Context context;

		public HistoryAdapter(Context context) {
			super(context, R.layout.history_entry);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View row = convertView;
			UsersListHolder holder;

			if (row == null) {
				row = inflater.inflate(R.layout.history_entry, parent, false);
				holder = new UsersListHolder();
				holder.selfUsername = ((TextView) row.findViewById(R.id.selfUsername));
				holder.opponentUsername = ((TextView) row.findViewById(R.id.opponentUsername));
				holder.result = ((TextView) row.findViewById(R.id.result));
				holder.linearLayout = ((LinearLayout) row.findViewById(R.id.historyItemLayout));
				row.setTag(holder);
			} else {
				holder = (UsersListHolder) row.getTag();
			}

			HistoryEntry item = getItem(position);

			holder.selfUsername.setText(AppController.USERNAME);
			holder.opponentUsername.setText(item.getOpponentUsername());

			if (item.getResult() == 1) {
				holder.result.setText(getResources().getString(R.string.win));
				holder.linearLayout.setBackgroundColor(Color.parseColor("#5A00FF00"));
			} else {
				holder.result.setText(getResources().getString(R.string.lose));
				holder.linearLayout.setBackgroundColor(Color.parseColor("#5AFF0000"));
			}
			return row;
		}

		private class UsersListHolder {
			TextView selfUsername;
			TextView opponentUsername;
			TextView result;
			LinearLayout linearLayout;
		}
	}
}