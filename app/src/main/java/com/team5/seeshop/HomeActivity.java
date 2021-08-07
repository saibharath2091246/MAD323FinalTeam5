package com.team5.seeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.adapters.AllProductsAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.seller.AddProductsActivity;
import com.team5.seeshop.seller.ManageProductsActivity;
import com.team5.seeshop.seller.RatingListActivity;
import com.team5.seeshop.seller.SellerOrdersActivity;
import com.team5.seeshop.seller.SellerRepairRequestsActivity;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

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

    RelativeLayout viewContainer;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    SharedPreferences sharedPref;

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<ProductModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    AllProductsAdapter adapter;

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

       /* user_typeIntent=getIntent();
        if (user_typeIntent.hasExtra("user_type"))
        {
            user_type= user_typeIntent.getStringExtra("user_type");

            Menu nav_Menu = navigationView.getMenu();
            if (user_type.equals("customer"))
            {
                nav_Menu.findItem(R.id.nav_add_products).setVisible(false);
                nav_Menu.findItem(R.id.nav_manage_products).setVisible(false);
            }

        }*/

        prepareNavList();
        setUpDrawer();
        viewContainer = findViewById(R.id.sellerDashboard_frame);

        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        /*--------- Get All Products -------------------*/
        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();
        getProductData();

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

                    case R.id.nav_product_ratings:
                        Intent nav_product_ratings = new Intent(HomeActivity.this, RatingListActivity.class);
                        nav_product_ratings.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nav_product_ratings);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_manage_orders:
                        Intent nav_manage_orders = new Intent(HomeActivity.this, SellerOrdersActivity.class);
                        nav_manage_orders.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nav_manage_orders);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_repair_requests:
                        Intent nav_repair_requests = new Intent(HomeActivity.this, SellerRepairRequestsActivity.class);
                        nav_repair_requests.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(nav_repair_requests);
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



    /*------------ get All Products ---------------------*/
    private void getProductData() {


        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);



                    for (DataSnapshot productDs : dataSnapshot.getChildren())
                    {
                        ProductModel productModel2 = productDs.getValue(ProductModel.class);
                        productModelList.add(productModel2);
                        adapter = new AllProductsAdapter(productModelList, HomeActivity.this,2);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(HomeActivity.this,2));
                        recyclerView.setAdapter(adapter);
                    }





                }else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "No Product Found.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, "No Product Found.", Toast.LENGTH_SHORT).show();
            }
        });




    }






}