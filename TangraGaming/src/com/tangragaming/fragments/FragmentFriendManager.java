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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tangragaming.R;
import com.tangragaming.fragments.FragmentFindFriends.InviteFriend;
import com.tangragaming.fragments.FragmentFindFriends.SearchFriends;

@SuppressLint("NewApi")
public class FragmentFriendManager extends Fragment {
	Button btnToFr;
	Button btnToFrReq;
	Button btnToFrReqStat;
	DatabaseHandler dbHandler;
	ListView lv;
	ArrayList<HashMap<String, String>> friendlist = new ArrayList<HashMap<String, String>>();
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_GETFRIENDS = "GetFriends";

	public FragmentFriendManager() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_manager, null);
		btnToFr = (Button) view.findViewById(R.id.btnFriends);
		btnToFrReq = (Button) view.findViewById(R.id.btnFriendReq);
		btnToFrReqStat = (Button) view.findViewById(R.id.btnFrReqStatus);
		dbHandler = new DatabaseHandler(getActivity());
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewFriendList);
		lv.setVerticalFadingEdgeEnabled(true);
		friendlist = new ArrayList<HashMap<String, String>>();

		// ToFr button
		btnToFr.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentFriendManager fragment = new FragmentFriendManager();

				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
					// will allow you to use the back button to go back to this
					// specific fragment
					ft.addToBackStack(null);
					ft.commit();

				}
			}
		});
		// ToFrReq button
		btnToFrReq.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentFriendRequests fragment = new FragmentFriendRequests();

				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
					// will allow you to use the back button to go back to this
					// specific fragment
					ft.addToBackStack(null);
					ft.commit();

				}
			}
		});
		// ToFrReqStat button
		btnToFrReqStat.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentFriendRequestStatuses fragment = new FragmentFriendRequestStatuses();

				if (fragment != null) {
					// Replace current fragment by this new one
					ft.replace(R.id.activity_main_content_fragment, fragment);
					// will allow you to use the back button to go back to this
					// specific fragment
					ft.addToBackStack(null);
					ft.commit();

				}
			}
		});
		Map<String, String> map = new HashMap<String, String>();
		map = dbHandler.getUserDetails();
		// get the user data we need
		String user = map.get("email");
		GetFriends getFriends = new GetFriends();
		getFriends.execute(user);
		return view;
	}

	// Async Call get the friends of a specific user
	class GetFriends extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter + to string
			// use to get the friend list of a user
			String user1 = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getFriends(user1);
			// Celear the current content of the list view
			friendlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_GETFRIENDS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String user = jsonobject.getString("user1");
							Log.e("UserEmail", user);

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();

							mapsubrooms.put("user1", user);

							friendlist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), friendlist, R.layout.row,
									new String[] { "user1" },
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
									d.setContentView(R.layout.custom_dialog_friends);
									d.setCancelable(true);
									Button defriend = (Button) d
											.findViewById(R.id.defriend);
									Button back = (Button) d
											.findViewById(R.id.backfriends);

									back.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {
											// close dialog
											d.dismiss();
										}

									});
									defriend.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {

											String user2 = friendlist.get(
													+position).get("user1");
											 //Run async call
											 RemoveFriend removeFriend = new RemoveFriend();
											 removeFriend.execute(user2);

											Toast.makeText(
													getActivity(),
													"Defriend Successful ",
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
	// Async Call to remove a friend record
		public class RemoveFriend extends AsyncTask<String, Void, JSONObject> {

			@Override
			protected JSONObject doInBackground(String... params) {
				// Bind the first passed parameter of RemoveFriend  + to string
				String user2 = params[0];

				Map<String, String> map = new HashMap<String, String>();
				map = dbHandler.getUserDetails();
				// get the user data we need
				String user1 = map.get("email");

				UserFunctions UserFunction = new UserFunctions();
				JSONObject json = UserFunction.removeFriend(user1, user2);

				try {
					if (json != null && json.getString(KEY_SUCCESS) != null) {

						Log.e(KEY_SUCCESS, "Defriend Successful!");		
						//Reload to refresh tge data
						GetFriends getFriends = new GetFriends();
						getFriends.execute(user1);

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
