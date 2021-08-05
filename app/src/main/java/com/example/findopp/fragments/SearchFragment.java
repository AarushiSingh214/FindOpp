package com.example.findopp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findopp.MainActivity;
import com.example.findopp.OppAdapter;
import com.example.findopp.Pop;
import com.example.findopp.models.Opportunity;
import com.example.findopp.R;
import com.example.findopp.SearchResultsActivity;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchFragment extends Fragment {
    public ArrayList<Opportunity> filterOpps;
    private TextView tvChange;
    private EditText etLocation;
    private EditText etDuration;
    private EditText etAge;
    private EditText etInterests;
    private EditText etRadius;
    private Button btnSearch;
    Intent i;
    private OppAdapter adapter;
    private List<Opportunity> allOpps;
    public static final String TAG = "Search Fragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvChange = view.findViewById(R.id.tvChange);
        etLocation = view.findViewById(R.id.etLocation);
        etDuration = view.findViewById(R.id.etDuration);
        etAge = view.findViewById(R.id.etAge);
        etInterests = view.findViewById(R.id.etInterests);
        etRadius = view.findViewById(R.id.etRadius);
        btnSearch = view.findViewById(R.id.btnSearch);
        filterOpps = new ArrayList<>();
        allOpps = new ArrayList<>();
        adapter = new OppAdapter(getContext(), filterOpps);

        defaultPreferences();
        searchBtn();
    }

    //when the search button is clicked, intents are passed to the SearchResultsActivity.class
    private void searchBtn(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = etLocation.getText().toString();

                //ensures the user enters a location
                if (location.equals("")){
                    startActivity(new Intent (getContext(), Pop.class));
                    return;
                } else
                    //trying to pass allOpps array to SearchResultsActivity
                    i = new Intent(getContext(), SearchResultsActivity.class);
                    i.putExtra("location", etLocation.getText().toString());
                    i.putExtra("age", etAge.getText().toString());
                    i.putExtra("duration", etDuration.getText().toString());
                    i.putExtra("interest", etInterests.getText().toString());
                    i.putExtra("radius", etRadius.getText().toString());
                    startActivity(i);
            }
        });
    }

    //sets default preferences for the search
    private void defaultPreferences() {
        String location = ParseUser.getCurrentUser().getString("location");
        String interests = ParseUser.getCurrentUser().getString("interests");
        String duration = ParseUser.getCurrentUser().getString("duration");
        Integer yearBirth = ParseUser.getCurrentUser().getInt("year_of_birth");
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);

        //calculating year
        String age = Integer.toString(currentYear - yearBirth);
        etAge.setText(age);

        if (location != null) {
            etLocation.setText(location);
        }
        if (interests != null) {
            etInterests.setText(interests);
        }
        if (duration != null) {
            etDuration.setText(duration);
        }
    }
}






