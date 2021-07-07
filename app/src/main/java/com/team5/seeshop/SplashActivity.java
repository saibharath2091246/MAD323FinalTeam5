package com.team5.seeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.SeeShopUtility;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        SharedPreferences sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);
        //final boolean is_login  = sharedPref.getBoolean(ConstantStrings.USER_ID,false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                /*if (is_login ==false)
                {*/
                SeeShopUtility.startActivity(SplashActivity.this, WelcomeActivity.class);
                finish();
                // }


            }
        },2000);
    }
}