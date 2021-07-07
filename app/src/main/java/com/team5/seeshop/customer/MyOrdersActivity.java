package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.OrdersAdapter;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class MyOrdersActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<PlaceOrderModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    SharedPreferences sharedPref;
    TextView no_found_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        getSupportActionBar().hide();
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);

        databaseReference = mDatabase.getReference(ConstantStrings.ORDERS).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        no_found_tv = findViewById(R.id.no_found_tv);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();
        getOrdersData();
    }





    /*------------ get All Orders from Firebase ---------------------*/
    private void getOrdersData() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    no_found_tv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot productDs : dataSnapshot.getChildren())
                    {
                        PlaceOrderModel productModel2 = productDs.getValue(PlaceOrderModel.class);

                        productModelList.add(productModel2);

                        OrdersAdapter adapter = new OrdersAdapter(productModelList, MyOrdersActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MyOrdersActivity.this));
                        recyclerView.setAdapter(adapter);

                    }


                } else {
                    progressBar.setVisibility(View.GONE);

                    no_found_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    // Toast.makeText(MyOrdersActivity.this, "No orders found.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }
    /*---------------------------------------------------------------------*/
}