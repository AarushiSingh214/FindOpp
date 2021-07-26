package com.example.findopp.models;


import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel(analyze = Interests.class)
@ParseClassName("Interests")
public class Interests extends ParseObject {

    //ParseObject interest = new ParseObject("Interests");
    public static final String KEY_OPPINTEREST = "oppInterest";
    public static final ArrayList KEY_RELATEDINTEREST = new ArrayList<String>();

    //default constructor
//    public Interests(){
//    }

    //getter and setter for opportunity interest
    public String getOppInterest(){
        return KEY_OPPINTEREST;
    }
    public void setOppInterest(String opp){
        put(KEY_OPPINTEREST, opp);
    }

    //getter and setter for related interest
    //public ArrayList<String> getRelatedInterest(){return (ArrayList) interest.getJSONArray("relatedInterest");}
    public ArrayList<String> getRelatedInterest(){return KEY_RELATEDINTEREST;}
    //public void setRelatedInterest(ArrayList<String> relatedInterest){put(KEY_RELATEDINTEREST)}

    //public void setUsers(ParseUser user){put(KEY_RELATEDINTEREST, user);}

}
