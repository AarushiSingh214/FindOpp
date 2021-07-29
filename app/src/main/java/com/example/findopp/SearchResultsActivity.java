package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.findopp.models.Interests;
import com.example.findopp.models.Opportunity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private TextView tvSearch;
    private TextView tvNoResults;
    private SearchAdapter adapter;
    public static final String TAG = "Search Results Activity";
    public ArrayList<Opportunity> filterOpps;
    public ArrayList<Opportunity> relatedOpps;
    private ArrayList<String> oppLocation;
    String inputLocation;
    String inputAge;
    String inputDuration;
    String inputInterest;
    String inputRadius;
    String destination = "";
    HashMap<String, String> map;

    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        rvSearchResults = findViewById(R.id.rvSearchResults);
        tvNoResults = findViewById(R.id.tvNoResults);
        tvSearch = findViewById(R.id.tvSearch);
        tvSearch.setText("Here's What We Found!");
        pb = (ProgressBar) findViewById(R.id.pbLoading);


        Intent intent = getIntent();
        inputLocation = intent.getStringExtra("location");
        inputAge = intent.getStringExtra("age");
        inputDuration = intent.getStringExtra("duration");
        inputInterest = intent.getStringExtra("interest");
        inputRadius = intent.getStringExtra("radius");

        //if the user enters no radius, set a small default radius so algorithm doesn't crash
        if(inputRadius.equals("")){
            inputRadius = "5";
        }

        //before the opportunities are displayed, the loading symbol should be displayed for waiting period
        pb.setVisibility(ProgressBar.VISIBLE);

        map = new HashMap<String, String>();
        oppLocation = new ArrayList<>();
        filterOpps = new ArrayList<>();
        relatedOpps = new ArrayList<>();
        adapter = new SearchAdapter(this, filterOpps);
        //adapter = new OppAdapter(this, relatedOpps);

        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        queryOpps();

    }

    //parses through the Opportunity class and gets the locations of the opportunities
    private void queryOpps() {
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        query.include("location");
        query.findInBackground(new FindCallback<Opportunity>() {
            @Override
            public void done(List<Opportunity> opportunities, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting likes (display likes)", e);
                    return;
                } else {

                    //getting the location for all of the opportunities
                    for (int h = 0; h < opportunities.size(); h++) {
                        oppLocation.add(opportunities.get(h).getLocation());
                    }
                    Log.i(TAG, "location of all opps: " + oppLocation);
                    apiCall(opportunities);
                }
            }
        });

    }

    //makes an api call to calculate the distance between the input location and opportunities' location
    private void apiCall(List<Opportunity> opportunities) {
        for (int k = 0; k < oppLocation.size(); k++) {
            destination = destination + "|" + oppLocation.get(k);
        }
        destination = destination.substring(1);

        AsyncHttpClient client = new AsyncHttpClient();

        //right now the destinations are hardcoded, get destinations from query
        String DISTANCE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=/" + inputLocation + "/&destinations=/" + destination + "/&key=AIzaSyDLQBSmsy3Xo2Py3swZQ6RtNt92wYwiP1U";
        client.get(DISTANCE_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    JSONArray locationData = jsonObject.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");

                    //returns the distance from origin to each destination
                    for (int j = 0; j < locationData.length(); j++) {
                        String jsonDistance = locationData.getJSONObject(j).getJSONObject("distance").getString("text");

                        //gets rid of the units at the end of the string
                        jsonDistance = jsonDistance.substring(0, jsonDistance.indexOf(" "));

                        //replaces all of the commas and then puts the opportunities below 300km in hashmap
                        if (jsonDistance.indexOf(",") != -1) {
                            jsonDistance.replaceAll(",", "");
                        } else if (Float.parseFloat(jsonDistance) < Float.parseFloat(inputRadius)) {
                            map.put(opportunities.get(j).getObjectId(), jsonDistance);
                        }
                    }
                    Log.i(TAG, "hashmap: " + map);
                    querySearch();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure- api call");
            }
        });
    }

    //filters the different conditions to receive accurate search results
    private void querySearch() {
        for (String objectId : map.keySet()) {
            ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
            query.include("opportunity");
            query.whereEqualTo("objectId", objectId);
            if (!inputAge.equals("")) {
                query.whereEqualTo("age", inputAge);
            }
            if (!inputDuration.equals("")) {
                query.whereEqualTo("duration", inputDuration);
            }
            if (!inputInterest.equals("")) {
                query.whereEqualTo("interest", inputInterest);
            }
            query.findInBackground(new FindCallback<Opportunity>() {
                @Override
                public void done(List<Opportunity> opportunities, ParseException e) {
                    // check for errors
                    if (e != null) {
                        Log.e(TAG, "Issue with getting posts", e);
                        return;
                    } else {
                        filterOpps.addAll(opportunities);

                        if(filterOpps.size() == 0){
                            //tvNoResults.setText("No Results Found");
                            tvNoResults.setVisibility(View.VISIBLE);
                        }
                        //Log.i(TAG, "filterOpps size" + filterOpps.size());
                        adapter.notifyDataSetChanged();
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }
                }
            });
        }
    }
}






















