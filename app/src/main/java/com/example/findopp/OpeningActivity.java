package com.example.findopp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

public class OpeningActivity extends AppCompatActivity {

    public static final String TAG = "Opening Activity";
    private ImageView ivBulb;
    private Button loginBtn;
    private Button registerBtn;
    private TextView tvFindOpp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        ivBulb = findViewById(R.id.ivBulb);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        tvFindOpp = findViewById(R.id.tvFindOpp);

        userPersistence();
        loginAction();
        registerAction();
    }

    //allows user to use app without having to login each time
    private void userPersistence(){
        if (ParseUser.getCurrentUser() != null) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    //passes intent to LoginActivity.class after login button is clicked
    private void loginAction(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "inside of main activity");
                Intent i = new Intent(OpeningActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    //passes intent to SignupActivity.class after register button is clicked
    private void registerAction(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "inside of main activity");
                Intent i = new Intent(OpeningActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}