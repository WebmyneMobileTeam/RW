package com.webmyne.rightway.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.webmyne.rightway.MyNotifications.CustomerNotification;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SharedPreferenceNotification {

	public static final String PREF_NAME = "SHARED_DATA_NOTIFICATION";
	public static final String PREF_VALUE = "shared_values_for_notifications";
    List<CustomerNotification> driverNotifications =new ArrayList<CustomerNotification>();
	public SharedPreferenceNotification() {
		super();
	}


    public void clearNotification(Context context) {
        SharedPreferences sharedPref;
        Editor editor;
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        driverNotifications.clear();
        editor.clear();
        editor.commit();
    }
    public void saveNotification(Context context, CustomerNotification customerNotification) {
        SharedPreferences sharedPref;
        Editor editor;
        if (driverNotifications == null) {
            driverNotifications = new ArrayList<CustomerNotification>();
        }
        driverNotifications.add(customerNotification);
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(driverNotifications);
        editor.putString(PREF_VALUE, jsonFavorites);
        Log.e(" list:", jsonFavorites + "");
        editor.commit();
    }

    public ArrayList<CustomerNotification> loadNotification(Context context) {
        SharedPreferences sharePref;
        List<CustomerNotification> notificationList;
        sharePref = context.getSharedPreferences(PREF_NAME,context.MODE_PRIVATE);
        String jsonFavorites = sharePref.getString(PREF_VALUE, null);
        Gson gson = new Gson();
        CustomerNotification[] favoriteItems = gson.fromJson(jsonFavorites,CustomerNotification[].class);
        notificationList = new ArrayList<CustomerNotification>(Arrays.asList(favoriteItems));
        Log.e(" array", notificationList + "");
        return (ArrayList<CustomerNotification>) notificationList;
    }

}