//        ///////////////////////////////////////////////////////////////////////////////////
//        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//        Log.i(TAG, "query posts");
//        query.include(Opportunity.KEY_NAME);
//        query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
//        ParseQuery<Opportunity> interest = query.whereEqualTo("interest", inputInterest);
//        Log.i(TAG, "query interest " + interest);
//
//        //this works if everything matches exactly and if something is empty
//        if (!inputLocation.equals("")) {
//            query.whereEqualTo("location", inputLocation);
//        }
//        if (!inputAge.equals("")) {
//            query.whereEqualTo("age", inputAge);
//        }
//        if (!inputDuration.equals("")) {
//            query.whereEqualTo("duration", inputDuration);
//        }
//
//        //if (!inputInterest.equals("")) {
//        if(interest == null){
//            Log.i(TAG, "inside if interest doesn't ");
//            //query.whereEqualTo("location", inputLocation);
//            //queryInterests();
//            //return;
//
//        }else{
//            //have text print that says sorry no results found
//        }
//
//        //if interests doesn't equal what you searched for call anotehr query where you query intersts and get the relted
//        //query.whereeualto("interst" input interet
//        //getRelatedFields
//        // go back to querySearch and do for loop there
//        //for loop should be for each relactedinterest. wueryWhereequal to "interest", that related fiel
//
//        query.findInBackground(new FindCallback<Opportunity>() {
//            @Override
//            public void done(List<Opportunity> opportunities, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                } else {
//                    Log.i(TAG, "size of opportunities " + opportunities.size());
//                    // for debugging purposes let's print every post description to logcat
////                    for (Opportunity opportunity : opportunities) {
////                        Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName() + opportunity.getLocation());
////                    }
//
//                    //all filtered data
//                    filterOpps.addAll(opportunities);
//                    adapter.notifyDataSetChanged();
//                    Log.i(TAG, "size of allOpps IN method " + filterOpps.size());
//
//                    //after the posts have been queried and displayed, the visibility bar should be invisible
//                    pb.setVisibility(ProgressBar.INVISIBLE);
//
//
//                }
//            }
//        });
//
//    }

//    private void queryInterests() {
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
//        query.include(Interests.KEY_OPPINTEREST);
//        //query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
//        query.whereEqualTo("oppInterest", inputInterest);
//        Log.i(TAG, "query interests: " + query);
//
//        query.getFirstInBackground(new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject interest, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting interests", e);
//                    return;
//                } else {
//
//                    List<ParseObject> relatedInterest = interest.getList("relatedInterest");
//                    Log.i(TAG, "oppInterest: " + relatedInterest);
//                    //queryRelatedFields(relatedInterest);
//
//                }
//            }
//
//        });
//    }
//
//    private void queryRelatedFields(List relatedInterest) {
//        for (Object related : relatedInterest) {
//            Log.i(TAG, "related: " +related);
//            ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//            //Log.i(TAG, "query posts");
//            query.include(Opportunity.KEY_NAME);
//            query.whereEqualTo("interest", related);
//            Log.i(TAG, "query related interest: " + query);
//
//            query.findInBackground(new FindCallback<Opportunity>() {
//                @Override
//                public void done(List<Opportunity> opportunities, ParseException e) {
//                    if (e != null) {
//                        Log.e(TAG, "Issue with getting posts", e);
//                        return;
//                    } else {
//                        Log.i(TAG, "size of opportunities " + opportunities.size());
//
//                        //all filtered data
//                        relatedOpps.addAll(opportunities);
//                        adapter.notifyDataSetChanged();
//                        Log.i(TAG, "relatedOpps " + relatedOpps);
//
//
//                    }
//                }
//            });
//        }
//    }
















