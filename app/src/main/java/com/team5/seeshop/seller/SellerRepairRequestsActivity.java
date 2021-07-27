package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.team5.seeshop.adapters.RatingListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.R;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class RatingListActivity extends AppCompatActivity {
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<ProductModel> productModelList;
    ProgressBar progressBar;
    SharedPreferences sharedPref;
    RecyclerView recyclerView;

    RatingListAdapter ratingListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_list);

        /*--------- Get All Products -------------------*/
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();
        getProductData();
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

                        if (productModel2.getRatingModelList()!=null) {
                            for (int i=0;i<productModel2.getRatingModelList().size();i++) {
                                float total = 0;
                                total += productModel2.getRatingModelList().get(i).getRating();
                                float average = total / 2;
                                //holder.rating_bar.setRating(average);
                                productModel2.setAverage_rating(average);
                            }
                        }
                        productModelList.add(productModel2);
                        ratingListAdapter = new RatingListAdapter(productModelList, RatingListActivity.this,3);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new GridLayoutManager(RatingListActivity.this,2));
                        recyclerView.setAdapter(ratingListAdapter);
                    }





                }else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RatingListActivity.this, "No Product Found.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RatingListActivity.this, "No Product Found.", Toast.LENGTH_SHORT).show();
            }
        });




    }
}