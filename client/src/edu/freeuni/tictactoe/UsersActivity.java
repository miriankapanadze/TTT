package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;

import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class UsersActivity extends Activity {

	private List<UserEntry> userEntries;
	private UsersAdapter adapter;
	private UserMode mode;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_list);
		ListView listView = (ListView) findViewById(R.id.list);
		adapter = new UsersAdapter(this);
		listView.setAdapter(adapter);

		userEntries = new Gson().fromJson(getIntent().getExtras().getString("users"), new TypeToken<List<UserEntry>>(){}.getType());
		mode = UserMode.valueOf(getIntent().getExtras().getString("mode"));

		if (mode == UserMode.ACTIVE) {
			registerForContextMenu(listView);
		}

		listView.setItemsCanFocus(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initAdapter();
	}

	private void initAdapter() {
		adapter.clear();

		for (UserEntry entry : userEntries) {
			adapter.add(entry);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			ListView lv = (ListView) v;
			AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
			UserEntry obj = (UserEntry) lv.getItemAtPosition(acmi.position);

			menu.add(1, obj.getId(), ContextMenu.NONE, getResources().getString(R.string.startGame));
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Log.i("categories", "onOptionsItemSelected");
		switch (item.getGroupId()) {
			case 1:
				int userId = item.getItemId();
				Toast.makeText(this, "starting game", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(UsersActivity.this, BoardDialogActivity.class);
				intent.putExtra("opponent", userId);
				intent.putExtra("mode", UserMode.ACTIVE.name());

				startActivity(intent);
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class UsersAdapter extends ArrayAdapter<UserEntry> {

		private Context context;

		public UsersAdapter(Context context) {
			super(context, R.layout.users_list_item);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflanter = ((Activity) context).getLayoutInflater();
			View row = convertView;
			PaymentsListHolder holder;

			if (row == null) {
				row = inflanter.inflate(R.layout.users_list_item, parent, false);
				holder = new PaymentsListHolder();
				holder.name = ((TextView) row.findViewById(R.id.name));
				holder.username = ((TextView) row.findViewById(R.id.userName));
				holder.rank = ((TextView) row.findViewById(R.id.rank));
				row.setTag(holder);
			} else {
				holder = (PaymentsListHolder) row.getTag();
			}

			UserEntry item = getItem(position);

			holder.name.setText(item.getName());
			holder.username.setText(item.getUsername());
			holder.rank.setText(String.valueOf(item.getRank()));

			return row;
		}

		private class PaymentsListHolder {
			TextView name;
			TextView username;
			TextView rank;
		}
	}
}