package com.tangragaming.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.DatabaseHandler;
import tangralibrary.UserFunctions;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tangragaming.R;
import com.tangragaming.fragments.FragmentRoomList.JoinRoom;
import com.tangragaming.fragments.FragmentRoomList.SearchRooms;

@SuppressLint("NewApi")
public class FragmentFindFriends extends Fragment {
	Button btnSearchUser;
	ListView lv;
	EditText inputUserEmail;
	DatabaseHandler dbHandler;
	ArrayList<HashMap<String, String>> userlist = new ArrayList<HashMap<String, String>>();

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_SEARCHUSERS = "SearchUsers";

	public FragmentFindFriends() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find_friends, null);
		btnSearchUser = (Button) view.findViewById(R.id.btnSearchFriend);
		inputUserEmail = (EditText) view.findViewById(R.id.searchFriendEdit);
		dbHandler = new DatabaseHandler(getActivity());
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewSearchFriends);
		lv.setVerticalFadingEdgeEnabled(true);
		userlist = new ArrayList<HashMap<String, String>>();

		// Search For Friends
		btnSearchUser.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				String useremail = inputUserEmail.getText().toString();

				SearchFriends searchFriends = new SearchFriends();
				searchFriends.execute(useremail);
			}
		});

		return view;
	}

	// Async Call get the user that matched the user input
	class SearchFriends extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of searchUsers to a string
			// variable called usernickname wjich we
			// use to get the list of users that match the user input
			String useremail = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.searchUser(useremail);
			// Celear the current content of the list view
			userlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_SEARCHUSERS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String email = jsonobject.getString("email");
							Log.e("UserEmail", email);

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();

							mapsubrooms.put("email", email);

							userlist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), userlist, R.layout.row,
									new String[] { "email" },
									new int[] { R.id.name });

							// Updating the view must be done on the main UI
							// thread
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									lv.setAdapter(adapter);
								}
							});
							registerForContextMenu(lv);
							lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, final int position, long id) {
									final Dialog d = new Dialog(getActivity());

									d.requestWindowFeature(Window.FEATURE_NO_TITLE);
									d.setContentView(R.layout.custom_dialog_invite);
									d.setCancelable(true);
									Button invite = (Button) d
											.findViewById(R.id.invite);
									Button back = (Button) d
											.findViewById(R.id.back);

									back.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {
											// close dialog
											d.dismiss();
										}

									});
									invite.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {

											String email = userlist.get(
													+position).get("email");
											// Run async call
											InviteFriend inviteFriend = new InviteFriend();
											inviteFriend.execute(email);

											Toast.makeText(
													getActivity(),
													"You sent a firiend request to "
															+ userlist
																	.get(+position)
																	.get("email"),
													Toast.LENGTH_SHORT).show();

											d.dismiss();
										}

									});

									d.show();

								}
							});

						}
					} else {

						Log.e("Error JsonArray Empty", "Error JsonArray Empty");
					}

				} else {

					Log.e("KEY_ERROR", "The returned json was null!");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// Async Call to join a specific room
	public class InviteFriend extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of InviteFriend to a string
			String to = params[0];

			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			// get the user data we need
			String from = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.inviteFriend(from, to);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {

					Log.e("SUCCESS", "Invite Successful!");

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
