package com.webmyne.rightway.MyBooking;

import android.app.Activity;
import android.content.Context;
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

import com.webmyne.rightway.R;

import java.util.ArrayList;

public class CanceledOrdersFragment extends Fragment {
    ListView ordersCanceledListView;
    OrdersCanceledAdapter ordersCanceledAdapter;
    ArrayList<String> ordersCanceledList =new ArrayList<String>();
    public static CanceledOrdersFragment newInstance(String param1, String param2) {
        CanceledOrdersFragment fragment = new CanceledOrdersFragment();

        return fragment;
    }

    public CanceledOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersCanceledList.add("one");
        ordersCanceledList.add("two");
        ordersCanceledList.add("three");
        ordersCanceledList.add("four");
        ordersCanceledList.add("five");
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View  convertView= inflater.inflate(R.layout.fragment_canceled_orders, container, false);
        ordersCanceledListView =(ListView)convertView.findViewById(R.id.canceledOrdersList);
        ordersCanceledAdapter =new OrdersCanceledAdapter(getActivity(), ordersCanceledList);
        ordersCanceledListView.setAdapter(ordersCanceledAdapter);
        return convertView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.filter,menu);
    }
    public class OrdersCanceledAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> currentOrdersList;

        public OrdersCanceledAdapter(Context context, ArrayList<String> currentOrdersList) {
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
                convertView = mInflater.inflate(R.layout.item_canceled_orders, parent, false);
                holder = new ViewHolder();
                holder.orderHistoryCname=(TextView)convertView.findViewById(R.id.orderCanceledCname);
                holder.orderHistoryDate=(TextView)convertView.findViewById(R.id.orderCanceledDate);
                holder.orderHistoryPickupLocation=(TextView)convertView.findViewById(R.id.orderCanceledPickupLocation);
                holder.orderHistoryDropoffLocation=(TextView)convertView.findViewById(R.id.orderCanceledDropoffLocation);
                holder.orderHistoryStatus=(TextView)convertView.findViewById(R.id.orderCanceledStatus);
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

}
