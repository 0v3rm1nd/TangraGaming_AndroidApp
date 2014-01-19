package com.tangragaming;

import tangralibrary.UserFunctions;
import tangralibrary.DatabaseHandler;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	EditText inputEmail;
	EditText inputPassword;
	Button btnLogin;
	TextView loginErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_EMAIL = "email";
	private static String KEY_NICKNAME = "nickname";
	private static String KEY_NAME = "name";
	private static String KEY_SIGNATURE = "signature";
	private static String KEY_HOBBY = "hobby";
	private static String KEY_BIRTHDAY = "birthday";
	private static String KEY_GENDER = "gender";
	private static String KEY_DATECREATED = "datecreated";
	private static String KEY_DATEUPDATED = "dateupdated";
	private static String KEY_LASTLOGIN = "lastlogin";
	private static String KEY_LOCATION = "location";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Importing assets
		inputEmail = (EditText) findViewById(R.id.inputEmail);
		inputPassword = (EditText) findViewById(R.id.inputPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		TextView welcomeScreen = (TextView) findViewById(R.id.link_to_welcome);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				// instantiate asynctask and do the network call
				new LoginAsyncTask().execute(email, password);
			}
		});

		// Link to Register Activity
		registerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
			}
		});

		// Link to Welcome Activity
		welcomeScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						WelcomeActivity.class);
				startActivity(i);
			}
		});
	}

	private class LoginAsyncTask extends AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 2)
				return null;
			JSONObject json = userFunction.loginUser(params[0], params[1]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					loginErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// user successfully logged in
						// Store user details in SQLite Database
						DatabaseHandler db = new DatabaseHandler(
								getApplicationContext());
						JSONObject json_user = json.getJSONObject("user");

						// Clear all previous data in database
						UserFunctions userFunction = new UserFunctions();
						userFunction.logoutUser(getApplicationContext());
						db.addUser(json_user.getString(KEY_EMAIL),
								json_user.getString(KEY_NICKNAME),
								json_user.getString(KEY_NAME),
								json_user.getString(KEY_LOCATION),
								json_user.getString(KEY_GENDER),
								json_user.getString(KEY_BIRTHDAY),
								json_user.getString(KEY_HOBBY),
								json_user.getString(KEY_DATECREATED),
								json_user.getString(KEY_DATEUPDATED),
								json_user.getString(KEY_LASTLOGIN));
						// Launch Dashboard Screen
						Intent i = new Intent(getApplicationContext(),
								ReaderActivity.class);

						// Close all views before launching Reader Activity
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} else {
						// Error in login
						loginErrorMsg.setText("Incorrect username/password");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

}
