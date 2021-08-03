package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.findopp.models.Opportunity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class OppDetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView tvTitleDetails;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAge;
    private TextView tvCost;
    private TextView tvDuration;
    private TextView tvSupplies;
    private TextView tvContact;
    public String address;
    String currentUserLoc;
    public static final String TAG = "Opportunity Details";

    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opp_details);

        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvDescription = findViewById(R.id.tvDescription);
        tvLocation = findViewById(R.id.tvLocation);
        tvAge = findViewById(R.id.tvAge);
        tvCost = findViewById(R.id.tvCost);
        tvDuration = findViewById(R.id.tvDuration);
        tvSupplies = findViewById(R.id.tvSupplies);
        tvContact = findViewById(R.id.tvContact);
        currentUserLoc = ParseUser.getCurrentUser().getString("location");

        setTextViews();

        GeoLocation geoLocation = new GeoLocation();
        geoLocation.getAddress(tvLocation.getText().toString(), getApplicationContext(), new GeoHandler());
        geoLocation.getAddress(currentUserLoc, getApplicationContext(), new GeoHandler());

        //initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //sets the textViews for all the fields after getting an Intent
    private void setTextViews(){
        Opportunity opportunity = (Opportunity) Parcels.unwrap(getIntent().getParcelableExtra("opportunity"));
        tvTitleDetails.setText(opportunity.getTitle() + " Details");
        tvDescription.setText("Description: " + opportunity.getDescription());
        tvLocation.setText("Location: " + opportunity.getLocation());
        tvDuration.setText("Duration: " + opportunity.getDuration());
        tvContact.setText("Point of Contact: " + opportunity.getPointOfContact());

        if (opportunity.getAge() == null){
            tvAge.setText("Age: no age requirement");
        }else{
            tvAge.setText("Age: " + opportunity.getAge());
        }
        if (opportunity.getCost() == null){
            tvCost.setText("Cost: no cost");
        }else{
            tvCost.setText("Cost: " + opportunity.getCost());
        }
        if (opportunity.getSupplies().equals("") ){
            tvSupplies.setText("Supplies: no supplies necessary");
        }else{
            tvSupplies.setText("Supplies: " + opportunity.getSupplies());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.i(TAG, "made map");
    }

    private class GeoHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    Float latitude = Float.parseFloat(address.substring(0, address.indexOf("\n")));
                    Float longitude = Float.parseFloat(address.substring(address.indexOf("\n")));
                    Log.i(TAG, "address: " + address);
                    markers(latitude, longitude);

                    break;
                    default:
                        address = null;
            }
        }
    }

    //adds markers of the user's current search and the location of the opportunity
    private void markers(Float latitude, Float longitude ){
        Log.i(TAG, "inside markers method");
        LatLng userInput = new LatLng(latitude,longitude);
        map.addMarker(new MarkerOptions().position(userInput).title(tvLocation.getText().toString()));
        map.addMarker(new MarkerOptions().position(userInput).title(currentUserLoc));
        map.moveCamera(CameraUpdateFactory.newLatLng(userInput));

    }
}