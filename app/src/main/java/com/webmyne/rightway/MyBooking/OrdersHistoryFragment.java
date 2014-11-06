package com.webmyne.rightway.MyBooking;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import com.webmyne.rightway.CustomComponents.ListDialog;
import com.webmyne.rightway.R;

import java.util.ArrayList;


public class OrdersHistoryFragment extends Fragment implements ListDialog.setSelectedListner{

    ListView ordersHistoryListView;
    OrdersHistoryAdapter ordersHistoryAdapter;
    TextView txtDateSelection;
    ArrayList<String> ordersHistoryList =new ArrayList<String>();
    ArrayList<String> dateSelectionArray=new ArrayList<String>();

//    Spinner dateSelection;
    public static OrdersHistoryFragment newInstance(String param1, String param2) {
        OrdersHistoryFragment fragment = new OrdersHistoryFragment();
        return fragment;
    }
    public OrdersHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersHistoryList.add("one");
        ordersHistoryList.add("two");
        ordersHistoryList.add("three");
        ordersHistoryList.add("four");
        ordersHistoryList.add("five");

        dateSelectionArray.add("Current Week");
        dateSelectionArray.add("Last Week");
        dateSelectionArray.add("Current Month");
        dateSelectionArray.add("Last Month");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView=inflater.inflate(R.layout.fragment_orders_history, container, false);

//        // Spinner DropDown List
//        dateSelection=(Spinner)convertView.findViewById(R.id.dateSelection);
//        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,dateSelectionArray);
//        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dateSelection.setAdapter(spinnerAdapter);
        txtDateSelection=(TextView)convertView.findViewById(R.id.txtDateSelection);
        txtDateSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        ordersHistoryListView =(ListView)convertView.findViewById(R.id.ordersHistoryList);
        ordersHistoryAdapter =new OrdersHistoryAdapter(getActivity(), ordersHistoryList);
        ordersHistoryListView.setAdapter(ordersHistoryAdapter);

        return convertView;
    }




    public class OrdersHistoryAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> currentOrdersList;

        public OrdersHistoryAdapter(Context context, ArrayList<String> currentOrdersList) {
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
            TextView orderHistoryCname,orderHistoryDate,orderHistoryPickupLocation,orderHistoryDropoffLocation,orderHistoryStatus;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_ordered_history, parent, false);
                holder = new ViewHolder();
                holder.orderHistoryCname=(TextView)convertView.findViewById(R.id.orderHistoryCname);
                holder.orderHistoryDate=(TextView)convertView.findViewById(R.id.orderHistoryDate);
                holder.orderHistoryPickupLocation=(TextView)convertView.findViewById(R.id.orderHistoryPickupLocation);
                holder.orderHistoryDropoffLocation=(TextView)convertView.findViewById(R.id.orderHistoryDropoffLocation);
                holder.orderHistoryStatus=(TextView)convertView.findViewById(R.id.orderHistoryStatus);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "search_result_play",0);
//                    complexPreferences.putObject("searched_play",beanSearchList.get(position));
//                    complexPreferences.commit();
//                    Intent i=new Intent(getActivity(), PlayInfoActivity.class);
//                    startActivity(i);


                }
            });
            return convertView;


        }

    }

    public void showDialog() {

        ListDialog listDialog = new ListDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.title("SELECT DATE FILTER");
        listDialog.setItems(dateSelectionArray);
        listDialog.setSelectedListner(this);
        listDialog.show();
    }

    @Override
    public void selected(String value) {

        txtDateSelection.setText("Filtered By "+value);

    }


}
