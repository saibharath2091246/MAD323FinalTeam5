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
import com.team5.seeshop.adapters.AdminAllOrdersAdapter;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManageCustomerOrdersActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    // List<PlaceOrderModel> placeOrderModelList;
    ProgressBar progressBar;

    RecyclerView recyclerView;

    AdminAllOrdersAdapter adapter;

    List<UserModel> customer_list;
    Spinner spinner;
    int check = 0;

    TextView no_found_tv;
    CheckBox all_cb;

    String all="a";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer_orders);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("All customer Orders");

        /*--------- Get All Products -------------------*/
        databaseReference = mDatabase.getReference(ConstantStrings.ORDERS);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        no_found_tv=findViewById(R.id.no_found_tv);
        spinner=findViewById(R.id.spinner);

        all_cb =  findViewById(R.id.all_cb);

        // placeOrderModelList=new ArrayList<>();
        customer_list=new ArrayList<>();
        getCustomerData();
        getAllOrdersData();


        all_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {

                    all="All";

                    recyclerView.setVisibility(View.VISIBLE);
                    no_found_tv.setVisibility(View.GONE);
                    // adapter.filterList(placeOrderModelList);
                    getAllOrdersData();
                    //adapter.notifyDataSetChanged();
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
                    UserModel userModel = customer_list.get(i);
                    Log.e("asssasass-> ", userModel.getUser_id());


                    getCustomerOrdersData(userModel.getUser_id());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    private void getCustomerData() {

        DatabaseReference databaseReference = mDatabase.getReference().child(ConstantStrings.USERS_TABLE_KEY);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {



                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        UserModel userModel2 = ds.getValue(UserModel.class);
                        UserModel userModel = new UserModel();


                        if (userModel2.getUser_type().equals("customer"))
                        {



                            userModel.setUser_name(userModel2.getUser_name());
                            userModel.setUser_id(userModel2.getUser_id());

                            customer_list.add(userModel);
                            ArrayAdapter<UserModel> dataAdapter = new ArrayAdapter<UserModel>(ManageCustomerOrdersActivity.this, android.R.layout.simple_spinner_item, customer_list);
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


    private void getCustomerOrdersData(String customer_id) {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child(customer_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    List<PlaceOrderModel> productModelList=new ArrayList<>();
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    no_found_tv.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        PlaceOrderModel productModel2 = ds.getValue(PlaceOrderModel.class);



                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        no_found_tv.setVisibility(View.GONE);
                        productModelList.add(productModel2);
                        Collections.reverse(productModelList);

                        AdminAllOrdersAdapter adapter = new AdminAllOrdersAdapter(productModelList, ManageCustomerOrdersActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ManageCustomerOrdersActivity.this));
                        recyclerView.setAdapter(adapter);



                    }

                } else {
                    no_found_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ManageCustomerOrdersActivity.this, "No orders found.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void getAllOrdersData() {
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<PlaceOrderModel> placeOrderModelList=new ArrayList<>();

                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    no_found_tv.setVisibility(View.GONE);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        for (DataSnapshot productDs : ds.getChildren()) {
                            PlaceOrderModel productModel2 = productDs.getValue(PlaceOrderModel.class);
                            placeOrderModelList.add(productModel2);
                            Collections.reverse(placeOrderModelList);

                            adapter = new AdminAllOrdersAdapter(placeOrderModelList, ManageCustomerOrdersActivity.this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ManageCustomerOrdersActivity.this));
                            recyclerView.setAdapter(adapter);

                        }
                    }

                } else {
                    no_found_tv.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ManageCustomerOrdersActivity.this, "No orders found.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
}