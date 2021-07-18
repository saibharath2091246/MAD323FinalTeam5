package com.team5.seeshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.AdminAllProductsAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class AdminAllProductsActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<ProductModel> productModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;

    AdminAllProductsAdapter adapter;

    List<UserModel>seller_list;
    Spinner spinner;
    int check = 0;

    TextView no_found_tv;
    CheckBox all_cb;

    String all="a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_products);
        getSupportActionBar().setTitle("All Products");

        /*--------- Get All Products -------------------*/
        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        no_found_tv=findViewById(R.id.no_found_tv);
        spinner=findViewById(R.id.spinner);

        all_cb =  findViewById(R.id.all_cb);

        productModelList=new ArrayList<>();
        seller_list=new ArrayList<>();
        getSellersData();
        getProductData();

        all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {

                    all="All";

                    recyclerView.setVisibility(View.VISIBLE);
                    no_found_tv.setVisibility(View.GONE);
                    adapter.filterList(productModelList);
                    adapter.notifyDataSetChanged();
                    all_cb.setChecked(false);
                }else {

                    all="a";

                }
            }
        });

        spinner.setSelection(Adapter.NO_SELECTION, true);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(++check > 1) {
                    UserModel userModel = seller_list.get(i);
                    Log.e("asssasass-> ", userModel.getUser_id());


                    getProductBysellerData(userModel.getUser_id());

                }

                /*adapter.getFilter().filter(userModel.getUser_id(), new Filter.FilterListener() {
                    @Override
                    public void onFilterComplete(int i) {

                    }
                });*/



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getFilterData(String userid)
    {
        List<ProductModel> filterList = new ArrayList<>();
        if (productModelList.size()>0) {
            for (ProductModel productModel : productModelList) {

                String id=productModel.getSeller_id();

                Log.e("idididididiid", productModel.getSeller_id());
                if (id.equals(userid)) {
                    recyclerView.setVisibility(View.VISIBLE);
                    filterList.add(productModel);
                    adapter.filterList(filterList);
                    adapter.notifyDataSetChanged();
                    break;
                }else {
                    recyclerView.setVisibility(View.GONE);
                    Toast.makeText(this, "No product found.", Toast.LENGTH_SHORT).show();
                }


            }
        }
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
                            adapter = new AdminAllProductsAdapter(productModelList, AdminAllProductsActivity.this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(AdminAllProductsActivity.this));
                            recyclerView.setAdapter(adapter);
                        }


                    }


                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AdminAllProductsActivity.this, "No products Found.", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.GONE);


                }

                all_cb.setChecked(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminAllProductsActivity.this, "No products Found.", Toast.LENGTH_SHORT).show();

            }
        });




    }



    private void getProductBysellerData(String sellerid) {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS).child(sellerid);

        Log.e("sellelrlerlel",sellerid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

/*
                   if (productModelList.size()>0)productModelList.clear();
*/

                    List<ProductModel>productModelList1=new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    no_found_tv.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);



                    for (DataSnapshot productDs : dataSnapshot.getChildren())
                    {

                        ProductModel productModel2 = productDs.getValue(ProductModel.class);
                        productModelList1.add(productModel2);
                        adapter = new AdminAllProductsAdapter(productModelList1, AdminAllProductsActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AdminAllProductsActivity.this));
                        recyclerView.setAdapter(adapter);
                    }





                }else {
                    progressBar.setVisibility(View.GONE);

                    recyclerView.setVisibility(View.GONE);
                    no_found_tv.setVisibility(View.VISIBLE);

                    // Toast.makeText(AdminAllProductsActivity.this, "No products Found.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                no_found_tv.setVisibility(View.VISIBLE);
            }
        });




    }

    private void getSellersData() {

        DatabaseReference databaseReference = mDatabase.getReference().child(ConstantStrings.USERS_TABLE_KEY);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {



                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        UserModel userModel2 = ds.getValue(UserModel.class);
                        UserModel userModel = new UserModel();


                        if (userModel2.getUser_type().equals("seller"))
                        {



                            userModel.setUser_name(userModel2.getUser_name());
                            userModel.setUser_id(userModel2.getUser_id());

                            seller_list.add(userModel);
                            ArrayAdapter<UserModel> dataAdapter = new ArrayAdapter<UserModel>(AdminAllProductsActivity.this, android.R.layout.simple_spinner_item, seller_list);
                            spinner.setAdapter(dataAdapter);


                        }
                    }







                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}