package com.tangragaming;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tangragaming.xmlparse.*;

public class RssDetailActivity extends Activity {

	RSSFeed feed;
	TextView title;
	Button btnlinkToActualFeed;
	Button btnlinkToForum;
	WebView desc;

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rssfeed_item_detail);

		// Enable the vertical fading edge (by default it is disabled)
		ScrollView sv = (ScrollView) findViewById(R.id.sv);
		sv.setVerticalFadingEdgeEnabled(true);

		// Get the feed object and the position from the Intent
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		final int pos = getIntent().getExtras().getInt("pos");

		// Initialize the views
		title = (TextView) findViewById(R.id.title);
		desc = (WebView) findViewById(R.id.desc);
		btnlinkToForum = (Button) findViewById(R.id.btnLinkToForum);
		btnlinkToActualFeed = (Button) findViewById(R.id.btnLinkToActualFeed);

		// set webview properties
		WebSettings ws = desc.getSettings();
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.getPluginState();
		ws.setPluginState(PluginState.ON);
		ws.setJavaScriptEnabled(true);
		ws.setBuiltInZoomControls(true);

		// Set the views
		title.setText(feed.getItem(pos).getTitle());
		desc.loadDataWithBaseURL(
				"http://www.gamestop.com/SyndicationHandler.ashx?Filter=BestSellers&platform=xbox360",
				feed.getItem(pos).getDescription(), "text/html", "UTF-8", null);

		// Link to the actual website for the full feed
		btnlinkToActualFeed.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri
						.parse(feed.getItem(pos).getLink())));
			}
		});

		// Link To Forum functionality
		btnlinkToForum.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						ReaderActivity.class);
				// pass data to an intent to load a specific fragment of reader
				// activity
				String fragmnet = "forum";
				i.putExtra("fragment", fragmnet);
				startActivity(i);
			}
		});
	}

}
