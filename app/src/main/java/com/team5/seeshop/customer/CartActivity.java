package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.CartAdapter;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    public static double totalAmount;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<CartModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    SharedPreferences sharedPref;
    public static TextView total_amount_tv,total_items_tv,sub_total_tv,total_price_tv;
    RelativeLayout cart_main_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().hide();
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);

        databaseReference = mDatabase.getReference(ConstantStrings.CART).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        total_amount_tv = findViewById(R.id.total_amount_tv);
        total_items_tv = findViewById(R.id.total_items_tv);
        sub_total_tv = findViewById(R.id.sub_total_tv);
        total_price_tv = findViewById(R.id.total_price_tv);
        cart_main_layout = findViewById(R.id.cart_main_layout);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();
        getCartData();

    }



    /*------------ get All Cart Products from Firebase ---------------------*/
    private void getCartData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    progressBar.setVisibility(View.GONE);
                    cart_main_layout.setVisibility(View.VISIBLE);
                    for (DataSnapshot productDs : dataSnapshot.getChildren())
                    {
                        CartModel productModel2 = productDs.getValue(CartModel.class);
                  /*  if (productModel2.getUser_id().equals(sharedPref.getString(ConstantStrings.USER_ID,"0")))
                    {*/
                        productModelList.add(productModel2);

                        CartAdapter adapter = new CartAdapter(productModelList, CartActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        recyclerView.setAdapter(adapter);
                        //}


                    }


                }
                else {
                    progressBar.setVisibility(View.GONE);
                    cart_main_layout.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "No items found.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }
    /*---------------------------------------------------------------------*/


    /*------------send data to order placed activity */
    public void checkoutOrder(View view) {
        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        intent.putExtra("productModelList", (Serializable) productModelList);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }



}