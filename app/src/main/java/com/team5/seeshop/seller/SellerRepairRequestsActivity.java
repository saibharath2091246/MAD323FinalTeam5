package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.team5.seeshop.adapters.RatingListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.adapters.RepairRequestsAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.R;
import com.team5.seeshop.models.RepairModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class SellerRepairRequestsActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<RepairModel> repairModelList;
    ProgressBar progressBar;
    SharedPreferences sharedPref;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_repair_requests);
        getSupportActionBar().setTitle("Repair Requests");
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        databaseReference = mDatabase.getReference(ConstantStrings.REPAIR_REQUEST);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);

        getProductData();
    }

    private void getProductData() {

        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List  repairModelList=new ArrayList<>();
                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);

                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        for (DataSnapshot ds2: ds.getChildren())
                        {
                            RepairModel productModel2 = ds2.getValue(RepairModel.class);
                            if (productModel2.getSeller_id().contentEquals(sharedPref.getString(ConstantStrings.USER_ID,"0"))) {
                                repairModelList.add(productModel2);
                                RepairRequestsAdapter adapter = new RepairRequestsAdapter(repairModelList, SellerRepairRequestsActivity.this,2);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(SellerRepairRequestsActivity.this, 2));
                                recyclerView.setAdapter(adapter);
                            }


                        }

                        Log.e("sdadasda", ds.getValue().toString());


                    }


                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SellerRepairRequestsActivity.this, "No request found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });





    }
}