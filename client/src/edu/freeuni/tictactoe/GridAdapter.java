package edu.freeuni.tictactoe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class GridAdapter extends BaseAdapter {
	private Context context;
	private int boardSize;
	private List<Integer> values;

	public GridAdapter(Context c, int boardSize, List<Integer> values) {
		this.context = c;
		this.boardSize = boardSize;
		this.values = values;
	}

	public int getCount() {
		return boardSize*boardSize;
	}

	public List<Integer> getValues() {
		return values;
	}

	public Object getItem(int position) {
		return values.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setPadding(1, 1, 1, 1);
		} else {
			imageView = (ImageView) convertView;
		}
		int value = (Integer)getItem(position);

		switch (value) {
			case 0:
				imageView.setImageResource(R.drawable.default_xo);
				break;
			case 1:
				imageView.setImageResource(R.drawable.x);
				break;
			case 2:
				imageView.setImageResource(R.drawable.o);
				break;
		}
		return imageView;
	}
}