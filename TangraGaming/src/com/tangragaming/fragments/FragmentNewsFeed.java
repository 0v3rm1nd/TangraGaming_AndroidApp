package com.tangragaming.fragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.tangragaming.R;
import com.tangragaming.fragments.ReaderFragments.FragmentRssNINTENDO;
import com.tangragaming.fragments.ReaderFragments.FragmentRssPC;
import com.tangragaming.fragments.ReaderFragments.FragmentRssPS2;
import com.tangragaming.fragments.ReaderFragments.FragmentRssPS3;
import com.tangragaming.fragments.ReaderFragments.FragmentRssPSP;
import com.tangragaming.fragments.ReaderFragments.FragmentRssWII;
import com.tangragaming.fragments.ReaderFragments.FragmentRssXBOX360;

@SuppressLint("NewApi")
public class FragmentNewsFeed extends Fragment {

	TextView btnPc;
	TextView btnPs2;
	TextView btnPs3;
	TextView btnPs4;
	TextView btnPsp;
	TextView btnXbox360;
	TextView btnNintendo;
	TextView btnWii;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_news_feed, null);
		btnPc = (Button) view.findViewById(R.id.pc);
		btnPs2 = (Button) view.findViewById(R.id.ps2);
		btnPs3 = (Button) view.findViewById(R.id.ps3);
		btnPs4 = (Button) view.findViewById(R.id.ps4);
		btnPsp = (Button) view.findViewById(R.id.psp);
		btnXbox360 = (Button) view.findViewById(R.id.xbox360);
		btnNintendo = (Button) view.findViewById(R.id.nintendo);
		btnWii = (Button) view.findViewById(R.id.wii);

		// PC fragment swap
		btnPc.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssPC fragmentrsspc = new FragmentRssPC();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrsspc);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		// PS3 fragment swap
		btnPs3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssPS3 fragmentrssps3 = new FragmentRssPS3();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrssps3);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		// PS4 fragment swap
		btnPs4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssPS3 fragmentrssps4 = new FragmentRssPS3();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrssps4);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});

		// PSP fragment swap
		btnPsp.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssPSP fragmentrsspsp = new FragmentRssPSP();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrsspsp);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});

		// XBOX360 fragment swap
		btnXbox360.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssXBOX360 fragmentrssxbox360 = new FragmentRssXBOX360();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrssxbox360);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		// Nintendo fragment swap
		btnNintendo.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssNINTENDO fragmentrssnintendo = new FragmentRssNINTENDO();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrssnintendo);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		// Wii fragment swap
		btnWii.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssWII fragmentrsswii = new FragmentRssWII();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrsswii);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		
		// PS2 fragment swap
		btnPs2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// on click replace the main RSS fragment with another fragment
				// fragment
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();

				FragmentRssPS2 fragmentrssps2 = new FragmentRssPS2();
				fragmentTransaction.replace(
						((ViewGroup) (getView().getParent())).getId(),
						fragmentrssps2);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

			}
		});
		return view;
	}
}
