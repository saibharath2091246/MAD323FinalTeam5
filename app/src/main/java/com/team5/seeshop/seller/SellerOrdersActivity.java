package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.SellerOrdersAdapter;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SellerOrdersActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    // List<PlaceOrderModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    SharedPreferences sharedPref;

    TextView no_found_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);

        databaseReference = mDatabase.getReference().child(ConstantStrings.ORDERS);
        no_found_tv = findViewById(R.id.no_found_tv);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        // productModelList=new ArrayList<>();
        getOrdersData();
    }

    /*------------ get All Orders from Firebase ---------------------*/
    private void getOrdersData() {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<PlaceOrderModel> productModelList=new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    no_found_tv.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        for (DataSnapshot productDs : ds.getChildren()) {
                            PlaceOrderModel productModel2 = productDs.getValue(PlaceOrderModel.class);


                            Log.e("sellerorderer",productModel2.getOrder_id());
                            List<String> seller_id = productModel2.getSeller_id_list();
                            if (seller_id.contains(sharedPref.getString(ConstantStrings.USER_ID,"0"))) {
                                productModelList.add(productModel2);
                                Collections.reverse(productModelList);

                                SellerOrdersAdapter adapter = new SellerOrdersAdapter(productModelList, SellerOrdersActivity.this);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SellerOrdersActivity.this));
                                recyclerView.setAdapter(adapter);
                            }
                        }
                    }

                } else {
                    no_found_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SellerOrdersActivity.this, "No orders found.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    /*---------------------------------------------------------------------*/
}