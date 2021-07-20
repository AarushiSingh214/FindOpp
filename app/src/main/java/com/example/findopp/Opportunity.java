package com.example.findopp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import org.parceler.Parcel;

import java.io.Serializable;
import java.util.ArrayList;

@Parcel(analyze = Opportunity.class)
@ParseClassName("Opportunities")
public class Opportunity extends ParseObject implements Serializable {

    public static final String KEY_NAME = "name";
    public static final String KEY_POINT_OF_CONTACT = "point_of_contact";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SUPPLIES = "supplies";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AGE = "age";
    public static final String KEY_COST = "cost";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_LIKES = "likes";
    public static final String KEY_OBJECTID = "objectId";
    public static final String KEY_INTERESTS = "interest";

    //getter and setter for name
    public String getName(){
        return getString(KEY_NAME);
    }
    public void setName(String name){
        put(KEY_NAME, name);
    }

    //getter and setter for point of contact
    public String getPointOfContact(){
        return getString(KEY_POINT_OF_CONTACT);
    }
    public void setPointOfContact(String pointOfContact){ put(KEY_POINT_OF_CONTACT, pointOfContact); }

    //getter and setter for location
    public String getLocation(){
        return getString(KEY_LOCATION);
    }
    public void setLocation(String location){
        put(KEY_LOCATION, location);
    }

    //getter and setter for duration
    public String getDuration(){
        return getString(KEY_DURATION);
    }
    public void setDuration(String duration){
        put(KEY_DURATION, duration);
    }

    //getter and setter for description
    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    //getter and setter for supplies
    public String getSupplies(){
        return getString(KEY_SUPPLIES);
    }
    public void setSupplies(String supplies){
        put(KEY_SUPPLIES, supplies);
    }

    //getter and setter for image
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }

    //getter and setter for title
    public String getTitle(){
        return getString(KEY_TITLE);
    }
    public void setTitle(String title){
        put(KEY_TITLE, title);
    }

    //getter and setter for age
    public String getAge(){
        return getString(KEY_AGE);
    }
    public void setAge(String age){
        put(KEY_AGE, age);
    }

    //getter and setter for cost
    public String getCost(){
        return getString(KEY_COST);
    }
    public void setCost(String cost){
        put(KEY_COST, cost);
    }

    //getter and setter for likes
    public String getLikes(){
        return getString(KEY_LIKES);
    }
    public void setLikes(String likes){
        put(KEY_LIKES, likes);
    }

    //getter and setter for objectID
    public String getObjectId(){
        return getString(KEY_OBJECTID);
    }
    public void setObjectId(String objectId){
        put(KEY_OBJECTID, objectId);
    }

    //getter and setter for interest
    public String getInterest(){ return getString(KEY_INTERESTS); }
    public void setInterest(String interest){
        put(KEY_INTERESTS, interest);
    }

}
