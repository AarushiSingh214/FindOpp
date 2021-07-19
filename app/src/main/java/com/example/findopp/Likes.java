package com.example.findopp;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Likes.class)
@ParseClassName("Likes")
public class Likes extends ParseObject {

    public static final String KEY_OPP = "opportunity";
    public static final String KEY_USERNAME = "userName";

    //getter and setter for name
    public String getOpp(){
        return getString(KEY_OPP);
    }
    public void setOpp(String opp){
        put(KEY_OPP, opp);
    }

    //getter and setter for username
    public String getUserName(){
        return getString(KEY_USERNAME);
    }

    //want user to be the current user
    //public void setUserName(User userName){put(KEY_USERNAME, userName.getUserName());}

    //when u set the username, you want it to be the current user that liked that specific opportunity
    public void setUserName2(User userName){put(KEY_USERNAME, ParseUser.getCurrentUser());}
}
