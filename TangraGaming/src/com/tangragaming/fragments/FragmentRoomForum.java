package com.tangragaming.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.UserFunctions;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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
public class FragmentRoomForum extends Fragment {
	TextView textView;
	Button btnToPostMessage;
	ListView lv;
	ArrayList<HashMap<String, String>> postlist = new ArrayList<HashMap<String, String>>();
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_GETPOSTS = "GetPosts";

	public FragmentRoomForum() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_forum, null);
		textView = (TextView) view.findViewById(R.id.roomforumtitile);
		btnToPostMessage = (Button) view.findViewById(R.id.btnCreatePost);
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.posts);
		lv.setVerticalFadingEdgeEnabled(true);
		postlist = new ArrayList<HashMap<String, String>>();
		// get the passed data
		String roomname = getArguments().getString("roomname");
		// Update the textview with the passed room name
		textView.setText(roomname);
		GetPosts getPosts = new GetPosts();
		getPosts.execute(roomname);

		// ToPostMessage button
		btnToPostMessage.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Bundle bundle = new Bundle();
				bundle.putString("roomname",textView.getText().toString());
				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentMakePost fragment = new FragmentMakePost();
				fragment.setArguments(bundle);

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
		return view;
	}

	// Async Call get all posts made in a specific room
	class GetPosts extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter + to string
			// use to get the friend list of a user
			String room = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getPosts(room);
			// Clear the current content of the list view
			postlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_GETPOSTS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String post = jsonobject.getString("post");
							String user = jsonobject.getString("user");
							//Make a single string out of the 2 retrieved
							String message = user +":"+post;

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();
							
							mapsubrooms.put("post", message);

							postlist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), postlist,
									R.layout.row_posts, new String[] {
											"post" },
									new int[] { R.id.message });

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
