package com.webmyne.rightway.Bookings;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.webmyne.rightway.Application.DrawerActivity;
import com.webmyne.rightway.CustomComponents.NonSwipeableViewPager;
import com.webmyne.rightway.Model.MapController;
import com.webmyne.rightway.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BookCabFragment extends Fragment implements View.OnClickListener,MapController.ClickCallback{


    public MyPageAdapter adapter;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private MapView mv;
    private MapController mc;
    public NonSwipeableViewPager viewPager;
    private String[] count = {"1","1","1","1","1","1","1","1","1"};
    private static String[] titles = {"PICK UP LOCATION",
            "SELECT PICKUP TIME",
            "PICKUP PLACE NOTE",
            "WHERE YOU WANT TO GO?",
            "PICK DRIVER",
            "HOW YOU WILL DO PAYMENT?",
            "TIP PERCENTAGE",
            "" };
    private ImageView btnPrevious;
    private TextView headerTitleForm;
    private boolean isPickUpLocationAdded = false;

    public static BookCabFragment newInstance(String param1, String param2) {
        BookCabFragment fragment = new BookCabFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public BookCabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_cab, container, false);
        mv = (MapView)view.findViewById(R.id.map);
        btnPrevious = (ImageView)view.findViewById(R.id.imgPreviousForm);
        btnPrevious.setOnClickListener(this);
        btnPrevious.setVisibility(View.INVISIBLE);
        headerTitleForm = (TextView)view.findViewById(R.id.headerTitleForm);


        setView(savedInstanceState);
        viewPager = (NonSwipeableViewPager)view.findViewById(R.id.nonSwipableViewPagerForm);

        setHeaderTitle(titles[0]);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0){
                  btnPrevious.setVisibility(View.INVISIBLE);
                }else{
                    btnPrevious.setVisibility(View.VISIBLE);
                }
                setHeaderTitle(titles[i]);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return view;
    }

    private void setView(Bundle savedInstanceState) {

        mv.onCreate(savedInstanceState);
        mc = new MapController(mv.getMap());

        mc.whenMapClick(this);
/*        mc.startTrackMyLocation(new MapController.ChangeMyLocation() {
            @Override
            public void changed(GoogleMap map, Location location,
                                boolean lastLocation) {
                Toast.makeText(getActivity(), location.toString(),
                        Toast.LENGTH_SHORT).show();
              //   mc.moveToMyLocation();


            }
        });*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {

        super.onResume();
        mv.onResume();

        mc.startTrackMyLocation(mc.getMap(),2000,0, MapController.TrackType.TRACK_TYPE_NONE,new MapController.ChangeMyLocation() {
            @Override
            public void changed(GoogleMap map, Location location, boolean lastLocation) {

            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {

                        Log.e("Resume called ",""+titles.length);
                        adapter = new MyPageAdapter(getActivity().getSupportFragmentManager());
                        adapter.notifyDataSetChanged();

                        viewPager.setAdapter(null);
                        viewPager.setAdapter(adapter);

                        Log.e("Adapter after resume ",""+adapter);
                    }
                });

            }
        });
    }

    @Override
    public void onPause() {
        mv.onPause();
        mc.stopTrackMyLocation();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mv.onDestroy();

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mv.onLowMemory();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv.onSaveInstanceState(outState);
    }


    public void setNextView(){

        viewPager.setCurrentItem(viewPager.getCurrentItem()+1,true);
    }

    public void setPreviousView(){

        viewPager.setCurrentItem(viewPager.getCurrentItem()-1,true);

    }

    public void setHeaderTitle(String title){
        headerTitleForm.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.imgPreviousForm:
                BookCabFragment fragment1 = (BookCabFragment)getActivity().getSupportFragmentManager().findFragmentByTag(DrawerActivity.BOOKCAB);
                fragment1.setPreviousView();

                break;

        }
    }

    @Override
    public void clicked(GoogleMap map, LatLng latLng) {

        if(isPickUpLocationAdded == false){

            mc.clearMarkers();
            MarkerOptions opts = new MarkerOptions();
            opts.position(latLng);
            opts.icon(BitmapDescriptorFactory.defaultMarker());
            opts.title("PICK ME UP HERE");
            opts.snippet("");
            addMarker(opts);

        }else{


        }



    }

    private void addMarker(MarkerOptions opts) {
        mc.addMarker(opts, new MapController.MarkerCallback() {
            @Override
            public void invokedMarker(GoogleMap map, Marker marker) {

            }
        });
    }


    public static class MyPageAdapter  extends FragmentStatePagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {

            BookingFormFragment fragment = new BookingFormFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_PARAM1,position);
            args.putString(ARG_PARAM2,titles[position]);
            fragment.setArguments(args);

            return fragment;

        }


        @Override

        public int getCount() {
            return 8;

        }
    }



}
