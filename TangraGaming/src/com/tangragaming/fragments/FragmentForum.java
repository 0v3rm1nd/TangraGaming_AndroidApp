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
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tangragaming.R;

@SuppressLint("NewApi")
public class FragmentForum extends Fragment {
	DatabaseHandler dbHandler;
	Cursor cursor;
	LinearLayout ll;
	ListView lv;
	TextView yourRooms;
	Button btnCreateRoom;
	Button btnSearchRoom;
	ArrayList<HashMap<String, String>> roomlist = new ArrayList<HashMap<String, String>>();

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ROOMMEMBERSHIP = "RoomMembership";
	private static String KEY_SUBROOMS = "SubRooms";

	public FragmentForum() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forum, null);
		dbHandler = new DatabaseHandler(getActivity());
		ll = (LinearLayout) view.findViewById(R.id.linearMainRooms);
		yourRooms = (TextView) view.findViewById(R.id.joinedroomsforum);
		btnCreateRoom = (Button) view.findViewById(R.id.btnCreateRoom);
		btnSearchRoom = (Button) view.findViewById(R.id.btnSearchRooms);

		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewJoinedRooms);
		lv.setVerticalFadingEdgeEnabled(true);
		roomlist = new ArrayList<HashMap<String, String>>();

		// will populate the action bar with the main rooms data received from
		// the API call
		PopulateMainRoomView();

		new GetRoomsJoined().execute();

		// Create Room Button
		btnCreateRoom.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentRoomCreate fragment = new FragmentRoomCreate();

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

		// Search Room Button
		btnSearchRoom.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				FragmentRoomList fragment = new FragmentRoomList();

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

	private void PopulateMainRoomView() {
		// Get the returned cursor from the slite db
		cursor = dbHandler.getMainRoomDetails();

		// Loop through the cursor data and assign its values to a string
		// variable
		if (cursor.moveToFirst()) {

			do {
				final String MainRoomName;
				MainRoomName = cursor.getString(cursor.getColumnIndex("name"));
				// Dynamically adding buttons to our linear layout in the scroll
				// view
				Button btnMainRoom = new Button(getActivity());
				btnMainRoom.setText(MainRoomName);
				btnMainRoom.setBackgroundResource(R.layout.rss_news_button);
				btnMainRoom.setTextColor(Color.parseColor("#eaeaea"));
				btnMainRoom.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				ll.addView(btnMainRoom);
				btnMainRoom.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						yourRooms.setText(MainRoomName);
						// Call the async task to get the sub rooms and pass the
						// parent room name
						// so that we can get them from the API
						GetSubRooms getSubRooms = new GetSubRooms();
						getSubRooms.execute(MainRoomName);

					}
				});

			} while (cursor.moveToNext());

		} else {
			new GetMainRoomDataAsyncTask().execute();
		}
	}

	// Async task to get Main Room Data
	private class GetMainRoomDataAsyncTask extends
			AsyncTask<String, Void, JSONObject> {

		protected JSONObject doInBackground(String... params) {
			UserFunctions userFunction = new UserFunctions();
			if (params.length > 0)
				return null;
			JSONObject json = userFunction.getMainRoomData();
			return json;
		}

		protected void onPostExecute(JSONObject json) {
			try {
				if (json != null) {

					DatabaseHandler db = new DatabaseHandler(getActivity()
							.getApplicationContext());
					// Get json array and loop through it adding its values to
					// the sqlite db
					JSONArray jsonArrayMainRooms = json
							.getJSONArray("MainRooms");

					if (jsonArrayMainRooms != null) {
						for (int i = 0; i < jsonArrayMainRooms.length(); i++) {

							JSONObject jsonobject = jsonArrayMainRooms
									.getJSONObject(i);
							String name = jsonobject.getString("name");
							String datecreated = jsonobject
									.getString("datecreated");

							db.addMainRooms(name, datecreated);

						}
					} else {

						Log.e("Error JsonArray Empty", "Error JsonArray Empty");
					}

					// Once we get the data run again through the populate
					// method!
					PopulateMainRoomView();

				} else {
					// There is a problem with the app, the json data is empty
					Log.e("Fatal Error",
							"There was a problem with the application!");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// Async Call get the total rooms joined by a user
	// joined
	class GetRoomsJoined extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Get the main info from the sqlite db
			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			String email = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getRoomMembership(email);

			try {
				if (json != null) {
					JSONArray jsonArrayMainRooms = json
							.getJSONArray(KEY_ROOMMEMBERSHIP);

					if (jsonArrayMainRooms != null) {
						for (int i = 0; i < jsonArrayMainRooms.length(); i++) {

							JSONObject jsonobject = jsonArrayMainRooms
									.getJSONObject(i);
							String name = jsonobject.getString("name");
							Log.e("RoomName", name);

							HashMap<String, String> maprooms = new HashMap<String, String>();

							maprooms.put("name", name);

							roomlist.add(maprooms);
							// lv=(ListView)findViewById(R.id.list);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), roomlist, R.layout.row_room,
									new String[] { "name" },
									new int[] { R.id.nameRoom });

							// Updating the view must be done on the main UI
							// thread
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									lv.setAdapter(adapter);
								}
							});

							lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// Pass the roomname that the user pressed
									// to the new fragment
									Bundle bundle = new Bundle();
									bundle.putString("roomname",
											roomlist.get(+position).get("name"));
									FragmentManager fm = getActivity()
											.getSupportFragmentManager();
									FragmentTransaction ft = fm
											.beginTransaction();
									FragmentRoomForum fragment = new FragmentRoomForum();
									fragment.setArguments(bundle);
									if (fragment != null) {
										// Replace current fragment by this new
										// one
										ft.replace(
												R.id.activity_main_content_fragment,
												fragment);
										// will allow you to use the back button
										// to go back to this
										// specific fragment
										ft.addToBackStack(null);
										ft.commit();

									}

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

	// Async Call get the subrooms for each main room
	// joined
	class GetSubRooms extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of GetSubRooms to a string
			// variable called parentroom wich we
			// use to get the subrooms
			String parentroom = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.getSubRooms(parentroom);
			// Celear the current content of the list view
			roomlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_SUBROOMS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String name = jsonobject.getString("name");
							Log.e("SubRoomName", name);

							HashMap<String, String> mapsubrooms = new HashMap<String, String>();

							mapsubrooms.put("name", name);

							roomlist.add(mapsubrooms);
							final ListAdapter adapter = new SimpleAdapter(
									getActivity(), roomlist, R.layout.row_room,
									new String[] { "name" },
									new int[] { R.id.nameRoom });

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
									d.setContentView(R.layout.custom_dialog_rooms);
									d.setCancelable(true);
									Button join = (Button) d
											.findViewById(R.id.join);
									Button back = (Button) d
											.findViewById(R.id.back);

									back.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {
											// close dialog
											d.dismiss();
										}

									});
									join.setOnClickListener(new View.OnClickListener() {

										public void onClick(View v) {

											String room = roomlist.get(
													+position).get("name");
											// Run async call
											JoinRoom joinRoom = new JoinRoom();
											joinRoom.execute(room);

											Toast.makeText(
													getActivity(),
													"You joined the room "
															+ roomlist
																	.get(+position)
																	.get("name"),
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
	public class JoinRoom extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of JoinRoom to a string
			String room = params[0];

			Map<String, String> map = new HashMap<String, String>();
			map = dbHandler.getUserDetails();
			// get the user data we need
			String user = map.get("email");

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.joinRoom(user, room);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {

					Log.e("SUCCESS", "Room Joined Successfully");

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
