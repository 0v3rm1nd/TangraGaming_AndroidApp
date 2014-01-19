package com.tangragaming.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.DatabaseHandler;
import tangralibrary.UserFunctions;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tangragaming.LoginActivity;
import com.tangragaming.R;

@SuppressLint("NewApi")
public class FragmentRoomCreate extends Fragment {
	EditText inputRoomName;
	Spinner spinnerMainRoom;
	Button btnCreateRoom;
	TextView createRoomErrorMsg;
	Cursor cursor;

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";

	DatabaseHandler dbHandler;

	public FragmentRoomCreate() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_room, null);

		// Importing assets
		inputRoomName = (EditText) view.findViewById(R.id.room_nameEdit);
		spinnerMainRoom = (Spinner) view
				.findViewById(R.id.main_room_nameSpinner);
		btnCreateRoom = (Button) view.findViewById(R.id.create_room_submit);
		createRoomErrorMsg = (TextView) view
				.findViewById(R.id.create_room_error);

		dbHandler = new DatabaseHandler(getActivity());
		
		// Loading spinner data from database
		loadSpinnerData();

		// Reset Password button Click Event
		btnCreateRoom.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				Map<String, String> map = new HashMap<String, String>();
				map = dbHandler.getUserDetails();
				String owner = map.get("email");

				String name = inputRoomName.getText().toString();
				// instantiate asynctask and do the network call
				String parentroom = spinnerMainRoom.getSelectedItem().toString();
				new CreateRoomAsyncTask().execute(name, owner, parentroom);
			}
		});

		return view;
	}

	/**
	 * Function to load the spinner data from SQLite database
	 * */
	private void loadSpinnerData() {
		// Get the returned cursor from the slite db
		cursor = dbHandler.getMainRoomDetails();
		List<String> mainroomlist = new ArrayList<String>();

		// Loop through the cursor data and assign its values to a string
		// variable
		if (cursor.moveToFirst()) {

			do {
				mainroomlist
						.add(cursor.getString(cursor.getColumnIndex("name")));

			} while (cursor.moveToNext());

		} else {
			Log.e("Error Database", "Empty mainroom table");
		}

		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item,
				mainroomlist);

		// Drop down layout style - list view with radio button
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// attaching data adapter to spinner
		spinnerMainRoom.setAdapter(dataAdapter);
	}

	private class CreateRoomAsyncTask extends
			AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length != 3)
				return null;
			JSONObject json = userFunction.createRoom(params[0], params[1],
					params[2]);
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					createRoomErrorMsg.setText("");
					String res = json.getString(KEY_SUCCESS);
					if (Integer.parseInt(res) == 1) {
						// room created successfully
						Toast.makeText(getActivity().getApplicationContext(),
								"Room Created Successfully!", Toast.LENGTH_LONG)
								.show();

						FragmentManager fm = getActivity()
								.getSupportFragmentManager();
						FragmentTransaction ft = fm.beginTransaction();
						FragmentForum fragment = new FragmentForum();

						// Replace current fragment by this new one
						ft.replace(R.id.activity_main_content_fragment,
								fragment);
						// will allow you to use the back button to go back
						// to this specific fragment
						ft.addToBackStack(null);
						ft.commit();

					} else {
						// show error message
						createRoomErrorMsg
								.setText("The Room Name must be at least 2 characters");
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
