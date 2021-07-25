package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findopp.models.Opportunity;
import com.parse.FindCallback;
import com.parse.ParseException;
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
        adapter = new OppAdapter(this, filterOpps);
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

        //this works if everything matches exactly and if something is empty
        if(!inputLocation.equals("")) {
            query.whereEqualTo("location", inputLocation);
        }
        if(!inputAge.equals("")) {
            query.whereEqualTo("age", inputAge);
        }
        if(!inputDuration.equals("")) {
            query.whereEqualTo("duration", inputDuration);
        }

        if(!inputInterest.equals("")) {
            query.whereEqualTo("location", inputLocation);
        }

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
                    for (Opportunity opportunity : opportunities) {
                        Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName() + opportunity.getLocation());
                    }

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

}