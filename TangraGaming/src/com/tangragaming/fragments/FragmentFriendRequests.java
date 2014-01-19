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
import com.tangragaming.fragments.FragmentFriendManager.GetFriends;
import com.tangragaming.fragments.FragmentFriendManager.RemoveFriend;

@SuppressLint("NewApi")
public class FragmentFriendRequests extends Fragment {
	Button btnToFr;
	Button btnToFrReq;
	Button btnToFrReqStat;
	DatabaseHandler dbHandler;
	ListView lv;
	ArrayList<HashMap<String, String>> reqlist = new ArrayList<HashMap<String, String>>();
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_GETFRIENDREQUESTS = "GetFriendRequests";

	public FragmentFriendRequests() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_friend_requests, null);
		btnToFr = (Button) view.findViewById(R.id.btnFriends);
		btnToFrReq = (Button) view.findViewById(R.id.btnFriendReq);
		btnToFrReqStat = (Button) view.findViewById(R.id.btnFrReqStatus);
		dbHandler = new DatabaseHandler(getActivity());
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewFriendRequests);
		lv.setVerticalFadingEdgeEnabled(true);
		reqlist = new ArrayList<HashMap<String, String>>();

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
		String to = map.get("email");
		GetFriendRequests getFriendRequests = new GetFriendRequests();
		getFriendRequests.execute(to);

		return view;
	}

	// Async Call get the friend requests
	class GetFriendRequests extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter + to string
			// use to get the friend list of a user
			String to = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getFriendRequests(to);
			// Celear the current content of the list view
			reqlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_GETFRIENDREQUESTS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String from = jsonobject.getString("from");
							Log.e("From", from);

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();

							mapsubrooms.put("from", from);

							reqlist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), reqlist, R.layout.row_friend_statuses,
									new String[] { "from" },
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
									d.setContentView(R.layout.custom_dialog_frrequests);
									d.setCancelable(true);
									Button accept = (Button) d
											.findViewById(R.id.accept);
									Button decline = (Button) d
											.findViewById(R.id.decline);

									accept.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {
											String from = reqlist
													.get(+position).get("from");
											// Run async call
											AcceptFriendRequest acceptFriendRequest = new AcceptFriendRequest();
											acceptFriendRequest.execute(from);

											Toast.makeText(
													getActivity(),
													"Friend Request Accepted Successfully!",
													Toast.LENGTH_SHORT).show();
											d.dismiss();
										}

									});
									decline.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {

											String from = reqlist
													.get(+position).get("from");
											// Run async call
											DeclineFriendRequest declineFriendRequest = new DeclineFriendRequest();
											declineFriendRequest.execute(from);

											Toast.makeText(
													getActivity(),
													"Friend Requst Declined Successfully!",
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

	// Async Call to accept friend request
	public class AcceptFriendRequest extends
			AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of RemoveFriend + to string
			String from = params[0];

			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			// get the user data we need
			String to = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.acceptFriendRequest(from, to);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {

					Log.e(KEY_SUCCESS, "Friend Request Accepted Successfully!");
					// Reload to refresh the data
					GetFriendRequests getFriendRequests = new GetFriendRequests();
					getFriendRequests.execute(to);

				} else {

					Log.e("KEY_ERROR", "The returned json was null!");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	// Async Call to decline friend request
	public class DeclineFriendRequest extends
			AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of RemoveFriend + to string
			String from = params[0];

			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			// get the user data we need
			String to = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.declineFriendRequest(from, to);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {

					Log.e(KEY_SUCCESS, "Friend Request Declined Successfully!");
					// Reload to refresh the data
					GetFriendRequests getFriendRequests = new GetFriendRequests();
					getFriendRequests.execute(to);

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