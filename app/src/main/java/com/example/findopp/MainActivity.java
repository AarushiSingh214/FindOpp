package com.example.findopp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button logoutButton;
    public static final String TAG = "Main Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = findViewById(R.id.btnLogout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
                Intent i = new Intent(MainActivity.this, OpeningActivity.class);
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

            }
        });
    }

}