package com.webmyne.rightway.Settings;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webmyne.rightway.CustomComponents.ListDialog;
import com.webmyne.rightway.R;

import java.util.ArrayList;


public class SettingsFragment extends Fragment implements ListDialog.setSelectedListner{
    LinearLayout linearIntervalTime;
    TextView txtUpdateTime;
    ArrayList<String> timeList;
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeList=new ArrayList<String>();
        timeList.add("5 minutes");
        timeList.add("10 minutes");
        timeList.add("15 minutes");
        timeList.add("20 minutes");
        timeList.add("25 minutes");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View convertView= inflater.inflate(R.layout.fragment_settings, container, false);
        linearIntervalTime=(LinearLayout)convertView.findViewById(R.id.linearIntervalTime);
        txtUpdateTime=(TextView)convertView.findViewById(R.id.txtUpdateTime);
        linearIntervalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        return convertView;
    }

    public void showDialog() {

        ListDialog listDialog = new ListDialog(getActivity(), android.R.style.Theme_Translucent_NoTitleBar);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);
        listDialog.title("SELECT TIME");
        listDialog.setItems(timeList);
        listDialog.setSelectedListner(this);
        listDialog.show();
    }

    @Override
    public void selected(String value) {

        txtUpdateTime.setText(value);

    }

}
