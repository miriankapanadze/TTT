package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.freeuni.tictactoe.server.RegistrationService;
import edu.freeuni.tictactoe.server.ServicesFactory;

public class SignUpActivity extends Activity implements View.OnClickListener{

	Button btnSignUp;
	RegistrationService registrationService;
	EditText mail;
	EditText fullName;
	EditText password;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_screen);

		btnSignUp = (Button) findViewById(R.id.submitSignUp);
		mail = (EditText) findViewById(R.id.upEmail);
		fullName = (EditText) findViewById(R.id.upUserName);
		password = (EditText) findViewById(R.id.upPass);

		btnSignUp.setOnClickListener(this);
		registrationService = ServicesFactory.getRegistrationService();

	}

	@Override
	public void onClick(View v) {
		if (mail.getText() == null || password.getText() == null || fullName.getText() == null) {
			return;
		}

		registrationService.registration(mail.getText().toString(), password.getText().toString(), fullName.getText().toString());
	}
}