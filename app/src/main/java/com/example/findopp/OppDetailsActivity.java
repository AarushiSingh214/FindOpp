package com.example.findopp;

import androidx.fragment.app.FragmentActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.findopp.models.Opportunity;
import com.example.findopp.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;

public class OppDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView tvTitleDetails;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAge;
    private TextView tvCost;
    private TextView tvDuration;
    private TextView tvSupplies;
    private TextView tvContact;
    private Button btnDirections;
    public String oppAddress;
    public String address;
    String currentUserLoc;
    Polyline polyline1;
    ArrayList<LatLng> markerArrayList;
    public static final String TAG = "Opportunity Details";
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_details);

        // init MarkerArray here
        markerArrayList = new ArrayList<LatLng>();

        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvAge = findViewById(R.id.tvAge);
        tvCost = findViewById(R.id.tvCost);
        tvDuration = findViewById(R.id.tvDuration);
        tvSupplies = findViewById(R.id.tvSupplies);
        tvContact = findViewById(R.id.tvContact);
        btnDirections = findViewById(R.id.btnDirections);
        currentUserLoc = ParseUser.getCurrentUser().getString("address");

        setTextViews();
        getDirections();

        //sends the addresses of the opportunity and user to the GeoLocation class
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.getAddress(oppAddress, getApplicationContext(), new GeoHandler());
        geoLocation.getAddress(currentUserLoc, getApplicationContext(), new GeoHandler());

        //initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //sets the textViews for all the fields after getting an Intent
    private void setTextViews() {
        Opportunity opportunity = (Opportunity) Parcels.unwrap(getIntent().getParcelableExtra("opportunity"));
        tvTitleDetails.setText(opportunity.getTitle() + " Details");
        tvDescription.setText("Description: " + opportunity.getDescription());
        tvLocation.setText("Location: " + opportunity.getAddress());
        tvDuration.setText("Duration: " + opportunity.getDuration());
        tvContact.setText("Point of Contact: " + opportunity.getPointOfContact());
        oppAddress = opportunity.getAddress();

        if (opportunity.getAge() == null) {
            tvAge.setText("Age: no age requirement");
        } else {
            tvAge.setText("Age: " + opportunity.getAge());
        }
        if (opportunity.getCost() == null) {
            tvCost.setText("Cost: no cost");
        } else {
            tvCost.setText("Cost: " + opportunity.getCost());
        }
        if (opportunity.getSupplies().equals("")) {
            tvSupplies.setText("Supplies: no supplies necessary");
        } else {
            tvSupplies.setText("Supplies: " + opportunity.getSupplies());
        }
    }

    //displays the map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    //gets latitude and longitude of locations based on the msg it receives from the GeoLocation class
    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    Float latitude = Float.parseFloat(address.substring(0, address.indexOf("\n")));
                    Float longitude = Float.parseFloat(address.substring(address.indexOf("\n")));
                    markers(latitude, longitude);

                    //makes sure to draw line between user location and opportunity location after getting adding both markers
                    if (markerArrayList.size() == 2) {
                        drawPolylines();
                    }
                    break;
                default:
                    address = null;
            }
        }
    }

    //adds markers and sets titles of the user's current search and the location of the opportunity
    private void markers(Float latitude, Float longitude) {
        LatLng userInput = new LatLng(latitude, longitude);
        markerArrayList.add(userInput);
        if (markerArrayList.size() == 1) {
            map.addMarker(new MarkerOptions().position(userInput).title(oppAddress));
        } else {
            map.addMarker(new MarkerOptions().position(userInput).title(currentUserLoc));
        }

        //zooms into the map based on zoomLevel
        float zoomLevel = 7.5f; //This goes up to 21
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(userInput, zoomLevel));

    }

    //draws the lines between the two markers
    private void drawPolylines() {
        polyline1 = map.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(markerArrayList.get(0).latitude, markerArrayList.get(0).longitude),
                        new LatLng(markerArrayList.get(1).latitude, markerArrayList.get(1).longitude)));
    }

    //calls DisplayTracK after clicking on "get directions" button
    private void getDirections() {
        btnDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayTrack(currentUserLoc, oppAddress);
            }
        });
    }

    //opens google maps and displays the time it takes to travel between the two locations
    private void DisplayTrack(String currentUserLoc, String oppLoc) {
        //When google map is installed
        //initialize uri
        Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + currentUserLoc + "/" + oppLoc);
        //initialize intent with action view
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //set package
        intent.setPackage("com.google.android.apps.maps");
        //set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        startActivity(intent);
    }
}
