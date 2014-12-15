package com.webmyne.rightway.CurrentTrip;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.webmyne.rightway.Bookings.Driver;
import com.webmyne.rightway.Bookings.Trip;
import com.webmyne.rightway.CustomComponents.CallWebService;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.MapNavigator.Navigator;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.MapController;
import com.webmyne.rightway.R;
import com.webmyne.rightway.Receipt_And_Feedback.ReceiptAndFeedbackActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentCurrentTripMap extends Fragment {

    private MapView mv;
    private MapController mc;
    private LatLng driver_latlng;
    private LatLng pickup_latlng;
    private LatLng dropoff_latlng;
    private Trip currentTrip;

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

        setHasOptionsMenu(true);

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
        currentTrip=complexPreferences.getObject("current_trip_details", Trip.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_current_trip, container, false);

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
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
        currentTrip=complexPreferences.getObject("current_trip_details", Trip.class);

            mv.onResume();
        SharedPreferences preferencesRate = getActivity().getSharedPreferences("current_rate",getActivity().MODE_PRIVATE);
        String updatedTimeInterval=preferencesRate.getString("timeInterval","2");
//            SharedPreferences preferencesTimeInterval = getActivity().getSharedPreferences("driver_time_interval",getActivity().MODE_PRIVATE);
//            final String updatedTimeInterval=preferencesTimeInterval.getString("driver_time_interval", "5");


            pickup_latlng=new LatLng(Double.parseDouble(currentTrip.PickupLatitude),Double.parseDouble(currentTrip.PickupLongitude));
            dropoff_latlng=new LatLng(Double.parseDouble(currentTrip.DropoffLatitude),Double.parseDouble(currentTrip.DropoffLongitude));


            Timer timer=new Timer();

            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    try {
                        updateDriverLocation();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // updatedTimeInterval from API
                    //TODO
                }
            },0,1000*60*Integer.parseInt(updatedTimeInterval));

    }

    public void updateDriverLocation() {
        new CallWebService(AppConstants.DriverUpdatedLocation+currentTrip.DriverID, CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {
                Driver  availableDrivers= new GsonBuilder().create().fromJson(response, Driver.class);

//                Log.e("DriverID", availableDrivers.DriverID + "");
//                Log.e("FirstName", availableDrivers.FirstName+"");
//                Log.e("LastName", availableDrivers.LastName+"");
//                Log.e("Webmyne_Latitude", availableDrivers.Webmyne_Latitude+"");
//                Log.e("Webmyne_Longitude", availableDrivers.Webmyne_Longitude+"");

                driver_latlng =new LatLng(Double.parseDouble(availableDrivers.Webmyne_Latitude),Double.parseDouble(availableDrivers.Webmyne_Longitude));

                int zoom = (int)(mc.getMap().getMaxZoomLevel() - (mc.getMap().getMinZoomLevel()*2.5));

                mc.clearMarkers();

                if(driver_latlng != null) {

                    MarkerOptions opts = new MarkerOptions();
                    opts.position(driver_latlng);
                    opts.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_driver));
                    opts.title(currentTrip.DriverName+"");
                    opts.snippet("");
                    addMarker(opts);
                    mc.animateTo(driver_latlng,zoom);

                }

                if(pickup_latlng != null) {

                    MarkerOptions opts = new MarkerOptions();
                    opts.position(pickup_latlng);
                    opts.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pickup_pin));
                    opts.title("PICK ME UP HERE");
                    opts.snippet("");
                    addMarker(opts);
                }

                if(dropoff_latlng != null) {
                    MarkerOptions opts = new MarkerOptions();
                    opts.position(dropoff_latlng);
                    opts.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_dropoff_pin));
                    opts.title("DROP ME HERE");
                    opts.snippet("");
                    addMarker(opts);
                }

                Navigator nav = new Navigator(mv.getMap(),pickup_latlng,dropoff_latlng);
                nav.findDirections(false);
                nav.setPathColor(Color.parseColor("#4285F4"),Color.BLUE,Color.BLUE);
            }

            @Override
            public void error(VolleyError error) {
                Log.e("error response: ",error+"");
            }
        }.start();

    }

    private void addMarker(MarkerOptions opts) {

        mc.addMarker(opts, new MapController.MarkerCallback() {
            @Override
            public void invokedMarker(GoogleMap map, Marker marker) {

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mv.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.refresh, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateDriverLocation();
        }
        return true;
    }
}
