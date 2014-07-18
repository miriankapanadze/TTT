package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class BoardActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.board);

		int boardSize = getIntent().getExtras().getInt("boardSize");

		GridView grid = (GridView) findViewById(R.id.gridView);
		grid.setAdapter(new GridAdapter(this, boardSize));

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				Toast.makeText(BoardActivity.this, "" + position, Toast.LENGTH_SHORT).show();
				((ImageView)v).setImageResource(R.drawable.x);
			}
		});

	}
}