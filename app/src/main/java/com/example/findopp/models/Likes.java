package com.example.findopp.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze = Likes.class)
@ParseClassName("Likes")
public class Likes extends ParseObject {

    public static final String KEY_OPP = "opportunity";
    public static final String KEY_USER = "user";

    //default constructor
//    public Likes(){
//    }

    //getter and setter for opportunity
    public Opportunity getOpp(){
        return (Opportunity) getParseObject(KEY_OPP);
    }
    public void setOpp(Opportunity opp){
        put(KEY_OPP, opp);
    }

    //getter and setter for username
    public ParseUser getUsers(){
        return getParseUser(KEY_USER);
    }
    public void setUsers(ParseUser user){put(KEY_USER, user);}

    public void setUpdate(ParseUser user, ParseObject opportunity){
        put(KEY_USER, user);
        put(KEY_OPP, opportunity);
    }

}
