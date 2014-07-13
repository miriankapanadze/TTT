package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.freeuni.tictactoe.model.LoginRequest;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

public class SignInActivity extends Activity implements View.OnClickListener {

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

	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null) {
			return;
		}
		LoginRequest request = new LoginRequest();
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());

		ServerStatus status = userService.login(request);
		Toast.makeText(this, status.getStatus().name() + "(" + status.getAdditionalInfo() + ")", Toast.LENGTH_LONG).show();
	}
}