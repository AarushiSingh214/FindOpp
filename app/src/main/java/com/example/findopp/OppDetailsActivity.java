package com.example.findopp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.findopp.models.Opportunity;

import org.parceler.Parcels;

public class OppDetailsActivity extends AppCompatActivity {

    private TextView tvTitleDetails;
    private TextView tvDescription;
    private TextView tvLocation;
    private TextView tvAge;
    private TextView tvCost;
    private TextView tvDuration;
    private TextView tvSupplies;
    private TextView tvContact;

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

        setTextViews();
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
}