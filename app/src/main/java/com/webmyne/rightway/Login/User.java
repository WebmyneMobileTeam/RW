package com.webmyne.rightway.Login;

/**
 * Created by dhruvil on 21-10-2014.
 */
public class User {

    public String id;
    public String customer_id;
    public String photo_url;
    public String preffered_driver;
    public String zipcode;
    public String state;
    public String city;
    public String email;
    public String mobile;
    public String name;


    public User() {
    }

    public User(String id, String customer_id, String photo_url, String preffered_driver,
                String zipcode, String state, String city, String email, String mobile, String name) {
        this.id = id;
        this.customer_id = customer_id;
        this.photo_url = photo_url;
        this.preffered_driver = preffered_driver;
        this.zipcode = zipcode;
        this.state = state;
        this.city = city;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
    }
}
