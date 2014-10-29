package com.webmyne.rightway.Bookings;

/**
 * Created by dhruvil on 20-10-2014.
 */
public class Trip {

    public int id;

    public String customer_name;

    public String customer_mobile;

    public String pickup_latitude;

    public String pickup_longitude;

    public String pickup_location;

    public String drop_latitude;

    public String drop_longitude;

    public String drop_location;

    public String driver_id;

    public String pick_time;

    public String drop_time;

    public String cost;

    public String tip;

    public String payment_method;

    public String status;

    public Trip() {

    }

    public Trip(String customer_name, String customer_mobile, String pickup_latitude,
                String pickup_longitude, String pickup_location, String drop_latitude,
                String drop_longitude, String drop_location, String driver_id, String pick_time,
                String drop_time, String cost, String tip, String payment_method, String status) {

        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.pickup_latitude = pickup_latitude;
        this.pickup_longitude = pickup_longitude;
        this.pickup_location = pickup_location;
        this.drop_latitude = drop_latitude;
        this.drop_longitude = drop_longitude;
        this.drop_location = drop_location;
        this.driver_id = driver_id;
        this.pick_time = pick_time;
        this.drop_time = drop_time;
        this.cost = cost;
        this.tip = tip;
        this.payment_method = payment_method;
        this.status = status;
    }
}
