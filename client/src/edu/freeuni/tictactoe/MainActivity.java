package edu.freeuni.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

	Button btnSignIn;
	Button btnSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btnSignIn = (Button) findViewById(R.id.btnSignIn);
		btnSignUp = (Button) findViewById(R.id.btnSignUp);

		btnSignIn.setOnClickListener(this);
		btnSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.btnSignIn:
				intent = new Intent(this, SignInActivity.class);
				break;
			case R.id.btnSignUp:
				intent = new Intent(this, SignUpActivity.class);
				break;
		}
		startActivity(intent);
	}
}