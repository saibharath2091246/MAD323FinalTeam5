package com.team5.seeshop.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.team5.seeshop.R;

public class MainManageOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manage_order);
        //getSupportActionBar().hide();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage all orders");
    }

    public void openSellersOrdersList(View view) {
        Intent intent = new Intent(MainManageOrderActivity.this, ManageOrdersActivity.class);
        startActivity(intent);
    }

    public void openCustomerOrdersList(View view) {
        Intent intent = new Intent(MainManageOrderActivity.this, ManageCustomerOrdersActivity.class);
        startActivity(intent);
    }
}