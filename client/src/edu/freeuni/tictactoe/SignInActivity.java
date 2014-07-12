package edu.freeuni.tictactoe;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import edu.freeuni.tictactoe.server.LoginService;
import edu.freeuni.tictactoe.server.ServicesFactory;

public class SignInActivity extends Activity implements View.OnClickListener {

	Button btnSignIn;
	EditText mail;
	EditText password;

	LoginService loginService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);

		btnSignIn = (Button) findViewById(R.id.submitSignIn);
		mail = (EditText) findViewById(R.id.inMail);
		password = (EditText) findViewById(R.id.inPass);

		btnSignIn.setOnClickListener(this);
		loginService = ServicesFactory.getLoginService();

	}

	@Override
	public void onClick(View v) {
		if (mail.getText() == null || password.getText() == null) {
			return;
		}
		loginService.login(mail.getText().toString(), mail.getText().toString());
	}
}