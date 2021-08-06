package com.team5.seeshop.customer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.adapters.RepairRequestsAdapter;
import com.team5.seeshop.models.RepairModel;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.R;

import java.util.ArrayList;
import java.util.List;

public class RepairRequestsActivity extends AppCompatActivity {
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<RepairModel> repairModelList;
    ProgressBar progressBar;
    SharedPreferences sharedPref;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_requests);

        getSupportActionBar().setTitle("My Repair Requests");

        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        databaseReference = mDatabase.getReference(ConstantStrings.REPAIR_REQUEST).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        repairModelList=new ArrayList<>();
        getProductData();
    }

    private void getProductData() {

        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);

                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {

                        RepairModel productModel2 = ds.getValue(RepairModel.class);
                        repairModelList.add(productModel2);
                        RepairRequestsAdapter adapter = new RepairRequestsAdapter(repairModelList, RepairRequestsActivity.this,1);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(RepairRequestsActivity.this,2));
                        recyclerView.setAdapter(adapter);


                        Log.e("sdadasda",ds.getValue().toString());

                    }


                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RepairRequestsActivity.this, "No request found.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }
}