package com.tangragaming.layout;

//import com.tangragaming.fragments.GamesFragment;
//import com.tangragaming.fragments.NintendoFragment;
//import com.tangragaming.fragments.PCFragment;
//import com.tangragaming.fragments.PS3Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/*
	 * TABS NAVIGATION NOT USED CURRENTLY BUT IT IS A SMART WAY TO SLIDE THROUGH
	 * FRAGMENTS
	 */

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Games fragment activity
//			return new GamesFragment();
//		case 1:
//			return new PS3Fragment();
//		case 2:
//			return new PCFragment();
//		case 3:
//			return new NintendoFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
