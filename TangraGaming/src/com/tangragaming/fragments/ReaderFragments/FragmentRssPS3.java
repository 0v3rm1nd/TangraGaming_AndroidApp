package com.tangragaming.fragments.ReaderFragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangragaming.R;

@SuppressLint("NewApi")
public class FragmentRssPS3 extends Fragment{
    TextView textView;
    
    public FragmentRssPS3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rss_ps3, null);

        textView = (TextView) view.findViewById(R.id.fragment_rss_ps3);
        
        return view;
    }

}
