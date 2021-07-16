package com.example.findopp.fragments;

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
import android.widget.Toast;

import com.example.findopp.OppAdapter;
import com.example.findopp.Opportunity;
import com.example.findopp.R;
import com.example.findopp.User;
import com.example.findopp.UserAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private List<User> allUsers;
    //private UserAdapter adapter;

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
        //adapter = new UserAdapter(getContext(), allUsers);

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


        //maybe u need to call the profile adapter and put an if else in there, but first get it to save
        rvOpps.setAdapter(adapter);
        rvOpps.setLayoutManager(new LinearLayoutManager(getContext()));
        queryUsers();

        //Opportunity opportunity = (Opportunity) Parcels.unwrap(getContext().getParcelableExtra("opportunity"));

    }

    private void queryUsers() {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        query.include(User.KEY_USERNAME);
        query.addDescendingOrder(User.KEY_CREATED_AT);
        Log.i(TAG, "query posts");
        query.findInBackground(new FindCallback<User>() {

            @Override
            public void done(List<User> users, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting users", e);
                    return;
                }else{

                    Log.i(TAG, "size of users " + users.size());
                    // for debugging purposes let's print every post description to logcat
                    for (User user : users) {
                        Log.i(TAG,  "Username: " + user.getUserName() + user.getLocation2());
                    }
                    //this line is trying to get opportunities to appear when u go on home screen
                    //adapter.clear();
                    allUsers.addAll(users);
                    Log.i(TAG, "size of users " + allUsers.size());

                    //User currentUser = new User();
                    //User currentUser =  User.getCurrentUser();
                    tvUserName.setText(ParseUser.getCurrentUser().getUsername() + " Profile");
                    tvRealEmail.setText(ParseUser.getCurrentUser().getEmail());

                    //tvRealLoc.setText((User.getCurrentUser().getLocation2()));
                    //tvRealInterests.setText((currentUser.getInterests()));

//                    ParseObject user = new ParseObject("User");
//                    user.put("interests", "phil");
                    //tvRealInterests.setText(users.getInterests());
//                    tvRealLoc.setText(user.getLocation2());

                    //adapter.notifyDataSetChanged();

                    //User user = new User();
                    //tvUserName.setText(user.getUserName());

                }

            }
        });
    }
}
