package com.example.findopp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

public class Pop  extends Activity {

    RelativeLayout back_dim_layout;
    LottieAnimationView animation;
    FrameLayout layout_MainMenu;
    Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_window);

        mDialog = new Dialog(this);

        mDialog.setContentView(R.layout.pop_up_window);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //layout_MainMenu = (FrameLayout) findViewById(R.id.pop_up);
        //layout_MainMenu.getForeground().setAlpha( 0);

        //getting metrics to create the size of the pop-up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.6), (int)(height*.2));

//        layout_MainMenu.getForeground().setAlpha( 220); // dim
//
//        layout_MainMenu.getForeground().setAlpha( 0); // restore




    }
}
