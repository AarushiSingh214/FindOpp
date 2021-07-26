package com.example.findopp.fragments;

import android.graphics.Path;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.findopp.OppAdapter;
import com.example.findopp.models.Likes;
import com.example.findopp.models.Opportunity;
import com.example.findopp.R;
import com.example.findopp.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<User> allUsers;

    private TextView tvUserName;
    private TextView tvEdit;
    private TextView tvEmail;
    private TextView tvRealEmail;
    private TextView tvLocation2;
    private TextView tvRealLoc;
    private TextView tvInterests;
    private TextView tvRealInterests;
    private TextView tvBirth;
    private TextView tvRealBirth;
    public static final String TAG = "Profile Fragment";
    private RecyclerView rvOpps;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;
    private String currentUserName = ParseUser.getCurrentUser().getUsername();
    private ArrayList<Opportunity> profileLikes = new ArrayList<Opportunity>();


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allUsers = new ArrayList<>();
        tvUserName = view.findViewById(R.id.tvUserName);
        tvEdit = view.findViewById(R.id.tvEdit);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRealEmail = view.findViewById(R.id.tvRealEmail);
        tvLocation2 = view.findViewById(R.id.tvLocation2);
        tvRealLoc = view.findViewById(R.id.tvRealLoc);
        tvInterests = view.findViewById(R.id.tvInterests);
        tvRealInterests = view.findViewById(R.id.tvRealInterests);
        tvBirth = view.findViewById(R.id.tvBirth);
        tvRealBirth = view.findViewById(R.id.tvRealBirth);

        tvEdit.setText("Edit");
        tvEmail.setText("Email");
        tvLocation2.setText("Location");
        tvInterests.setText("Interests");
        tvBirth.setText("Birth");

        rvOpps = view.findViewById(R.id.rvOpps);
        allOpps = new ArrayList<>();
        adapter = new OppAdapter(getContext(), allOpps);

        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));
        displayLikes();

        try {
            getUserInfo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //uses ParseUser to get the data about the current user
    private void getUserInfo() throws ParseException {
        String location = ParseUser.getCurrentUser().getString("location");
        String interests = ParseUser.getCurrentUser().getString("interests");
        Integer yearBirth = ParseUser.getCurrentUser().getInt("year_of_birth");

        tvUserName.setText(ParseUser.getCurrentUser().getUsername() + " Profile");
        tvRealEmail.setText(ParseUser.getCurrentUser().getEmail());
        tvRealLoc.setText(location);
        tvRealInterests.setText(interests);
        tvRealBirth.setText(yearBirth.toString());

    }

    //this needs to be an opportunity class
    private void displayLikes() {
        ParseQuery<Likes> query = ParseQuery.getQuery(Likes.class);

        //need to have query.include for the pointers specifically
        query.include("opportunity");
        query.include("user");
        ParseUser currentUser = ParseUser.getCurrentUser();
        query.whereEqualTo("user", currentUser);

        query.findInBackground(new FindCallback<Likes>() {
            @Override
            public void done(List<Likes> likes, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting likes (display likes)", e);
                    return;
                } else {
                    Log.i(TAG, "size of likes (display likes)" + likes.size());

                    //Opportunity opportunity = new Opportunity();
                    for (int i = 0; i < likes.size(); i++) {
                        Log.i(TAG, "oppsLikes.get(i).getObjectId() " + likes.get(i).getOpp().getClass().getSimpleName());
                        allOpps.add(likes.get(i).getOpp());
                        Log.i(TAG, "size of allOpps (display likes)" + allOpps.size());
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }
}

//    private void matchOpp(){
//        for (int i = 0; i < profileLikes.size(); i++) {
//            ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//            query.whereEqualTo("objectId", profileLikes.get(i).getObjectId());
//            query.findInBackground(new FindCallback<Opportunity>() {
//                @Override
//                public void done(List<Opportunity> opportunities, ParseException e) {
//                    // check for errors
//                    if (e != null) {
//                        Log.e(TAG, "Issue with getting likes (display likes)", e);
//                        return;
//                    } else {
//
//                        allOpps.addAll(opportunities);
//                    }
//                }
//            });
//        }





//    //all the opps show up in your favorited but u need to find a way to filter by hearts
//    private void queryPosts() {
//        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
//        Log.i(TAG, "query posts");
//        query.include(Opportunity.KEY_NAME);
//        query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
//        query.findInBackground(new FindCallback<Opportunity>() {
//
//            @Override
//            public void done(List<Opportunity> opportunities, ParseException e) {
//                // check for errors
//                if (e != null) {
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                } else {
//                    Log.i(TAG, "size of opportunities " + opportunities.size());
//                    // for debugging purposes let's print every post description to logcat
//                    for (Opportunity opportunity : opportunities) {
//                        Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName() + opportunity.getLocation());
//                    }
//
//                    profileLikes(opportunities);
//                    adapter.notifyDataSetChanged();
//                }
//
//            }
//        });
//    }
//
//    //gets liked opportunities to appear when u go on profile screen
//    private void profileLikes(List<Opportunity> opportunities) {
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
//                    Log.i(TAG, "size of likes (saveHeart)" + likes.size());
//
//                    allOpps.addAll(opportunities);
//                    //ivOpenHeart.setImageResource(R.drawable.filled_heart);
//                    //notifyDataSetChanged();
//                }
//            }
//        });
//    }
//
//    }

//        ParseUser currentUser = ParseUser.getCurrentUser();
//        ArrayList<String> likedId = (ArrayList<String>) currentUser.get("userLikes");
//        try {
//            if (!likedId.isEmpty()) {
//                for (String user : likedId) {
//                    for (Opportunity opportunity : opportunities) {
//                        if (user.equals(opportunity.getName())) {
//                            allOpps.add(opportunity);
//                        }
//                    }
//                }
//            }
//        } catch (NullPointerException e2){
//            Log.i(TAG, "nothing is liked " + e2);
//            return;
//
//        }




