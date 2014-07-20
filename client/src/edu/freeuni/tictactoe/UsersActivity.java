package edu.freeuni.tictactoe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.freeuni.tictactoe.listeners.GameInvitationListener;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.ServicesFactory;

import java.util.List;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public class UsersActivity extends Activity implements GameInvitationListener {

	private List<UserEntry> userEntries;
	private UsersAdapter adapter;
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.users_list);
		ListView listView = (ListView) findViewById(R.id.list);
		adapter = new UsersAdapter(this);
		listView.setAdapter(adapter);

		userEntries = new Gson().fromJson(getIntent().getExtras().getString("users"), new TypeToken<List<UserEntry>>(){}.getType());
		UserMode mode = UserMode.valueOf(getIntent().getExtras().getString("mode"));

		if (mode == UserMode.ACTIVE) {
			registerForContextMenu(listView);
		} else {
			ListenersManager.addGameInvitationListener(this);
			ServicesFactory.getGameService().waitForInvitation();
		}
		listView.setItemsCanFocus(true);
		handler = new Handler();
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
		switch (item.getGroupId()) {
			case 1:
				int userId = item.getItemId();
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

	@Override
	public void onGameInvitation(final int opponentId, final String opponentName, int opponentRank, final int boardSize) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				System.out.println("onGameInvitation");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(UsersActivity.this);
				alertDialog.setCancelable(false);
				alertDialog.setMessage(opponentName + " " + getResources().getString(boardSize == 3 ? R.string.invitationText3 : R.string.invitationText5));
				alertDialog.setPositiveButton(getResources().getString(R.string.accept), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("onPositiveButton");
						ServicesFactory.getGameService().acceptInvitation(opponentId);
						Intent intent = new Intent(UsersActivity.this, BoardActivity.class);
						intent.putExtra("size", boardSize);
						intent.putExtra("mode", UserMode.PASSIVE.name());
						intent.putExtra("opponentId", opponentId);
						startActivity(intent);
					}
				});

				alertDialog.setNegativeButton(getResources().getString(R.string.reject), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						System.out.println("onNegativeButton");
						ServicesFactory.getGameService().rejectInvitation(opponentId);
						ServicesFactory.getGameService().waitForInvitation();
					}
				});

				alertDialog.create().show();
			}
		});
	}

	private class UsersAdapter extends ArrayAdapter<UserEntry> {

		private Context context;

		public UsersAdapter(Context context) {
			super(context, R.layout.users_list_item);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			View row = convertView;
			UsersListHolder holder;

			if (row == null) {
				row = inflater.inflate(R.layout.users_list_item, parent, false);
				holder = new UsersListHolder();
				holder.name = ((TextView) row.findViewById(R.id.name));
				holder.username = ((TextView) row.findViewById(R.id.userName));
				holder.rank = ((TextView) row.findViewById(R.id.rank));
				row.setTag(holder);
			} else {
				holder = (UsersListHolder) row.getTag();
			}

			UserEntry item = getItem(position);

			holder.name.setText(item.getName());
			holder.username.setText(item.getUsername());
			holder.rank.setText(String.valueOf(item.getRank()));

			return row;
		}

		private class UsersListHolder {
			TextView name;
			TextView username;
			TextView rank;
		}
	}
}