package com.webmyne.rightway.MyNotifications;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
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

import com.webmyne.rightway.R;

import java.util.ArrayList;

public class MyNotificationFragment extends Fragment {

    ArrayList<String> notificationList=new ArrayList<String>();
    ListView lvCustomerNotifications;
     NotificationAdapter notificationAdapter;
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
        notificationList.add("One");
        notificationList.add("Two");
        notificationList.add("Three");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_my_notification, container, false);
        notificationAdapter=new NotificationAdapter(getActivity(),notificationList);
        lvCustomerNotifications=(ListView)rootView.findViewById(R.id.lvCustomerNotifications);
        lvCustomerNotifications.setAdapter(notificationAdapter);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.filter,menu);

    }

    public class NotificationAdapter extends BaseAdapter {

        Context context;

        LayoutInflater inflater;

        ArrayList<String> notificationList;


        public NotificationAdapter(Context context, ArrayList<String> notificationList) {

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


        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_notification, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;

        }

    }
}
