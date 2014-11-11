package com.webmyne.rightway.MyBooking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.webmyne.rightway.Bookings.Trip;
import com.webmyne.rightway.CustomComponents.CallWebService;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Login.Customer;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.PagerSlidingTabStrip;
import com.webmyne.rightway.Model.SharedPreferenceTrips;
import com.webmyne.rightway.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MyBookingFragment extends Fragment {
    private SharedPreferenceTrips sharedPreferenceTrips;
    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    public ArrayList<Trip> tripArrayList=new ArrayList<Trip>();
    ProgressDialog progressDialog;
    Customer customerDetails;
    public static MyBookingFragment newInstance(String param1, String param2) {
        MyBookingFragment fragment = new MyBookingFragment();
        return fragment;
    }

    public MyBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_my_booking, container, false);
        tabs = (PagerSlidingTabStrip)convertView.findViewById(R.id.my_order_tabs);
        pager = (ViewPager) convertView.findViewById(R.id.pager);

        return convertView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripList();
        sharedPreferenceTrips=new SharedPreferenceTrips();
    }

    public void getTripList() {

        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        customerDetails =complexPreferences.getObject("customer_data", Customer.class);

        Log.e("list: ",AppConstants.getTripList+customerDetails.CustomerID +"");

        new AsyncTask<Void,Void,Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Loading...");
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {

                new CallWebService(AppConstants.getTripList+customerDetails.CustomerID , CallWebService.TYPE_JSONARRAY) {

                    @Override
                    public void response(String response) {

                        Type listType=new TypeToken<List<Trip>>(){
                        }.getType();
                        tripArrayList = new GsonBuilder().create().fromJson(response, listType);


                        handleTripListData();
                    }

                    @Override
                    public void error(VolleyError error) {

                        Log.e("error: ",error+"");

                    }
                }.start();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.dismiss();
            }


        }.execute();


    }

    public void handleTripListData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                sharedPreferenceTrips.clearTrip(getActivity());
                for(int i=0;i<tripArrayList.size();i++){
                    sharedPreferenceTrips.saveTrip(getActivity(),tripArrayList.get(i));

                }
                adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
                pager.setAdapter(adapter);
                final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
                pager.setPageMargin(pageMargin);
                tabs.setViewPager(pager);
            }
        });

    }


    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        private final String[] TITLES = { "Current", "History", "Canceled"};
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment=null;
            if(i==0) {
                fragment=CurrentOrdersFragment.newInstance("","");
            } else if(i==1) {
                fragment=OrdersHistoryFragment.newInstance("","");
            }else if(i==2) {
                fragment=CanceledOrdersFragment.newInstance("","");
            }
            return fragment;
        }
    }
}
