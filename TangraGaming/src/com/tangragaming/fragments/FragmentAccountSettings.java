package com.tangragaming.fragments;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.tangragaming.R;
import com.tangragaming.ReaderActivity;

@SuppressLint("NewApi")
public class FragmentAccountSettings extends Fragment {
	Button btnResetPassword;
	Button btnUpdateInfo;
	EditText inputName;
	EditText inputLocation;
	EditText inputGender;
	EditText inputBirthday;
	EditText inputHobby;
	TextView viewEmail;
	TextView viewNickname;
	TextView viewTotalPosts;
	TextView viewRoomsJoined;
	TextView viewTitle;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_TOTALPOSTS = "getTotalPosts";
	private static String KEY_TOTALROOMSJOINED = "getTotalRoomsJoined";
	private static String KEY_USERSTATISTICALDATA = "getUserStatisticalData";

	DatabaseHandler dbHandler;

	public FragmentAccountSettings() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_account_settings, null);

		// Importing assets
		inputName = (EditText) view
				.findViewById(R.id.account_settings_nameEdit);
		inputLocation = (EditText) view
				.findViewById(R.id.account_settings_locationEdit);
		inputGender = (EditText) view
				.findViewById(R.id.account_settings_genderEdit);
		inputBirthday = (EditText) view
				.findViewById(R.id.account_settings_birthdayEdit);
		inputHobby = (EditText) view
				.findViewById(R.id.account_settings_hobbyEdit);
		btnResetPassword = (Button) view
				.findViewById(R.id.account_settings_resetpassword);
		btnUpdateInfo = (Button) view
				.findViewById(R.id.account_settings_updateinfo);
		viewEmail = (TextView) view.findViewById(R.id.account_settings_email);
		viewNickname = (TextView) view
				.findViewById(R.id.account_settings_nickname);
		viewTotalPosts = (TextView) view
				.findViewById(R.id.account_settings_totalposts);
		viewRoomsJoined = (TextView) view
				.findViewById(R.id.account_settings_roomjoined);
		viewTitle = (TextView) view.findViewById(R.id.account_settings_title);

		dbHandler = new DatabaseHandler(getActivity());

		// check db and update the email, nickname, total posts and rooms joined
		updateMainInfo();

		// Try and get the total posts from URL
		new GetUserStisticalData().execute();

		// Update Info button
		btnUpdateInfo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Map<String, String> map = new HashMap<String, String>();
				map = dbHandler.getUserDetails();
				String email = map.get("email");
				String name = inputName.getText().toString();
				String location = inputLocation.getText().toString();
				String gender = inputGender.getText().toString();
				String birthday = inputBirthday.getText().toString();
				String hobby = inputHobby.getText().toString();
				// instantiate asynctask and do the network call
				new UpdateUserAsyncTask().execute(email, name, location,
						gender, birthday, hobby);
			}
		});

		// Reset Password button
		btnResetPassword.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentResetPassword fragment = new FragmentResetPassword();

				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
					//will allow you to use the back button to go back to this specific fragment
					ft.addToBackStack(null);
					ft.commit();

				}
			}
		});

		return view;
	}

	private void updateMainInfo() {

		// Get the main info from the sqlite db and update the views
		Map<String, String> map = new HashMap<String, String>();
		map = dbHandler.getUserDetails();

		if (map.isEmpty()) {

			// There is a problem with the app, main data can not be empty
			Log.e("Fatal Error",
					"The database must contain the main user data!");

		} else {
			// get the value of the hash map and display it in the text view
			viewEmail.setText("Email: " + map.get("email"));
			viewNickname.setText("Nickname: " + map.get("nickname"));
			viewTitle.setText(map.get("nickname"));
			inputName.setText(map.get("name"));
			inputLocation.setText(map.get("location"));
			inputGender.setText(map.get("gender"));
			inputBirthday.setText(map.get("birthday"));
			inputHobby.setText(map.get("hobby"));

		}

	}

	// Async task to update user data
	private class UpdateUserAsyncTask extends
			AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 6)
				return null;
			JSONObject json = userFunction.updateUserData(params[0], params[1],
					params[2], params[3], params[4], params[5]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null) {

					// user successfully updated
					// Update user details in SQLite Database
					DatabaseHandler db = new DatabaseHandler(getActivity()
							.getApplicationContext());
					JSONObject json_updateuser = json.getJSONObject("user");

					db.updateUser(json_updateuser.getString("email"),
							json_updateuser.getString("name"),
							json_updateuser.getString("location"),
							json_updateuser.getString("gender"),
							json_updateuser.getString("birthday"),
							json_updateuser.getString("hobby"),
							json_updateuser.getString("dateupdated"));

					// display a toast message that the update was completed
					// successfully
					Toast.makeText(getActivity().getApplicationContext(),
							"The user data was updated successfully",
							Toast.LENGTH_LONG).show();
					// updateMainInfo();

				} else {
					// There is a problem with the app, main data can not be
					// empty
					Log.e("Fatal Error", "There was a problem!");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// Async Call to get the total posts of a user in all rooms + count rooms
	// joined
	class GetUserStisticalData extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Get the main info from the sqlite db
			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			String email = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getUserStatisticalData(email);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					final JSONObject json_getUserStatistics = json
							.getJSONObject("getUserStatisticalData");
					// Log the total posts
					Log.e(KEY_USERSTATISTICALDATA,
							"The total number of made posts is : "
									+ json_getUserStatistics
											.getString(KEY_TOTALPOSTS)
									+ " and the total number of joined rooms is: "
									+ json_getUserStatistics
											.getString(KEY_TOTALROOMSJOINED));

					// Updating the view must be done on the main UI thread
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							try {
								viewTotalPosts.setText("Total Posts: "
										+ json_getUserStatistics
												.getString(KEY_TOTALPOSTS));

								viewRoomsJoined.setText("Rooms Joined: "
										+ json_getUserStatistics
												.getString(KEY_TOTALROOMSJOINED));
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					});

				} else {

					Log.e("KEY_ERROR", "The returned json was null!");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

}
