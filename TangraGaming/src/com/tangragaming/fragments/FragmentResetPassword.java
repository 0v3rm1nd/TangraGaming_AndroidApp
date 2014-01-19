package com.tangragaming.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.DatabaseHandler;
import tangralibrary.UserFunctions;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tangragaming.LoginActivity;
import com.tangragaming.R;

@SuppressLint("NewApi")
public class FragmentResetPassword extends Fragment {
	EditText inputOldPassword;
	EditText inputNewPassword;
	Button btnResetPassword;
	TextView resetPassErrorMsg;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";

	DatabaseHandler dbHandler;

	public FragmentResetPassword() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_reset_password, null);

		// Importing assets
		inputOldPassword = (EditText) view
				.findViewById(R.id.reset_password_oldEdit);
		inputNewPassword = (EditText) view
				.findViewById(R.id.reset_password_newEdit);
		btnResetPassword = (Button) view
				.findViewById(R.id.reset_password_submit);
		resetPassErrorMsg = (TextView) view.findViewById(R.id.passreset_error);
		
		dbHandler = new DatabaseHandler(getActivity());

		// Reset Password button Click Event
		btnResetPassword.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				
				Map<String, String> map = new HashMap<String, String>();
				map = dbHandler.getUserDetails();
				String email = map.get("email");

				String oldpassword = inputOldPassword.getText().toString();
				String newpassword = inputNewPassword.getText().toString();
				// instantiate asynctask and do the network call
				new ResetPasswordAsyncTask().execute(email, oldpassword,
						newpassword);
			}
		});

		return view;
	}

	private class ResetPasswordAsyncTask extends
			AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 3)
				return null;
			JSONObject json = userFunction.resetPassword(params[0], params[1],
					params[2]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					resetPassErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// pass successfully reset
						Toast.makeText(
								getActivity().getApplicationContext(),
								"Password reset successful! Please Login again!",
								Toast.LENGTH_LONG).show();
						// Clear all previous data in database
						UserFunctions userFunction = new UserFunctions();
						userFunction.logoutUser(getActivity()
								.getApplicationContext());

						getActivity().finish();
						// Launch Login Activity
						Intent i = new Intent(getActivity()
								.getApplicationContext(), LoginActivity.class);

						// Close all views before launching Login Activity
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
					} else {
						// show error message
						resetPassErrorMsg
								.setText("Incorrect old password/New password not complex enough!");
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
