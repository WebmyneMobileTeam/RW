package com.webmyne.rightway.MyNotifications;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.webmyne.rightway.CustomComponents.CallWebService;
import com.webmyne.rightway.CustomComponents.ComplexPreferences;
import com.webmyne.rightway.CustomComponents.ListDialog;
import com.webmyne.rightway.Model.AppConstants;
import com.webmyne.rightway.Model.ResponseMessage;
import com.webmyne.rightway.Model.SharedPreferenceNotification;
import com.webmyne.rightway.R;
import com.webmyne.rightway.Registration.Customer;

import java.util.ArrayList;

public class MyNotificationFragment extends Fragment implements ListDialog.setSelectedListner {

    ArrayList<CustomerNotification> notificationList;
    ListView lvCustomerNotifications;
     NotificationAdapter notificationAdapter;
    TextView txtDateSelectionForNotification;
    private SharedPreferenceNotification sharedPreferenceNotification;
    ArrayList<String> dateSelectionArray=new ArrayList<String>();
    public static MyNotificationFragment newInstance(String param1, String param2) {
        MyNotificationFragment fragment = new MyNotificationFragment();

        return fragment;
    }
    public MyNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateSelectionArray.add("Current Week");
        dateSelectionArray.add("Last Week");
        dateSelectionArray.add("Current Month");
        dateSelectionArray.add("Last Month");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_my_notification, container, false);
        txtDateSelectionForNotification=(TextView)rootView.findViewById(R.id.txtDateSelectionForNotification);
        txtDateSelectionForNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        lvCustomerNotifications=(ListView)rootView.findViewById(R.id.lvCustomerNotifications);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        unreadAllNotification();
        try {
            sharedPreferenceNotification = new SharedPreferenceNotification();
            notificationList = sharedPreferenceNotification.loadNotification(getActivity());

            if (notificationList != null) {
                notificationAdapter = new NotificationAdapter(getActivity(), notificationList);
                lvCustomerNotifications.setAdapter(notificationAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unreadAllNotification() {
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "customer_data", 0);
        Customer   customerDetails =complexPreferences.getObject("customer_data", Customer.class);
        new CallWebService(AppConstants.CustomerNotificationsStatusChanged+customerDetails.CustomerID , CallWebService.TYPE_JSONOBJECT) {

            @Override
            public void response(String response) {



                ResponseMessage responseMessage = new GsonBuilder().create().fromJson(response, ResponseMessage.class);

                Log.e("response message for unread all: ", responseMessage.Response + "");
            }

            @Override
            public void error(VolleyError error) {

                Log.e("error: ",error+"");

            }
        }.start();

    }

    public class NotificationAdapter extends BaseAdapter {

        Context context;

        LayoutInflater inflater;

        ArrayList<CustomerNotification> notificationList;


        public NotificationAdapter(Context context, ArrayList<CustomerNotification> notificationList) {

            this.context = context;
            this.notificationList = notificationList;
        }

        public int getCount() {
            return notificationList.size();
        }

        public Object getItem(int position) {
            return notificationList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView txtMessageTitle,txtNotificationDate,txtNotificationMessage,txtNotificationTime;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notification, parent, false);
                holder = new ViewHolder();
                holder.txtMessageTitle=(TextView)convertView.findViewById(R.id.txtMessageTitle);
                holder.txtNotificationDate=(TextView)convertView.findViewById(R.id.txtNotificationDate);
                holder.txtNotificationMessage=(TextView)convertView.findViewById(R.id.txtNotificationMessage);
                holder.txtNotificationTime=(TextView)convertView.findViewById(R.id.txtNotificationTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if(notificationList.get(position).Status.equalsIgnoreCase("false")){
                holder.txtNotificationDate.setTextColor(Color.GREEN);

            }
            holder.txtMessageTitle.setText(notificationList.get(position).Title);
            holder.txtNotificationDate.setText(notificationList.get(position).Date);
            holder.txtNotificationMessage.setText(notificationList.get(position).Message);
            holder.txtNotificationTime.setText(notificationList.get(position).notificationTime);
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

        txtDateSelectionForNotification.setText("Filtered By "+value);

    }
}
