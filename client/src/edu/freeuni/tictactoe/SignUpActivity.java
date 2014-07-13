package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.freeuni.tictactoe.model.RegistrationRequest;
import edu.freeuni.tictactoe.model.ServerStatus;
import edu.freeuni.tictactoe.server.ServicesFactory;
import edu.freeuni.tictactoe.server.UserService;

public class SignUpActivity extends Activity implements View.OnClickListener {

	private UserService userService;
	private EditText userName;
	private EditText name;
	private EditText password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_screen);

		Button btnSignUp = (Button) findViewById(R.id.submitSignUp);
		userName = (EditText) findViewById(R.id.upUserName);
		name = (EditText) findViewById(R.id.upName);
		password = (EditText) findViewById(R.id.upPass);

		btnSignUp.setOnClickListener(this);
		userService = ServicesFactory.getUserService();

	}

	@Override
	public void onClick(View v) {
		if (userName.getText() == null || password.getText() == null || name.getText() == null) {
			return;
		}

		RegistrationRequest request = new RegistrationRequest();

		request.setName(name.getText().toString());
		request.setUserName(userName.getText().toString());
		request.setPassword(password.getText().toString());

		ServerStatus status = userService.register(request);
//		Toast.makeText(this, status.getStatus().name() + "(" + status.getAdditionalInfo() + ")", Toast.LENGTH_LONG).show();
	}
}