package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.ManageProductsAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;

    List<ProductModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;
    TextView no_found_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);
        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        no_found_tv = findViewById(R.id.no_found_tv);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        productModelList=new ArrayList<>();

        getProductData();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getProductData() {


        progressBar.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);
                    no_found_tv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        ProductModel productModel2 = ds.getValue(ProductModel.class);
                        ProductModel productModel = new ProductModel();


                        /*-- show only one seller pru*/
                        if (productModel2.getSeller_id().equals(sharedPref.getString(ConstantStrings.USER_ID,"0")))
                        {


                            productModel.setTitle(productModel2.getTitle());
                            productModel.setProduct_id(productModel2.getProduct_id());
                            productModel.setPrice(productModel2.getPrice());
                            productModel.setQuantity(productModel2.getQuantity());
                            productModel.setDescription(productModel2.getDescription());
                            productModel.setProduct_enable(productModel2.getProduct_enable());
                            productModel.setImages(productModel2.getImages());
                            productModel.setSeller_id(productModel2.getSeller_id());
                            productModel.setBrand(productModel2.getBrand());
                            productModelList.add(productModel);

                            ManageProductsAdapter adapter = new ManageProductsAdapter(productModelList, ManageProductsActivity.this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ManageProductsActivity.this));
                            recyclerView.setAdapter(adapter);



                        }
                    }







                }else {
                    no_found_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });




    }
}