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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.tangragaming.R;
import com.tangragaming.fragments.FragmentForum.GetSubRooms;

@SuppressLint("NewApi")
public class FragmentRoomList extends Fragment {
	Button btnToCreateRoom;
	Button btnSearchRoom;
	ListView lv;
	EditText inputRoomName;
	DatabaseHandler dbHandler;
	ArrayList<HashMap<String, String>> roomlist = new ArrayList<HashMap<String, String>>();

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_SEARCHROOMS = "SearchRooms";

	public FragmentRoomList() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_room_list, null);
		btnToCreateRoom = (Button) view.findViewById(R.id.btnToCreateRoom);
		btnSearchRoom = (Button) view.findViewById(R.id.btnSearchRoom);
		inputRoomName = (EditText) view.findViewById(R.id.searchRoomEdit);
		dbHandler = new DatabaseHandler(getActivity());
		// Initialize the variables:
		lv = (ListView) view.findViewById(R.id.listViewSearchRooms);
		lv.setVerticalFadingEdgeEnabled(true);
		roomlist = new ArrayList<HashMap<String, String>>();

		// Search Room
		btnSearchRoom.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				String room = inputRoomName.getText().toString();

				SearchRooms searchRooms = new SearchRooms();
				searchRooms.execute(room);
			}
		});

		// To Create Room
		btnToCreateRoom.setOnClickListener(new View.OnClickListener() {

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

		return view;
	}

	// Async Call get the rooms that matched the user input
	// joined
	class SearchRooms extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind the first passed parameter of searchRooms to a string
			// variable called roomname wich we
			// use to get the list of rooms that match the user input
			String roomname = params[0];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.searchRoom(roomname);
			// Celear the current content of the list view
			roomlist.clear();

			try {
				if (json != null) {
					JSONArray jsonArraySubRooms = json
							.getJSONArray(KEY_SEARCHROOMS);

					if (jsonArraySubRooms != null) {
						for (int i = 0; i < jsonArraySubRooms.length(); i++) {

							JSONObject jsonobject = jsonArraySubRooms
									.getJSONObject(i);
							String name = jsonobject.getString("name");
							Log.e("RoomNames", name);

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
