package com.example.inger.beaconbeachler;

/**
 * Created by Marielle on 08-Feb-16.
 */
public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "https://home.hbv.no/110118/bachelor/test.php";
    public static final String REGISTER_URL ="https://home.hbv.no/110118/bachelor/registrerFacebook.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRSTNAME = "firstName";
    public static final String KEY_LASTNAME = "lastName";

    //Store minor for beacon
    public static final String KEY_MINOR = "minor";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    //Facebook values
    public String facebookID;
    public String firstName;
    public String lastName;
    public String username;
}
