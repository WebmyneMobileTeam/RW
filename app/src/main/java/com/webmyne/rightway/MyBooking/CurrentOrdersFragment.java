package com.webmyne.rightway.MyBooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.webmyne.rightway.Bookings.Trip;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.Model.SharedPreferenceTrips;
import com.webmyne.rightway.R;

import java.util.ArrayList;


public class CurrentOrdersFragment extends Fragment {

    ListView currentOrdersListView;
    CurrentOrdersAdapter currentOrdersAdapter;
    ArrayList<Trip> currentOrdersList;
    SharedPreferenceTrips sharedPreferenceTrips;
    public static CurrentOrdersFragment newInstance(String param1, String param2) {
        CurrentOrdersFragment fragment = new CurrentOrdersFragment();
        return fragment;
    }
    public CurrentOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferenceTrips=new SharedPreferenceTrips();
        currentOrdersList=sharedPreferenceTrips.loadTrip(getActivity());
        currentOrdersAdapter=new CurrentOrdersAdapter(getActivity(), currentOrdersList);
        currentOrdersListView.setAdapter(currentOrdersAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView=inflater.inflate(R.layout.fragment_current_orders, container, false);
        currentOrdersListView=(ListView)convertView.findViewById(R.id.currentOrdersList);


        return convertView;
    }


    public class CurrentOrdersAdapter extends BaseAdapter {

        Context context;
        ArrayList<Trip> currentOrdersList;

        public CurrentOrdersAdapter(Context context, ArrayList<Trip> currentOrdersList) {
            this.context = context;
            this.currentOrdersList = currentOrdersList;
        }

        public int getCount() {
            return currentOrdersList.size();
        }

        public Object getItem(int position) {
            return currentOrdersList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView currentOrderCname,currentOrderDate,currentOrderPickupLocation,currentOrderDropoffLocation,currentOrderFareAmount,currentOrderStatus;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_current_orders, parent, false);
                holder = new ViewHolder();
                holder.currentOrderCname=(TextView)convertView.findViewById(R.id.currentOrderCname);
                holder.currentOrderDate=(TextView)convertView.findViewById(R.id.currentOrderDate);
                holder.currentOrderPickupLocation=(TextView)convertView.findViewById(R.id.currentOrderPickupLocation);
                holder.currentOrderDropoffLocation=(TextView)convertView.findViewById(R.id.currentOrderDropoffLocation);
                holder.currentOrderFareAmount=(TextView)convertView.findViewById(R.id.currentOrderFareAmount);
                holder.currentOrderStatus=(TextView)convertView.findViewById(R.id.currentOrderStatus);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            holder.currentOrderCname.setText(currentOrdersList.get(position).DriverID);
            holder.currentOrderDate.setText(currentOrdersList.get(position).TripDate);
            holder.currentOrderPickupLocation.setText("pickup: "+currentOrdersList.get(position).PickupAddress);
            holder.currentOrderDropoffLocation.setText("dropoff: "+currentOrdersList.get(position).DropOffAddress);
//            holder.currentOrderFareAmount.setText(currentOrdersList.get(position).TripFare);
            holder.currentOrderStatus.setText("status: "+currentOrdersList.get(position).TripStatus);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "current_trip_details", 0);
                    complexPreferences.putObject("current_trip_details", currentOrdersList.get(position));
                    complexPreferences.commit();

                    Intent i=new Intent(getActivity(), OrderDetailActivity.class);
                    startActivity(i);


                }
            });
            return convertView;


        }

    }

}
