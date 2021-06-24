package com.team5.seeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.team5.seeshop.admin.AdminLoginActivity;
import com.team5.seeshop.utils.SeeShopUtility;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getSupportActionBar().hide();

        if (FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseAuth.getInstance().signOut();

        }

    }

    public void openLogin(View view) {
        SeeShopUtility.startActivity(WelcomeActivity.this, LoginActivity.class);

    }

    public void openSignup(View view) {
        SeeShopUtility.startActivity(WelcomeActivity.this, SignUpActivity.class);

    }
    //home page login button
    public void openAdmin(View view) {
        SeeShopUtility.startActivity(WelcomeActivity.this, AdminLoginActivity.class);

    }
}