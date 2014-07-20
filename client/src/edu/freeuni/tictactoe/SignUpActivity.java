package edu.freeuni.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.freeuni.tictactoe.listeners.RegisterListener;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.Status;
import edu.freeuni.tictactoe.server.ListenersManager;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

public class SignUpActivity extends Activity implements View.OnClickListener, RegisterListener {

	private UserService userService;
	private EditText userName;
	private EditText name;
	private EditText password;
	private Handler handler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_screen);

		Button btnSignUp = (Button) findViewById(R.id.submitSignUp);
		userName = (EditText) findViewById(R.id.upUserName);
		name = (EditText) findViewById(R.id.upName);
		password = (EditText) findViewById(R.id.upPass);

		btnSignUp.setOnClickListener(this);
		userService = ServicesFactory.getUserService();
		ListenersManager.addRegisterListener(this);
		handler = new Handler();
	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null || name.getText() == null ||
				userName.getText().toString().isEmpty() || password.getText().toString().isEmpty() || name.getText().toString().isEmpty()) {
			return;
		}

		RegistrationRequest request = new RegistrationRequest();

		request.setName(name.getText().toString());
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());

		userService.register(request);
	}

	@Override
	public void onRegister(final Status status) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				if (status.getType() == Status.Type.SUCCESS) {
					Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(SignUpActivity.this, status.getType().name() + "(" + status.getAdditionalInfo() + ")", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}