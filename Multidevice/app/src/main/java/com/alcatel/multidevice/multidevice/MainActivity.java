package com.alcatel.multidevice.multidevice;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Button loginButton;
	Button infosButton;
	TextView text;
	EditText serv, login, passwd;
	static String ser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		bindView();
	}

	private void bindView() {
		loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(loginButtonClickListener);
		//infosButton = (Button) findViewById(R.id.button1);
		//infosButton.setOnClickListener(infosButtonClickListener);
		text = (TextView) findViewById(R.id.text);
		text.setText("");
		serv = (EditText) findViewById(R.id._txtboxServer);
		login = (EditText) findViewById(R.id._txtBoxLogin);
		passwd = (EditText) findViewById(R.id._txtboxPassword);
        serv.setText("http://ot2.sqanet.fr", TextView.BufferType.EDITABLE );
        login.setText("cnam1", TextView.BufferType.EDITABLE);
        passwd.setText("Alcatel1", TextView.BufferType.EDITABLE);
	}

	private final OnClickListener loginButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			// Start Login
			DemoApplication app = (DemoApplication) getApplication();
			RESTExecutor executor = app.getRestExecutor();
			String log, pwd;
			log=login.getText().toString();
			pwd=passwd.getText().toString();
			ser=serv.getText().toString();
			executor.setCredential(log, pwd);
			//executor.setCredential("cnam1", "Alcatel1");

			// server response
			RESTResponseHandler responseHandler = new RESTResponseHandler() {

				
				@Override
				public void onResponse(String reponse) {
					if (reponse.isEmpty()) {
						//text.setText("Login failed");
					} else {
						text.setText("Login Success");
						/*Intent intent = new Intent(MainActivity.this, ScannerDisplayActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		                startActivity(intent);*/
					}
				}
			};

			// Ask for login
			executor.authenticate(responseHandler);
			text.setText("Login in progress");
			
			// server response
						RESTResponseHandler responseHandler2 = new RESTResponseHandler() {

							
							@Override
							public void onResponse(String reponse) {
								if (reponse.isEmpty()) {
									text.setText("Login" +
                                            " failed");
								} else {
									text.setText("Session Oppened");
									Intent intent = new Intent(MainActivity.this, ScannerDisplayActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
									startActivity(intent);
									
								}
							}
						};

						// Ask for login
						executor.openSession(responseHandler2);
						text.setText("Login in progress");
			
			
		}

	};
	
						
	
	/*
	private final OnClickListener infosButtonClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			
			// Start Login
			DemoApplication app = (DemoApplication) getApplication();
			RESTExecutor executor = app.getRestExecutor();
			executor.setCredential("cnam1", "Alcatel1");

			// server response
			RESTResponseHandler responseHandler = new RESTResponseHandler() {

				
				@Override
				public void onResponse(String reponse) {
					if (reponse.isEmpty()) {
						text.setText("failed");
					} else {
						text.setText(reponse);
					}
				}
			};

			// Ask for login
			executor.infosUser(responseHandler);
			text.setText("Work in progress");
			
			
		}

	};*/
	
}
