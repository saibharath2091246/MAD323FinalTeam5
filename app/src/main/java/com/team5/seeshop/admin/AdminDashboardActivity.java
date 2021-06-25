package com.team5.seeshop.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.team5.seeshop.R;


public class AdminDashboardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        getSupportActionBar().hide();

    }

    public void openSellersList(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, UsersListActivity.class);
        intent.putExtra("user_type","seller");
        startActivity(intent);
    }

    public void openCustomersList(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, UsersListActivity.class);
        intent.putExtra("user_type","customer");
        startActivity(intent);
    }
}