package com.example.findopp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.findopp.Opportunity;
import com.example.findopp.R;

import org.parceler.Parcels;

public class SearchFragment extends Fragment {
    private TextView tvChange;
    private EditText etLocation;
    private EditText etDuration;
    private EditText etAge;
    private EditText etInterests;
    private Button btnSearch;

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
        btnSearch = view.findViewById(R.id.btnSearch);

        //Opportunity opportunity = (Opportunity) Parcels.unwrap(getContext().getParcelableExtra("opportunity"));
        //HOW DO U GET INFO FROM USER/OPPORTUNITY
    }
}