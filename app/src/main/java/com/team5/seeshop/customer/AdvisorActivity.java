package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.AllProductsAdapter;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class AdvisorActivity extends AppCompatActivity {

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;

    List<ProductModel> productModelList;
    ProgressBar progressBar;

    RelativeLayout rec_layout;
    RecyclerView recyclerView;
    SearchView search_view;
    AllProductsAdapter adapter;

    /*first quest*/
    LinearLayout one_layout,two_layout;
    RadioButton apple_rb,hp_rb,dell_rb,lenovo_rb;
    String brand_value="Apple";
    //second
    RadioButton two_rb,four_rb,eight_rb,sixteen_rb;
    int ram=2;

    /*third*/
    LinearLayout third_layout;
    RadioButton h1_rb,h2_rb,h3_rb,h4_rb;
    int hard_disk=250;

    /*fourth*/
    LinearLayout fourth_layout;
    RadioButton g1_rb,g2_rb,g3_rb,g4_rb;
    int graphic_card=1;
    int back_value=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advisor);

        getSupportActionBar().setTitle("Advisor");

        /*first quest*/
        one_layout=findViewById(R.id.one_layout);
        apple_rb=findViewById(R.id.apple_rb);
        hp_rb=findViewById(R.id.hp_rb);
        dell_rb=findViewById(R.id.dell_rb);
        lenovo_rb=findViewById(R.id.lenovo_rb);

        apple_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    brand_value="Apple";
                }
            }
        }); hp_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    brand_value="HP";
                }
            }
        });dell_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    brand_value="Dell";

                }
            }
        });lenovo_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    brand_value="Lenovo";

                }
            }
        });



        /*------2nd ques*/
        two_layout=findViewById(R.id.two_layout);
        two_rb=findViewById(R.id.two_rb);
        four_rb=findViewById(R.id.four_rb);
        eight_rb=findViewById(R.id.eight_rb);
        sixteen_rb=findViewById(R.id.sixteen_rb);

        two_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    ram=2;
                }
            }
        }); four_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    ram=4;
                }
            }
        });eight_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    ram=8;

                }
            }
        });sixteen_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    ram=16;

                }
            }
        });
        /*------3rd ques*/
        third_layout=findViewById(R.id.third_layout);
        h1_rb=findViewById(R.id.h1_rb);
        h2_rb=findViewById(R.id.h2_rb);
        h3_rb=findViewById(R.id.h3_rb);
        h4_rb=findViewById(R.id.h4_rb);

        h1_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    hard_disk=250;
                }
            }
        }); h2_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    hard_disk=500;
                }
            }
        });h3_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    hard_disk=1;
                }
            }
        });h4_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    hard_disk=2;

                }
            }
        });

        /*------4th ques*/
        fourth_layout=findViewById(R.id.fourth_layout);
        g1_rb=findViewById(R.id.g1_rb);
        g2_rb=findViewById(R.id.g2_rb);
        g3_rb=findViewById(R.id.g3_rb);
        g4_rb=findViewById(R.id.g4_rb);

        g1_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    graphic_card=1;
                    Log.e("asasasasasa",brand_value+ram+hard_disk+graphic_card);
                }
            }
        }); g2_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    graphic_card=2;
                    Log.e("asasasasasa",brand_value+ram+hard_disk+graphic_card);

                }
            }
        });g3_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    graphic_card=4;
                    Log.e("asasasasasa",brand_value+ram+hard_disk+graphic_card);

                }
            }
        });g4_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    graphic_card=6;
                    Log.e("asasasasasa",brand_value+ram+hard_disk+graphic_card);


                }
            }
        });


        /*--------- Get All Products -------------------*/
        databaseReference = mDatabase.getReference(ConstantStrings.PRODUCTS);
        progressBar = findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recyclerView);
        rec_layout=findViewById(R.id.rec_layout);
        productModelList=new ArrayList<>();


    }

    private void getProductData() {


        Log.e("aaadddd",brand_value+ram+hard_disk+graphic_card);
        rec_layout.setVisibility(View.VISIBLE);
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
                            if (productModel2.getBrand().equalsIgnoreCase(brand_value)
                                    && productModel2.getRam()==ram
                                    && productModel2.getHard_disk()==hard_disk
                                    && productModel2.getGraphic_card()==graphic_card
                            ) {
                                productModelList.add(productModel2);
                                adapter = new AllProductsAdapter(productModelList, AdvisorActivity.this, 3);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new GridLayoutManager(AdvisorActivity.this, 2));
                                recyclerView.setAdapter(adapter);

                            }

                        }


                    }
                    if (productModelList.size()==0)
                    {
                        back_value=1;
                        Toast.makeText(AdvisorActivity.this, "No product matches.", Toast.LENGTH_SHORT).show();
                    }


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                rec_layout.setVisibility(View.GONE);
            }
        });




    }

    public void goToSecondQues(View view) {

        one_layout.setVisibility(View.GONE);
        two_layout.setVisibility(View.VISIBLE);


    }

    public void goToThird(View view) {
        two_layout.setVisibility(View.GONE);
        third_layout.setVisibility(View.VISIBLE);
        //  getProductData();
    }

    public void goToFourth(View view) {
        third_layout.setVisibility(View.GONE);
        fourth_layout.setVisibility(View.VISIBLE);
    }

    public void searchProduct(View view) {
        fourth_layout.setVisibility(View.GONE);
        getProductData();
    }

    @Override
    public void onBackPressed() {
        if (back_value==1)
        {
            startActivity(getIntent());
            finish();
        }else super.onBackPressed();
    }
}