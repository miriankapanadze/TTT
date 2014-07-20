package edu.freeuni.tictactoe;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.freeuni.tictactoe.model.HistoryEntry;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;

import java.util.List;

@SuppressWarnings({"deprecation", "ConstantConditions"})
public class HomeActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		TabHost tabHost = getTabHost();
		tabHost.setBackgroundColor(Color.BLACK);

		List<UserEntry> userEntries = new Gson().fromJson(getIntent().getExtras().getString("users"), new TypeToken<List<UserEntry>>(){}.getType());
		UserMode mode = UserMode.valueOf(getIntent().getExtras().getString("mode"));

		Intent usersActivity = new Intent().setClass(this, UsersActivity.class);
		usersActivity.putExtra("users", new Gson().toJson(userEntries));
		usersActivity.putExtra("mode", mode.name());


		List<HistoryEntry> historyEntries = new Gson().fromJson(getIntent().getExtras().getString("history"), new TypeToken<List<HistoryEntry>>(){}.getType());
		TabHost.TabSpec history = tabHost.newTabSpec("Users")
				.setIndicator(null, getResources().getDrawable(R.drawable.people))
				.setContent(usersActivity);

		Intent historyIntent = new Intent().setClass(this, TimeLineActivity.class);
		historyIntent.putExtra("history", new Gson().toJson(historyEntries));

		TabHost.TabSpec timeLine = tabHost.newTabSpec("BarChart")
				.setIndicator(null, getResources().getDrawable(R.drawable.history))
				.setContent(historyIntent);

		tabHost.addTab(history);
		tabHost.addTab(timeLine);
		tabHost.setCurrentTab(0);
	}
}