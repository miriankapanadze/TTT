package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.gson.Gson;
import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.UserMode;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

import java.util.List;

public class SignInActivity extends Activity implements View.OnClickListener, LoginListener {

	private EditText userName;
	private EditText password;
	private ToggleButton mode;

	private UserService userService;
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);

		Button btnSignIn = (Button) findViewById(R.id.submitSignIn);
		userName = (EditText) findViewById(R.id.inUserName);
		password = (EditText) findViewById(R.id.inPass);
		mode = (ToggleButton) findViewById(R.id.mode);
		mode.setTextOff(getResources().getString(R.string.passiveMode));
		mode.setTextOn(getResources().getString(R.string.activeMode));
		mode.setChecked(true);

		btnSignIn.setOnClickListener(this);
		userService = ServicesFactory.getUserService();
		ListenersManager.addLoginListener(this);

		userService.logout();
		handler = new Handler();
	}

	@Override
	protected void onResume() {
		System.out.println("sign in activity onResume()");
		userService.logout();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null ||
				userName.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
			Toast.makeText(this, getResources().getString(R.string.emptyValidationMessage), Toast.LENGTH_SHORT).show();
			return;
		}
		LoginRequest request = new LoginRequest();
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());
		request.setUserMode(mode.isChecked() ? UserMode.ACTIVE : UserMode.PASSIVE);

		userService.login(request);
	}

	@Override
	public void onLogin(final Status status, final List<UserEntry> users, final UserMode mode) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (status.getType() == Status.Type.SUCCESS) {
					Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
					intent.putExtra("users", new Gson().toJson(users));
					intent.putExtra("mode", mode.name());
					startActivity(intent);
				} else {
					Toast.makeText(SignInActivity.this, status.getAdditionalInfo(), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}