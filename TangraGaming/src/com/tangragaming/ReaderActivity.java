package com.tangragaming;

import java.util.List;

import com.tangragaming.fragments.FragmentAccountSettings;
import com.tangragaming.fragments.FragmentFindFriends;
import com.tangragaming.fragments.FragmentForum;
import com.tangragaming.fragments.FragmentFriendManager;
import com.tangragaming.fragments.FragmentNewsFeed;
import com.tangragaming.fragments.FragmentRoomInvites;
import com.tangragaming.fragments.FragmentRoomManager;
import com.tangragaming.layout.MainLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.tangragaming.RssDetailActivity;

public class ReaderActivity extends FragmentActivity {
	// The MainLayout which will hold both the sliding menu and our main content
	// Main content will holds our Fragment respectively
	MainLayout mainLayout;

	// ListView menu
	private ListView lvMenu;
	private String[] lvMenuItems;

	// Menu button
	Button btnMenu;

	// Title according to fragment
	TextView tvTitle;

	// //Custom Adapter BEGIN to implement custom list items within the list
	// view later on
	//
	// private class CustomListAdapter extends ArrayAdapter {
	//
	// private Context mContext;
	// private int id;
	// private List <String>items ;
	//
	// public CustomListAdapter(Context context, int textViewResourceId ,
	// List<String> list )
	// {
	// super(context, textViewResourceId, list);
	// mContext = context;
	// id = textViewResourceId;
	// items = list ;
	// }
	//
	// @Override
	// public View getView(int position, View v, ViewGroup parent)
	// {
	// View mView = v ;
	// if(mView == null){
	// LayoutInflater vi =
	// (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// mView = vi.inflate(id, null);
	// }
	//
	// TextView text = (TextView) mView.findViewById(R.id.textView);
	//
	// if(items.get(position) != null )
	// {
	// text.setTextColor(Color.WHITE);
	// text.setText(items.get(position));
	// text.setBackgroundColor(Color.RED);
	// int color = Color.argb( 200, 255, 64, 64 );
	// text.setBackgroundColor( color );
	//
	// }
	//
	// return mView;
	// }
	//
	// }
	// //Custom Adapter END

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Inflate the mainLayout
		mainLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.fragment_layout, null);
		setContentView(mainLayout);

		// Init menu

		lvMenuItems = getResources().getStringArray(R.array.menu_items);
		lvMenu = (ListView) findViewById(R.id.activity_main_menu_listview);
		lvMenu.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, lvMenuItems));
		lvMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onMenuItemClick(parent, view, position, id);
			}

		});

		// Get menu button
		btnMenu = (Button) findViewById(R.id.activity_main_content_button_menu);
		btnMenu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Show/hide the menu
				toggleMenu(v);
			}
		});

		// Get title textview
		tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

		// Add FragmentMain as the initial fragment
		FragmentManager fm = ReaderActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		FragmentNewsFeed fragment = new FragmentNewsFeed();
		ft.add(R.id.activity_main_content_fragment, fragment);
		ft.commit();

		// Load a specific fragment from another activity
		Intent i = getIntent();
		String fragmentName = i.getStringExtra("fragment");
		String forum = "forum";
		if (fragmentName != null && fragmentName.equals(forum)) {

			FragmentManager fmForum = ReaderActivity.this
					.getSupportFragmentManager();
			FragmentTransaction ftForum = fmForum.beginTransaction();
			FragmentForum fragmentForum = new FragmentForum();

			if (fragmentForum != null) {
				// Replace current fragment by this new one
				ftForum.replace(R.id.activity_main_content_fragment,
						fragmentForum);
				ftForum.commit();

				// Set title accordingly
				tvTitle.setText("Forum");
			}
		}

	}

	public void toggleMenu(View v) {
		mainLayout.toggleMenu();
	}

	// Perform action when a menu item is clicked
	private void onMenuItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		String selectedItem = lvMenuItems[position];
		String currentItem = tvTitle.getText().toString();

		// Do nothing if selectedItem is currentItem
		if (selectedItem.compareTo(currentItem) == 0) {
			mainLayout.toggleMenu();
			return;
		}

		FragmentManager fm = ReaderActivity.this.getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		Fragment fragment = null;

		if (selectedItem.compareTo("Account Settings") == 0) {
			fragment = new FragmentAccountSettings();
		} else if (selectedItem.compareTo("Room Manager") == 0) {
			fragment = new FragmentRoomManager();
		} else if (selectedItem.compareTo("Friend Manager") == 0) {
			fragment = new FragmentFriendManager();
		} else if (selectedItem.compareTo("Find Friends") == 0) {
			fragment = new FragmentFindFriends();
		} else if (selectedItem.compareTo("Room Invites") == 0) {
			fragment = new FragmentRoomInvites();
		} else if (selectedItem.compareTo("Forum") == 0) {
			fragment = new FragmentForum();
		} else if (selectedItem.compareTo("News Reader") == 0) {
			fragment = new FragmentNewsFeed();
		}

		if (fragment != null) {
			// Replace current fragment by this new one
			ft.replace(R.id.activity_main_content_fragment, fragment);
			ft.commit();

			// Set title accordingly
			tvTitle.setText(selectedItem);
		}

		// Hide menu anyway
		mainLayout.toggleMenu();
	}

	@Override
	public void onBackPressed() {
		if (mainLayout.isMenuShown()) {
			mainLayout.toggleMenu();
		} else {
			super.onBackPressed();
		}
	}
}
