package com.example.inger.beaconbeachler;

/**
 * Created by Marielle on 08-Feb-16.
 */
public class Config {

    //Store minor for beacon
    public static final String KEY_MINOR = "8";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "sessionVar";

    //This would be used to store the email of current logged in user
    public static final String USERNAME_SHARED_PREF = "username";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String BEACON_PICTURE_PREF = "inrange";

}
