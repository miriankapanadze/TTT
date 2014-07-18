package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
		mode.setTextOff(getResources().getString(R.string.activeMode));
		mode.setTextOn(getResources().getString(R.string.passiveMode));
		mode.setText("რეჟიმი");
		mode.setBackgroundColor(Color.WHITE);
		mode.setButtonDrawable(getResources().getDrawable(R.drawable.button_default_bg));

		btnSignIn.setOnClickListener(this);
		userService = ServicesFactory.getUserService();
		ServicesFactory.addLoginListener(this);

		userService.logout();
		handler = new Handler();
	}

	@Override
	protected void onResume() {
		userService.logout();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null) {
			return;
		}
		LoginRequest request = new LoginRequest();
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());
		request.setUserMode(mode.isFocused() ? UserMode.ACTIVE : UserMode.PASSIVE);

		userService.login(request);
	}

	@Override
	public void onLogin(final Status status, final List<UserEntry> users, final UserMode mode) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SignInActivity.this, status.getType().name() + "(" + status.getAdditionalInfo() + ")", Toast.LENGTH_SHORT).show();

				if (status.getType() == Status.Type.SUCCESS && mode == UserMode.ACTIVE) {
					Intent intent = new Intent(SignInActivity.this, UsersActivity.class);
					intent.putExtra("users", new Gson().toJson(users));
					startActivity(intent);
				}
			}
		});
	}
}