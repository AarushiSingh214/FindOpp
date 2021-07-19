package com.example.findopp;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel(analyze = User.class)
@ParseClassName("User")

//public class User extends ParseObject{
public class User extends ParseObject {
    public static final String TAG = "User";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_INTERESTS = "interests";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_OBJECTID = "objectId";
    private String currentUserName = ParseUser.getCurrentUser().getUsername();
    public static ArrayList<String> likedId = new ArrayList<String>();

    //getter and setter for username
    public String getUserName(){ return getString(KEY_USERNAME); }
    public void setUserName(String username){
        put(KEY_USERNAME, username);
    }

    //getter and setter for email
    public String getEmail(){
        return getString(KEY_EMAIL);
    }
    public void setEmail(String email){
        put(KEY_EMAIL, email);
    }

    //getter and setter for username
    public String getLocation2(){
        return getString(KEY_LOCATION);
    }
    public void setLocation2(String location){
        put(KEY_LOCATION, location);
    }

    //getter and setter for username
    public String getInterests(){
        return getString(KEY_INTERESTS);
    }
    public void setInterests(String interests){
        put(KEY_INTERESTS, interests);
    }

}
