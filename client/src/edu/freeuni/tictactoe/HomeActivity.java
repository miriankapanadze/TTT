package edu.freeuni.tictactoe;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings({"deprecation"})
public class HomeActivity extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		TabHost tabHost = getTabHost();
		tabHost.setBackgroundColor(Color.BLACK);

		Intent usersActivity = new Intent().setClass(this, UsersActivity.class);
		TabHost.TabSpec history = tabHost.newTabSpec("Users")
				.setIndicator(null, getResources().getDrawable(R.drawable.people))
				.setContent(usersActivity);

		Intent historyIntent = new Intent().setClass(this, TimeLineActivity.class);
		TabHost.TabSpec timeLine = tabHost.newTabSpec("BarChart")
				.setIndicator(null, getResources().getDrawable(R.drawable.history))
				.setContent(historyIntent);

		tabHost.addTab(history);
		tabHost.addTab(timeLine);
		tabHost.setCurrentTab(0);
	}
}