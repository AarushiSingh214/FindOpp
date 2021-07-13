package com.example.findopp;

import android.content.Intent;
import android.os.TestLooperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {

    public static final String TAG = "Signup Activity";
    private ImageView ivLogo;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etEmail;
    private EditText etLocation;
    private EditText etBirthday;
    private EditText etInterests;
    private Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etEmail = findViewById(R.id.etEmail);
        etLocation = findViewById(R.id.etLocation);
        etBirthday = findViewById(R.id.etBirthday);
        etInterests = findViewById(R.id.etInterests);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                // Set core properties
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.setEmail(etEmail.getText().toString());


                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong

                            if(etUsername.getText().toString().equals("")){
                                Toast.makeText(SignupActivity.this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
                                return;
                            } else if(etPassword.getText().toString().equals("")){
                                Toast.makeText(SignupActivity.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Log.e(TAG, "error sign up" + e);
                            return;
                        } else {
                            //sign up was successful
                            Log.i(TAG, "current user" + ParseUser.getCurrentUser().getUsername());
                            ParseUser.getCurrentUser().put("location", etLocation.getText().toString());
                            ParseUser.getCurrentUser().put("interests", etInterests.getText().toString());
                            ParseUser.getCurrentUser().put("year_of_birth", Integer.parseInt(etBirthday.getText().toString()));
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        // Sign up didn't succeed. Look at the ParseException
                                        // to figure out what went wrong
                                        Toast.makeText(SignupActivity.this, "saving error", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "error saving " +e);
                                        return;
                                    } else {
                                        //saving was successful
                                        Log.i(TAG, "current user" + ParseUser.getCurrentUser().getString("location"));
                                        Log.i(TAG, "current user" + ParseUser.getCurrentUser().getString("interests"));
                                        Log.i(TAG, "current user" + ParseUser.getCurrentUser().getString("birthday"));
                                        goMainActivity();
                                        Toast.makeText(SignupActivity.this, "saving successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });

            }



            private void goMainActivity() {
                Log.e(TAG, "inside of main activity");
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}


