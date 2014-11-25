package com.webmyne.rightway.Model;

/**
 * Created by nirav on 07-11-2014.
 */
public class AppConstants {

    //POST
    public static final String bookTrip="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/BookTrip";
    public static final String CancelTrip="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/CancelTrip";
    public static final String TripSuccessful="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/TripSuccessful";
    public static final String customerProfile ="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/CustomerProfile";

    //GET
    public static final String getActiveDrivers="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/ActiveDrivers";

    public static final String getTripList="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/CustomerTrips/";
    public static final String DriverUpdatedLocation="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/DriverUpdatedLocation/";

    public static final String GetCustomerNotifications="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/GetCustomerNotifications/";

    public static final String CustomerNotificationsStatusChanged="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Customer.svc/json/CustomerNotificationsStatusChanged/";

    //GET Current rates
    public static final String CurrentRate ="http://ws-srv-net.in.webmyne.com/Applications/Android/RiteWayServices/Driver.svc/json/CurrentRate";

    // FTP path
    public static final String ftpPath="http://ws-srv-net.in.webmyne.com/RiteWay/Images/";

    // FTP Username and Password
    public static final String ftpUsername="riteway";
    public static final String ftpPassword="riteway";

    // Trip Status
    public static final String tripInProgressStatus="In Progress";
    public static final String tripOnTripStatus="On Trip";
    public static final String tripCancelledByDriverStatus="Cancelled By Driver";
    public static final String tripCancelledByCustomerStatus="Canceled By Customer";
    public static final String tripAcceptStatus="Accept";
    public static final String tripSuccessStatus="Success";

}
