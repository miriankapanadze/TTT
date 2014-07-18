package edu.freeuni.tictactoe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {
	private Context mContext;
	private int boardSize;

	public GridAdapter(Context c, int boardSize) {
		mContext = c;
		this.boardSize = boardSize;
	}

	public int getCount() {
		return boardSize*boardSize;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(boardSize==3 ? new GridView.LayoutParams(50, 50) : new GridView.LayoutParams(30, 30));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(R.drawable.default_xo);
		return imageView;
	}
}