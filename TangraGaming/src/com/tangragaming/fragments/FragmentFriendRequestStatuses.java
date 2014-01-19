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
import com.tangragaming.fragments.FragmentFriendRequests.AcceptFriendRequest;
import com.tangragaming.fragments.FragmentFriendRequests.DeclineFriendRequest;
import com.tangragaming.fragments.FragmentFriendRequests.GetFriendRequests;

@SuppressLint("NewApi")
public class FragmentFriendRequestStatuses extends Fragment {
	Button btnToFr;
	Button btnToFrReq;
	Button btnToFrReqStat;
	DatabaseHandler dbHandler;
	ListView lv;
	ArrayList<HashMap<String, String>> statuslist = new ArrayList<HashMap<String, String>>();
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_GETFRIENDREQUESTSTATUSES = "GetFriendRequestStatuses";

	public FragmentFriendRequestStatuses() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_request_status,
				null);
		btnToFr = (Button) view.findViewById(R.id.btnFriends);
		btnToFrReq = (Button) view.findViewById(R.id.btnFriendReq);
		btnToFrReqStat = (Button) view.findViewById(R.id.btnFrReqStatus);
		dbHandler = new DatabaseHandler(getActivity());
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewFriendRequestStatus);
		lv.setVerticalFadingEdgeEnabled(true);
		statuslist = new ArrayList<HashMap<String, String>>();
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
		String from = map.get("email");
		GetFriendRequestStatuses getFriendRequestStatuses = new GetFriendRequestStatuses();
		getFriendRequestStatuses.execute(from);

		return view;
	}

	// Async Call get your the friend request statuses 
	class GetFriendRequestStatuses extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter + to string
			// use to get the friend list of a user
			String from = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getFriendRequestStatuses(from);
			// Celear the current content of the list view
			statuslist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_GETFRIENDREQUESTSTATUSES);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String to = jsonobject.getString("to");
							String status = jsonobject.getString("status");
							Log.e("To", to);
							Log.e("Status", status);

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();

							mapsubrooms.put("to", to);
							mapsubrooms.put("status", status);

							statuslist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), statuslist, R.layout.row_friend_statuses,
									new String[] { "to", "status" },
									new int[] { R.id.name, R.id.status });

							// Updating the view must be done on the main UI
							// thread
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									lv.setAdapter(adapter);
								}
							});
							registerForContextMenu(lv);
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

}