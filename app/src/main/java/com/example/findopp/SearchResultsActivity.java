package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
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
    private OppAdapter adapter;
    private ArrayList<Opportunity> searchOpps;
    public static final String TAG = "Search Results Activity";
    public ArrayList<Opportunity> filterOpps;
    public ArrayList<Opportunity> relatedOpps;
    private ArrayList<String> oppLocation;
    String inputLocation;
    String inputAge;
    String inputDuration;
    String inputInterest;
    String destination = "";
    HashMap<Opportunity, String> map;

    ProgressBar pb;
    String str_from,str_to;
    private List<Opportunity> allOpps;
    //public static final String DISTANCE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Cleveland,OH&destinations=Lexington,MA|Concord,MA&key=AIzaSyDLQBSmsy3Xo2Py3swZQ6RtNt92wYwiP1U";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        rvSearchResults = findViewById(R.id.rvSearchResults);
        tvSearch = findViewById(R.id.tvSearch);
        tvSearch.setText("Here's What We Found!");
        pb = (ProgressBar) findViewById(R.id.pbLoading);


        Intent intent = getIntent();
        inputLocation = intent.getStringExtra("location");
        inputAge = intent.getStringExtra("age");
        inputDuration = intent.getStringExtra("duration");
        inputInterest = intent.getStringExtra("interest");
        //Log.i(TAG, "duration " + inputDuration);

        //before the opportunities are displayed, the loading symbol should be displayed for waiting period
        pb.setVisibility(ProgressBar.VISIBLE);

        map = new HashMap<>();
        oppLocation = new ArrayList<>();
        allOpps = new ArrayList<>();
        filterOpps = new ArrayList<>();
        relatedOpps = new ArrayList<>();
        adapter = new OppAdapter(this, filterOpps);
        adapter = new OppAdapter(this, relatedOpps);

        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        querySearch();
        queryOpps();

    }

    //filters the different conditions to receive accurate search results
    public void querySearch() {
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        Log.i(TAG, "query posts");
        query.include(Opportunity.KEY_NAME);
        query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
        ParseQuery<Opportunity> interest = query.whereEqualTo("interest", inputInterest);
        Log.i(TAG, "query interest " + interest);

        //this works if everything matches exactly and if something is empty
        if (!inputLocation.equals("")) {
            query.whereEqualTo("location", inputLocation);
        }
        if (!inputAge.equals("")) {
            query.whereEqualTo("age", inputAge);
        }
        if (!inputDuration.equals("")) {
            query.whereEqualTo("duration", inputDuration);
        }

        //if (!inputInterest.equals("")) {
        if(interest == null){
            Log.i(TAG, "inside if interest doesn't ");
            //query.whereEqualTo("location", inputLocation);
            //queryInterests();
            //return;

        }else{
            //have text print that says sorry no results found
        }

        //if interests doesn't equal what you searched for call anotehr query where you query intersts and get the relted
        //query.whereeualto("interst" input interet
        //getRelatedFields
        // go back to querySearch and do for loop there
        //for loop should be for each relactedinterest. wueryWhereequal to "interest", that related fiel

        query.findInBackground(new FindCallback<Opportunity>() {
            @Override
            public void done(List<Opportunity> opportunities, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                } else {
                    Log.i(TAG, "size of opportunities " + opportunities.size());
                    // for debugging purposes let's print every post description to logcat
//                    for (Opportunity opportunity : opportunities) {
//                        Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName() + opportunity.getLocation());
//                    }

                    //all filtered data
                    filterOpps.addAll(opportunities);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "size of allOpps IN method " + filterOpps.size());

                    //after the posts have been queried and displayed, the visibility bar should be invisible
                    pb.setVisibility(ProgressBar.INVISIBLE);


                }
            }
        });

    }

    private void queryInterests() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.include(Interests.KEY_OPPINTEREST);
        //query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
        query.whereEqualTo("oppInterest", inputInterest);
        Log.i(TAG, "query interests: " + query);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject interest, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting interests", e);
                    return;
                } else {

                    List<ParseObject> relatedInterest = interest.getList("relatedInterest");
                    Log.i(TAG, "oppInterest: " + relatedInterest);
                    //queryRelatedFields(relatedInterest);

                }
            }

        });
    }

    private void queryRelatedFields(List relatedInterest) {
        for (Object related : relatedInterest) {
            Log.i(TAG, "related: " +related);
            ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
            //Log.i(TAG, "query posts");
            query.include(Opportunity.KEY_NAME);
            query.whereEqualTo("interest", related);
            Log.i(TAG, "query related interest: " + query);

            query.findInBackground(new FindCallback<Opportunity>() {
                @Override
                public void done(List<Opportunity> opportunities, ParseException e) {
                    if (e != null) {
                        Log.e(TAG, "Issue with getting posts", e);
                        return;
                    } else {
                        Log.i(TAG, "size of opportunities " + opportunities.size());

                        //all filtered data
                        relatedOpps.addAll(opportunities);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, "relatedOpps " + relatedOpps);


                    }
                }
            });
        }
    }

    private void queryOpps() {
        //parsing for all of the
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

                    //DO I NEED TO NOTIFY THE ADAPTER?????
                    //adapter.notifyDataSetChanged();

                    //getting the location for all of the opportunities
                    for (int h = 0; h < opportunities.size(); h++) {
                        oppLocation.add(opportunities.get(h).getLocation());
                    }
                    Log.i(TAG, "location of all opps: " + oppLocation);


                    apiCall();

                }
            }
        });

    }

    private void apiCall() {
        //Log.i(TAG, "location of all opps(OUTSIDE): " + oppLocation);
        for(int k = 0; k < oppLocation.size(); k++){
            destination = destination + "|" + oppLocation.get(k);
        }
        destination = destination.substring(1);
        //Log.i(TAG,"destination: " + destination);

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
                        Log.i(TAG, "jsonDistance: " + jsonDistance);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });
    }





//    private void apiCallTry1() {
//        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//        Log.i(TAG, "query" + query);
//        query.include("location");
//        query.findInBackground(new FindCallback<Opportunity>() {
//            @Override
//            public void done(List<Opportunity> opportunities, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting likes (display likes)", e);
//                    return;
//                } else {
//                    Log.i(TAG, "size of likes (display likes)" + opportunities.size());
//                    allOpps.addAll(opportunities);
//                    Log.i(TAG, "size of likes (display likes)" + allOpps.size());
//                    adapter.notifyDataSetChanged();
//
//                }
//            }
//        });
//
//        str_from = inputLocation;
//        Log.i(TAG, "before for loop");
//        for(Opportunity opp: allOpps) {
//            str_to = opp.getLocation();
//            //str_to = oppLocation.getText().toString();
//            String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + str_from + "&destinations=" + str_to + "=" + API_KEY;
//            new GeoTask(SearchResultsActivity.this).execute(url);
//            Log.i(TAG, "url: " + url);
//        }


}















