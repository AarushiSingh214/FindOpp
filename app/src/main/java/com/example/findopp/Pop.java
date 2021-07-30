package com.example.findopp;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class Pop  extends Activity {

    RelativeLayout back_dim_layout;
    LottieAnimationView animation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6), (int)(height*.2));
        //animation = findViewById(R.id.progressBar);
        //animation.speed = 2.0F // How fast does the animation play
        //animation.progress = 50F // Starts the animation

        //setting background dim when showing popup
        back_dim_layout = (RelativeLayout) findViewById(R.id.bac_dim_layout);
//        back_dim_layout.setVisibility(View.VISIBLE);
//        back_dim_layout.setVisibility(View.GONE);

    }
}
