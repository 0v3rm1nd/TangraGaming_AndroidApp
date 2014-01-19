package com.tangragaming.fragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tangragaming.R;

@SuppressLint("NewApi")
public class FragmentRoomManager extends Fragment{
    TextView textView;
    
    public FragmentRoomManager() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_room_manager, null);

        textView = (TextView) view.findViewById(R.id.fragment_room_manager);
        
        return view;
    }

}
