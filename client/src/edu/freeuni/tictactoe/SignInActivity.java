package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import edu.freeuni.tictactoe.listeners.LoginListener;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.model.UserEntry;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

import java.util.List;

public class SignInActivity extends Activity implements View.OnClickListener, LoginListener {

	private EditText userName;
	private EditText password;

	private UserService userService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);

		Button btnSignIn = (Button) findViewById(R.id.submitSignIn);
		userName = (EditText) findViewById(R.id.inUserName);
		password = (EditText) findViewById(R.id.inPass);

		btnSignIn.setOnClickListener(this);
		userService = ServicesFactory.getUserService();
		ServicesFactory.addLoginListener(this);
	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null) {
			return;
		}
		LoginRequest request = new LoginRequest();
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());

		userService.login(request);
	}

	@Override
	public void onLogin(final ServerStatus status, final List<UserEntry> users) {
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(SignInActivity.this, status.getStatus().name() + "(" + status.getAdditionalInfo() + ")", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(SignInActivity.this, UsersActivity.class);
				intent.putExtra("users", new Gson().toJson(users));
				startActivity(intent);
			}
		});
	}
}