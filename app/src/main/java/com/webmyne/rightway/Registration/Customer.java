package com.webmyne.rightway.Registration;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhruvil on 21-10-2014.
 */
public class Customer {

    @SerializedName("CustomerID")
    public String CustomerID;
    @SerializedName("CustomerIMEI_Number")
    public String CustomerIMEI_Number;
    @SerializedName("NotificationID")
    public String NotificationID;
    @SerializedName("DeviceType")
    public String DeviceType;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("Email")
    public String Email;
    @SerializedName("City")
    public String City;
    @SerializedName("State")
    public String State;
    @SerializedName("ZipCode")
    public String ZipCode;
    @SerializedName("ProfilePicture")
    public String ProfilePicture;

    public Customer(){

    }
    public Customer(String customerID, String customerIMEI_Number, String notificationID, String deviceType, String name, String mobile, String email, String city, String state, String zipCode, String profilePicture) {
        CustomerID = customerID;
        CustomerIMEI_Number = customerIMEI_Number;
        NotificationID = notificationID;
        DeviceType = deviceType;
        Name = name;
        Mobile = mobile;
        Email = email;
        City = city;
        State = state;
        ZipCode = zipCode;
        ProfilePicture = profilePicture;
    }
}

