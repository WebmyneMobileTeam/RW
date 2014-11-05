package com.webmyne.rightway.CurrentTrip;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webmyne.rightway.R;

public class FragmentCurrentTripMap extends Fragment {

    public static FragmentCurrentTripMap newInstance(String param1, String param2) {
        FragmentCurrentTripMap fragment = new FragmentCurrentTripMap();

        return fragment;
    }

    public FragmentCurrentTripMap() {
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
