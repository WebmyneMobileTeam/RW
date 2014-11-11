package com.webmyne.rightway.Bookings;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav on 10-11-2014.
 */
public class Driver {

    @SerializedName("DriverID")
    public String DriverID;
    @SerializedName("DriverNotificationID")
    public String DriverNotificationID;
    @SerializedName("FirstName")
    public String FirstName;
    @SerializedName("LastName")
    public String LastName;
    @SerializedName("Webmyne_Latitude")
    public String Webmyne_Latitude;
    @SerializedName("Webmyne_Longitude")
    public String Webmyne_Longitude;


    public Driver() {

    }

    public Driver(String webmyne_Longitude, String driverID, String driverNotificationID, String firstName, String lastName, String webmyne_Latitude) {
        Webmyne_Longitude = webmyne_Longitude;
        DriverID = driverID;
        DriverNotificationID = driverNotificationID;
        FirstName = firstName;
        LastName = lastName;
        Webmyne_Latitude = webmyne_Latitude;
    }
}
