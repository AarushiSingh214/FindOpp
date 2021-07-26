package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findopp.models.Interests;
import com.example.findopp.models.Opportunity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private RecyclerView rvSearchResults;
    private TextView tvSearch;
    private OppAdapter adapter;
    private ArrayList<Opportunity> searchOpps;
    public static final String TAG = "Search Results Activity";
    public ArrayList<Opportunity> filterOpps;
    public ArrayList<Opportunity> relatedOpps;
    String inputLocation;
    String inputAge;
    String inputDuration;
    String inputInterest;
    ProgressBar pb;

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

        filterOpps = new ArrayList<>();
        relatedOpps = new ArrayList<>();
        adapter = new OppAdapter(this, filterOpps);
        adapter = new OppAdapter(this, relatedOpps);

        rvSearchResults.setAdapter(adapter);
        rvSearchResults.setLayoutManager(new LinearLayoutManager(this));
        querySearch();
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
            queryInterests();
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
        Log.i(TAG, "query interests: " + query);
        query.include(Interests.KEY_OPPINTEREST);
        //query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
        query.whereEqualTo("oppInterest", inputInterest);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject interest, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting interests", e);
                    return;
                } else {

                    List<ParseObject> relatedInterest = interest.getList("relatedInterest");
                    Log.i(TAG, "oppInterest: " + relatedInterest);
                    queryRelatedFields(relatedInterest);

//                    String oppInterest = interest.getString("relatedInterest");
//                    Log.i(TAG, "oppInterest: " + oppInterest);

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
                        // for debugging purposes let's print every post description to logcat
//                        for (Opportunity opportunity : opportunities) {
//                            Log.i(TAG, "Opp: " + opportunity.getName());
//                        }

                        //all filtered data
                        relatedOpps.addAll(opportunities);
                        adapter.notifyDataSetChanged();
                        Log.i(TAG, "relatedOpps " + relatedOpps);


                    }
                }
            });
        }
    }
}














