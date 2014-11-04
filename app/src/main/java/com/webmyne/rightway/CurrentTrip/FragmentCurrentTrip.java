package com.webmyne.rightway.CurrentTrip;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.rightway.R;

public class FragmentCurrentTrip extends Fragment {

    public static FragmentCurrentTrip newInstance(String param1, String param2) {
        FragmentCurrentTrip fragment = new FragmentCurrentTrip();

        return fragment;
    }

    public FragmentCurrentTrip() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_current_trip, container, false);
        return rootView;
    }


}
