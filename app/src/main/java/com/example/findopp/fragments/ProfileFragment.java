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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.findopp.MainActivity;
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
    private TextView tvNoFav;
    public static final String TAG = "Profile Fragment";
    private RecyclerView rvOpps;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;
    ProgressBar pb;

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
        tvEmail = view.findViewById(R.id.tvEmail);
        tvRealEmail = view.findViewById(R.id.tvRealEmail);
        tvLocation2 = view.findViewById(R.id.tvLocation2);
        tvRealLoc = view.findViewById(R.id.tvRealLoc);
        tvInterests = view.findViewById(R.id.tvInterests);
        tvRealInterests = view.findViewById(R.id.tvRealInterests);
        tvBirth = view.findViewById(R.id.tvBirth);
        tvRealBirth = view.findViewById(R.id.tvRealBirth);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvNoFav = view.findViewById(R.id.tvNoFav);

        tvEmail.setText("Email");
        tvLocation2.setText("Location");
        tvInterests.setText("Interests");
        tvBirth.setText("Birth");

        rvOpps = view.findViewById(R.id.rvOpps);
        allOpps = new ArrayList<>();
        adapter = new OppAdapter(getContext(), allOpps);

        //before the opportunities are displayed, the loading symbol should be displayed for waiting period
        pb.setVisibility(ProgressBar.VISIBLE);

        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));
        displayLikes();

        try {
            getUserInfo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //uses ParseUser to get the data about the current user and display it
    private void getUserInfo() throws ParseException {
        String location = ParseUser.getCurrentUser().getString("location");
        Log.i(TAG, "user location: " + location);
        String interests = ParseUser.getCurrentUser().getString("interests");
        Integer yearBirth = ParseUser.getCurrentUser().getInt("year_of_birth");

        tvUserName.setText(ParseUser.getCurrentUser().getUsername() + " Profile");
        tvRealEmail.setText(ParseUser.getCurrentUser().getEmail());
        tvRealLoc.setText(ParseUser.getCurrentUser().getString("location"));
        tvRealInterests.setText(interests);
        tvRealBirth.setText(yearBirth.toString());
    }

    //queries the likes based on the current user
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
                    //sets tvNoFav to visible if no opportunity has been liked
                    if(likes.size() == 0){
                        Log.i(TAG, "no favorite likes should appear " + likes.size());
                        tvNoFav.setVisibility(View.VISIBLE);
                        pb.setVisibility(ProgressBar.INVISIBLE);
                    }else{
                        tvNoFav.setVisibility(View.INVISIBLE);
                        for (int i = 0; i < likes.size(); i++) {
                            allOpps.add(likes.get(i).getOpp());
                            Log.i(TAG, "size of allOpps (display likes) PROFILE" + allOpps.size());
                            adapter.notifyDataSetChanged();
                            pb.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }
                }
            }
        });
    }
}



