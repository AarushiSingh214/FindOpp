package com.example.findopp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Opportunity.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5379mKkhN29LJSeT5k1cY008RoyORhoJYLviZFaL")
                .clientKey("rzDVTjS3R7oaVCg3yjPEU200HIMoUgTH1jDGfG5J")
                .server("https://parseapi.back4app.com")
                .build());

    }
}
