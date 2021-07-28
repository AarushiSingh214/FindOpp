package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findopp.models.Opportunity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView rvOpps;
    protected OppAdapter adapter;
    protected List<Opportunity> allOpps;
    public static final String TAG = "Feed Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        rvOpps = findViewById(R.id.rvOpps);
        // initialize the array that will hold posts and create a PostsAdapter
        allOpps = new ArrayList<>();
        //adapter = new OppAdapter(this, allOpps, (MainActivity) getActivity());

        // set the adapter on the recycler view
        rvOpps.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvOpps.setLayoutManager(new LinearLayoutManager(this));
        queryPosts();
    }

    private void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        // include data referred by user key
        query.include(Opportunity.KEY_TITLE);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Opportunity>() {
            @Override
            public void done(List<Opportunity> opportunities, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting opportunities", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Opportunity opportunity : opportunities) {
                    Log.i(TAG, "Opportunity: " + opportunity.getTitle());
                }

                // save received posts to list and notify adapter of new data
                allOpps.addAll(opportunities);
                adapter.notifyDataSetChanged();
            }
        });
    }

}