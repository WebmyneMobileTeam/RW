package com.webmyne.rightway.MyNotifications;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nirav on 13-11-2014.
 */
public class CustomerNotification {

    @SerializedName("CustomerID")
    public String CustomerID;
    @SerializedName("Date")
    public String Date;
    @SerializedName("Message")
    public String Message;
    @SerializedName("NotificationID")
    public String NotificationID;
    @SerializedName("Status")
    public String Status;
    @SerializedName("Time")
    public String notificationTime;
    @SerializedName("Title")
    public String Title;
    @SerializedName("TripID")
    public String TripID;

    public CustomerNotification() {
    }

    public CustomerNotification(String customerID, String date, String message, String notificationID, String status, String notificationTime, String title, String tripID) {
        CustomerID = customerID;
        Date = date;
        Message = message;
        NotificationID = notificationID;
        Status = status;
        this.notificationTime = notificationTime;
        Title = title;
        TripID = tripID;
    }

}
