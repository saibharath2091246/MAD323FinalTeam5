package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.smarteist.autoimageslider.SliderView;
import com.team5.seeshop.LoginActivity;
import com.team5.seeshop.ProfileActivity;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.AllProductsAdapter;
import com.team5.seeshop.adapters.SliderAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.view.GravityCompat.START;

public class CustomerDashboardActivity extends AppCompatActivity {

    ImageView drawerIndicator;
    public float offset;
    public boolean flipped;
    public DrawerLayout drawer;
    protected FrameLayout mDrawerLayout, actionBar;
    ImageView actionBarLogo;
    private NavigationView navigationView;
    private RelativeLayout drawerHead;
    private TextView nameTV;
    public static TextView home_cart_tv;

    RelativeLayout viewContainer;

    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    SharedPreferences sharedPref;

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<ProductModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    SearchView search_view;
    AllProductsAdapter adapter;
    List<String> bannerImages;
    String apple="Apple";
    String all="a";
    String hp="HP";
    String dell="Dell";
    String lenovo="Lenovo";
    int apple_val=0;
    int hp_val=0;
    int dell_val=0;
    int lenovo_val=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);

        getSupportActionBar().hide();

        drawerIndicator = findViewById(R.id.drawer_indicator);
        actionBar = findViewById(R.id.actionBar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        search_view = findViewById(R.id.search_view);
        home_cart_tv = findViewById(R.id.home_cart_tv);

        actionBarLogo = findViewById(R.id.actionBar_logo);
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        prepareNavList();
        setUpDrawer();
        viewContainer = findViewById(R.id.customerDashboard_frame);

        //        Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        bannerImages = new ArrayList<>();

        bannerImages.add("https://images.unsplash.com/photo-1593642532400-2682810df593?ixid=MnwxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1950&q=80");
        bannerImages.add("https://images.unsplash.com/photo-1496181133206-80ce9b88a853?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=751&q=80");
        bannerImages.add("https://images.unsplash.com/photo-1558562805-4bf1e2a724eb?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=750&q=80");

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);
        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, bannerImages);
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();


        /*--------- Get All Products -------------------*/
        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();
        getProductData();

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String text) {
        ArrayList<ProductModel> filteredlist = new ArrayList<>();

        for (ProductModel item : productModelList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {

            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {

            adapter.filterList(filteredlist);
        }
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
                        Intent dashboard = new Intent(CustomerDashboardActivity.this, CustomerDashboardActivity.class);
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(dashboard);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_profile:
                        Intent profile = new Intent(CustomerDashboardActivity.this, ProfileActivity.class);
                        profile.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(profile);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_cart:
                        Intent cart = new Intent(CustomerDashboardActivity.this, CartActivity.class);
                        cart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(cart);
                        drawer.closeDrawer(START);
                        break;

                    case R.id.nav_my_orders:
                        Intent nav_my_orders = new Intent(getApplicationContext(), MyOrdersActivity.class);
                        nav_my_orders.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                        startActivity(nav_my_orders);
                        drawer.closeDrawer(START);
                        break;



                    case R.id.nav_logout:
                        mAuth.signOut();
                        SharedPreferences preferences =getSharedPreferences(ConstantStrings.SEESHOP_PREFS, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();
                        Toast.makeText(CustomerDashboardActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CustomerDashboardActivity.this, LoginActivity.class);
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

                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {

                        for (DataSnapshot productDs : ds.getChildren())
                        {
                            ProductModel productModel2 = productDs.getValue(ProductModel.class);
                            productModelList.add(productModel2);
                            adapter = new AllProductsAdapter(productModelList, CustomerDashboardActivity.this,1);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new GridLayoutManager(CustomerDashboardActivity.this,2));
                            recyclerView.setAdapter(adapter);
                        }

                        Log.e("sdadasda",ds.getValue().toString());

                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }


    /*- click on filter button----*/
    public void filterProducts(View view) {

        apple_val=0;
        hp_val=0;
        dell_val=0;
        lenovo_val=0;

        apple="";
        hp="";
        dell="";
        lenovo="";
        all="";

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CustomerDashboardActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_layout, null);
        dialogBuilder.setView(dialogView);

        CheckBox apple_cb = dialogView.findViewById(R.id.apple_cb);
        CheckBox dell_cb = dialogView.findViewById(R.id.dell_cb);
        CheckBox hp_cb = dialogView.findViewById(R.id.hp_cb);
        CheckBox lenovo_cb = dialogView.findViewById(R.id.lenovo_cb);
        CheckBox all_cb = dialogView.findViewById(R.id.all_cb);

        EditText min_et = dialogView.findViewById(R.id.min_et);
        EditText max_et = dialogView.findViewById(R.id.max_et);

        all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {

                    all="All";
                }else {

                    all="a";

                }
            }
        });  apple_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    apple_val=1;
                    apple="Apple";
                }else {
                    apple_val=0;
                    apple="aa";

                }
            }
        });

        dell_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    dell_val=1;
                    dell="Dell";

                }else {
                    dell_val=0;
                    dell="dd";

                }
            }
        });

        hp_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    hp_val=1;

                    hp="HP";

                }else {
                    hp_val=0;
                    hp="";

                }
            }
        });

        lenovo_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    lenovo_val=1;
                    lenovo="Lenovo";

                }else {
                    lenovo_val=0;
                    lenovo="";

                }
            }
        });


        Button apply_btn = dialogView.findViewById(R.id.apply_btn);
        AlertDialog alertDialog = dialogBuilder.create();


        apply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<ProductModel> filtered = new ArrayList<>();
                for (ProductModel productModel : productModelList) {
                    int price = productModel.getPrice();
                    if (all.contentEquals("All") )
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.filterList(productModelList);

                    } if (productModel.getBrand().contentEquals(apple) )
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        filtered.add(productModel);
                        adapter.filterList(filtered);
                        Toast.makeText(CustomerDashboardActivity.this, "Product found", Toast.LENGTH_SHORT).show();

                    }

                    else  if(productModel.getBrand().contentEquals(dell)  )
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        filtered.add(productModel);
                        adapter.filterList(filtered);
                        Toast.makeText(CustomerDashboardActivity.this, "Product found", Toast.LENGTH_SHORT).show();

                    }
                    else if(hp_val==1 && hp.contentEquals(productModel.getBrand()))
                    {
                        recyclerView.setVisibility(View.VISIBLE);
                        filtered.add(productModel);
                        adapter.filterList(filtered);
                        Toast.makeText(CustomerDashboardActivity.this, "Product found", Toast.LENGTH_SHORT).show();

                    }
                    else if (lenovo_val==1 && lenovo.contentEquals(productModel.getBrand())) {
                        recyclerView.setVisibility(View.VISIBLE);
                        filtered.add(productModel);
                        adapter.filterList(filtered);
                        Toast.makeText(CustomerDashboardActivity.this, "Product found", Toast.LENGTH_SHORT).show();

                    }
                    else if (!min_et.getText().toString().isEmpty() && !max_et.getText().toString().isEmpty()) {

                        if (price >= Integer.parseInt(min_et.getText().toString()) && price <= Integer.parseInt(max_et.getText().toString())) {
                            recyclerView.setVisibility(View.VISIBLE);
                            filtered.add(productModel);
                            adapter.filterList(filtered);

                            Toast.makeText(CustomerDashboardActivity.this, "Product found", Toast.LENGTH_SHORT).show();

                        } /*else {
                            recyclerView.setVisibility(View.GONE);
                            Toast.makeText(CustomerDashboardActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                        }*/

                    }/*else {
                             recyclerView.setVisibility(View.GONE);
                             Toast.makeText(CustomerDashboardActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                         }*/
                }

                alertDialog.dismiss();


            }
        });

        alertDialog.show();
    }

    public void openCart(View view) {
        Intent intent = new Intent(CustomerDashboardActivity.this, CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cart_intent",2);
        startActivity(intent);
    }
}