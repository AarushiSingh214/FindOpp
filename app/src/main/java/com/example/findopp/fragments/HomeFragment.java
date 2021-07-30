package com.example.findopp.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.findopp.MainActivity;
import com.example.findopp.OpeningActivity;
import com.example.findopp.OppAdapter;
import com.example.findopp.models.Likes;
import com.example.findopp.models.Opportunity;
import com.example.findopp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

public class HomeFragment extends Fragment {

    private Button logoutButton;
    private TextView tvRecommendation;
    public static final String TAG = "Home Fragment";
    private RecyclerView rvOpps;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;
    private ArrayList<Likes> savedOpps;
    private Serializable openHeart;
    ProgressBar pb;
    public ArrayList<Likes> oppsLikes;



    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Grab your RecyclerView and the RecyclerViewFastScroller from the layout
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rvOpps);
        VerticalRecyclerViewFastScroller fastScroller = (VerticalRecyclerViewFastScroller) rootView.findViewById(R.id.fast_scroller);

        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(recyclerView);

        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        recyclerView.addOnScrollListener(fastScroller.getOnScrollListener());

        return rootView;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savedOpps = new ArrayList<Likes>();
        rvOpps = view.findViewById(R.id.rvOpps);
        logoutButton = view.findViewById(R.id.btnLogout);
        allOpps = new ArrayList<>();
        //adapter = new OppAdapter(getContext(), allOpps, (MainActivity) getActivity());
        adapter = new OppAdapter(getContext(), allOpps);
        tvRecommendation = view.findViewById(R.id.tvRecommendation);
        tvRecommendation.setText("Recommendations");
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        oppsLikes = new ArrayList<Likes>();

        //before the opportunities are displayed, the loading symbol should be displayed for waiting period
        pb.setVisibility(ProgressBar.VISIBLE);

        //opportunities are being added and displayed to recyclerview by setting adapter
        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
        //saveHeart();
        logout();
    }

    //method for the logout button
    private void logout() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), OpeningActivity.class);
                startActivity(i);
            }
        });
    }


    //queries the opportunities based on the location of the user
    private void queryPosts() {
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        Log.i(TAG, "query posts");
        query.include(Opportunity.KEY_NAME);
        query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
        String userLoc = ParseUser.getCurrentUser().getString("location");
        query.whereEqualTo("location", userLoc);
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
                    allOpps.addAll(opportunities);
                    adapter.notifyDataSetChanged();

                    //after the posts have been queried and displayed, the visibility bar should be invisible
                    pb.setVisibility(ProgressBar.INVISIBLE);

                }

            }
        });
    }

//    //gets all the opportunities that were liked by the user
//    public void saveHeart() {
//        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);
//
//        //trying to query where the user equals the current user
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        query.whereEqualTo("user", currentUser);
//
//        query.findInBackground(new FindCallback<Likes>() {
//            @Override
//            public void done(List<Likes> likes, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting likes (saveHeart)", e);
//                    return;
//                } else {
//                    Log.i(TAG, "size of likes (saveHeart MAIN)" + likes.size());
//                    oppsLikes.addAll(likes);
//                    adapter.notifyDataSetChanged();
//                    //displayLikes(opportunities);
//
//                }
//            }
//        });
//    }
}