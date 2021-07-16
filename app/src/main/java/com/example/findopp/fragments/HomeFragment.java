package com.example.findopp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findopp.MainActivity;
import com.example.findopp.OpeningActivity;
import com.example.findopp.OppAdapter;
import com.example.findopp.OppDetailsActivity;
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
    private TextView tvRecommendation;
    public static final String TAG = "Home Fragment";
    private RecyclerView rvOpps;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;
    Opportunity opportunity = new Opportunity();

    //private static final String KEY_LIKES = "likes";
    //private ImageView ivExit;

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
        tvRecommendation = view.findViewById(R.id.tvRecommendation);
        tvRecommendation.setText("Recommendations");
        //ivExit = view.findViewById(R.id.ivExit);
        //Glide.with(context).load(drawa).into(ivExit);

        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(getContext(), OpeningActivity.class);
                startActivity(i);
            }
        });

        if (savedInstanceState != null) {
            String savedLike = savedInstanceState.getString(opportunity.KEY_LIKES);
            opportunity.setLikes(savedLike);
            //saveView(view);

            if (opportunity.getLikes() == "false") {
                opportunity.setLikes("true");

            } else {
                Toast.makeText(getContext(), "New entry", Toast.LENGTH_SHORT).show();
            }
        }
    }

        @Override
        public void onSaveInstanceState (Bundle savedInstanceState){
            savedInstanceState.putString(opportunity.KEY_LIKES, opportunity.getLikes().toString());
            super.onSaveInstanceState(savedInstanceState);
        }


//    public void saveView(View view) {
//        if (opportunity.getLikes() == "false") {
//            //ivOpenHeart.setImageResource(R.drawable.filled_heart);
//            opportunity.setLikes("true");
//
//        }
//    }

    private void queryPosts() {
        ParseQuery<Opportunity> query = ParseQuery.getQuery(Opportunity.class);
        Log.i(TAG, "query posts");
        query.include(Opportunity.KEY_NAME);
        query.addDescendingOrder(Opportunity.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Opportunity>() {

            @Override
            public void done(List<Opportunity> opportunities, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }else{

                    Log.i(TAG, "size of opportunities " + opportunities.size());
                    // for debugging purposes let's print every post description to logcat
                    for (Opportunity opportunity : opportunities) {
                        Log.i(TAG, "Post: " + opportunity.getDescription() + ", username: " + opportunity.getName() + opportunity.getLocation());
                    }
                    //this line is trying to get opportunities to appear when u go on home screen
                    //adapter.clear();
                    allOpps.addAll(opportunities);
                    Log.i(TAG, "size of opps " + allOpps.size());
                    adapter.notifyDataSetChanged();

                }

            }
        });
    }
}

