package com.team5.seeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team5.seeshop.seller.AddProductsActivity;
import com.team5.seeshop.seller.ManageProductsActivity;
import com.team5.seeshop.utils.ConstantStrings;

import static androidx.core.view.GravityCompat.START;

public class HomeActivity extends AppCompatActivity {

    ImageView drawerIndicator;
    public float offset;
    public boolean flipped;
    public DrawerLayout drawer;
    protected FrameLayout mDrawerLayout, actionBar;
    ImageView actionBarLogo;
    private NavigationView navigationView;
    private RelativeLayout drawerHead;
    private TextView nameTV;

    FrameLayout viewContainer;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        drawerIndicator = findViewById(R.id.drawer_indicator);
        actionBar = findViewById(R.id.actionBar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        actionBarLogo = findViewById(R.id.actionBar_logo);
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        prepareNavList();
        setUpDrawer();
        viewContainer = findViewById(R.id.studentDashboard_frame);

        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

    }


    private void setUpDrawer() {

        //HEADER
        View headerLayout = navigationView.getHeaderView(0);
        nameTV = headerLayout.findViewById(R.id.drawer_userName);
        drawerHead = headerLayout.findViewById(R.id.drawer_head);

        nameTV.setText(sharedPref.getString(ConstantStrings.USER_NAME,""));



        drawerIndicator.setImageResource(R.drawable.ic_drawer);


        drawerIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });
    }


    private void prepareNavList() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                switch (menuItem.getItemId()) {

                    case R.id.nav_home:
                        Intent dashboard = new Intent(HomeActivity.this, HomeActivity.class);
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(dashboard);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_profile:
                        Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(profile);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_add_products:
                        Intent add_products = new Intent(HomeActivity.this, AddProductsActivity.class);
                        add_products.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(add_products);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_manage_products:
                        Intent manage_products = new Intent(HomeActivity.this, ManageProductsActivity.class);
                        manage_products.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(manage_products);
                        drawer.closeDrawer(START);
                        break;


                    case R.id.nav_logout:
                        mAuth.signOut();
                        SharedPreferences preferences =getSharedPreferences(ConstantStrings.SEESHOP_PREFS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(HomeActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        drawer.closeDrawer(START);
                        break;


                }
                return false;
            }
        });
    }
}