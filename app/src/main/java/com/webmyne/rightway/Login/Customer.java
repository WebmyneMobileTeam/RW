package com.webmyne.rightway.Login;

/**
 * Created by dhruvil on 21-10-2014.
 */
public class Customer {

    public String CustomerID;
    public String CustomerIMEI_Number;
    public String NotificationID;
    public String DeviceType;
    public String Name;
    public String Mobile;
    public String Email;
    public String City;
    public String State;
    public String ZipCode;
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

