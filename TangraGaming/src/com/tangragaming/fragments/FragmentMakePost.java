package com.tangragaming.fragments;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import tangralibrary.DatabaseHandler;
import tangralibrary.UserFunctions;
import android.annotation.SuppressLint;
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
import com.tangragaming.fragments.FragmentFindFriends.SearchFriends;

@SuppressLint("NewApi")
public class FragmentMakePost extends Fragment{
	EditText inputPostData;
	Button btnSubmitPost;
	DatabaseHandler dbHandler;
	// JSON Response node names
	private static String KEY_SUCCESS = "success";
    
    public FragmentMakePost() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_make_post, null);
        btnSubmitPost= (Button) view.findViewById(R.id.btnSendMessage);
		inputPostData = (EditText) view.findViewById(R.id.postcontent);
		dbHandler = new DatabaseHandler(getActivity());

		// Submit post button
		btnSubmitPost.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				
				Map<String, String> map = new HashMap<String, String>();
				map = dbHandler.getUserDetails();
				// get the user data we need
				String user = map.get("email");
				// get the passed data
				String room = getArguments().getString("roomname");
				//post data
				String post = inputPostData.getText().toString();
				SubmitPost submitPost = new SubmitPost();
				submitPost.execute(user,room,post);
			}
		});

        
        return view;
    }
	// Async Call to submit the post
	public class SubmitPost extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {
			// Bind paramteres
			String user = params[0];
			String room = params[1];
			String post = params[2];

			UserFunctions UserFunction = new UserFunctions();
			JSONObject json = UserFunction.makePosts(user, room, post);

			try {
				if (json != null && json.getString(KEY_SUCCESS) != null) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getActivity().getApplicationContext(),
									"Post Made Successfully!", Toast.LENGTH_LONG)
									.show();
							//I will have to load the old fragment here !
							

						}
					});

					Log.e("SUCCESS", "Post Successful!");					
				

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
