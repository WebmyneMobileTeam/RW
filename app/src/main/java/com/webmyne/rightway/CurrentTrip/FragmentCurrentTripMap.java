package com.webmyne.rightway.CurrentTrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.rightway.Bookings.Driver;
import com.webmyne.rightway.CustomComponents.CallWebService;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.MapController;
import com.webmyne.rightway.R;
import com.webmyne.rightway.Receipt_And_Feedback.ReceiptAndFeedbackActivity;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentCurrentTripMap extends Fragment {
    ProgressDialog progressDialog;
    private MapView mv;
    private MapController mc;
    TextView getReceipt;
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
        getReceipt =(TextView)rootView.findViewById(R.id.txtgetReceipt);
        getReceipt.setVisibility(View.VISIBLE);
        getReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), ReceiptAndFeedbackActivity.class);
                startActivity(i);
            }
        });
        mv = (MapView)rootView.findViewById(R.id.map);
        setView(savedInstanceState);
        return rootView;
    }

    private void setView(Bundle savedInstanceState) {
        mv.onCreate(savedInstanceState);
        mc = new MapController(mv.getMap());
//        mc.whenMapClick(this);
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

        new CountDownTimer(1500, 1000) {
            @Override
            public void onFinish() {
                int zoom = (int)(mc.getMap().getMaxZoomLevel() - (mc.getMap().getMinZoomLevel()*2.5));
                try {
                    mc.animateTo(mc.getMyLocation().getLatitude(), mc.getMyLocation().getLongitude(), zoom);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onTick(long millisUntilFinished) {
            }
        }.start();

        Timer timer=new Timer();
        // stopLoginTimer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    updateDriverLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,1000*15);



    }

    public void updateDriverLocation() {
        new AsyncTask<Void,Void,Void>(){


            @Override
            protected Void doInBackground(Void... params) {
                new CallWebService(AppConstants.DriverUpdatedLocation+"9", CallWebService.TYPE_JSONOBJECT) {

                    @Override
                    public void response(String response) {

                      Driver  availableDrivers= new GsonBuilder().create().fromJson(response, Driver.class);
                            Log.e("DriverID", availableDrivers.DriverID + "");
                            Log.e("FirstName", availableDrivers.FirstName+"");
                            Log.e("LastName", availableDrivers.LastName+"");
                            Log.e("Webmyne_Latitude", availableDrivers.Webmyne_Latitude+"");
                            Log.e("Webmyne_Longitude", availableDrivers.Webmyne_Longitude+"");
                    }

                    @Override
                    public void error(VolleyError error) {
                        Log.e("error response: ",error+"");
                    }
                }.start();
                return null;
            }
        }.execute();

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv.onSaveInstanceState(outState);
    }
}
