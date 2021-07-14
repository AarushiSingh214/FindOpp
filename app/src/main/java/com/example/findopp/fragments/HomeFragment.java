package com.example.findopp.fragments;

import android.content.Intent;
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
import android.widget.Button;

import com.example.findopp.MainActivity;
import com.example.findopp.OpeningActivity;
import com.example.findopp.OppAdapter;
import com.example.findopp.Opportunity;
import com.example.findopp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private Button logoutButton;
    public static final String TAG = "Home Fragment";
    private RecyclerView rvOpps;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;

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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvOpps = view.findViewById(R.id.rvOpps);
        logoutButton = view.findViewById(R.id.btnLogout);
        allOpps = new ArrayList<>();
        adapter = new OppAdapter(getContext(), allOpps);

        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), OpeningActivity.class);
                startActivity(i);
            }
        });
        queryPosts();


    }
    private void queryPosts() {
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        query.include(Opportunity.KEY_NAME);
        query.findInBackground(new FindCallback<Opportunity>() {

            @Override
            public void done(List<Opportunity> opportunities, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Opportunity opportunity : opportunities) {
                    Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName());
                }
                allOpps.addAll(opportunities);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
