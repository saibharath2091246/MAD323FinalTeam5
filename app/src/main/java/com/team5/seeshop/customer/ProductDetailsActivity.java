package com.team5.seeshop.customer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.team5.seeshop.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;
import com.team5.seeshop.adapters.ProductRatingAdapter;
import com.team5.seeshop.adapters.SliderAdapter;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.models.RatingModel;
import com.team5.seeshop.utils.ConstantStrings;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductDetailsActivity extends AppCompatActivity {



    Intent intent;

    ProductModel productModel;

    TextView title_tv,price_tv,description_tv,quantity_tv;

    // EditText quantity_et;
    Button add_to_cart_btn;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef;
    SharedPreferences sharedPref;
    static boolean isAddedInCart = false;
    //  MaterialRatingBar rating_bar;

    int intentVal;

    List<RatingModel> ratingModelList;

    RecyclerView recyclerView;
    ProductRatingAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);
        title_tv = findViewById(R.id.title_tv);
        price_tv = findViewById(R.id.price_tv);
        description_tv = findViewById(R.id.description_tv);
        //  quantity_et = findViewById(R.id.quantity_et);
        add_to_cart_btn = findViewById(R.id.add_to_cart_btn);
        progressBar = findViewById(R.id.progressBar);
        quantity_tv = findViewById(R.id.quantity_tv);
        intent= getIntent();
        ratingModelList= new ArrayList<>();
        if (intent.hasExtra(ConstantStrings.PRODUCT_ITEM))
        {
            productModel = (ProductModel) intent.getSerializableExtra(ConstantStrings.PRODUCT_ITEM);

            intentVal=intent.getIntExtra("intentVal",0);


        }
        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.slider);
        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(this, productModel.getImages());
        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        //set the data
        title_tv.setText(productModel.getTitle());
        price_tv.setText("Price : $"+productModel.getPrice());
        description_tv.setText(productModel.getDescription());
        quantity_tv.setText("Quantity : "+productModel.getQuantity());
        //  rating_bar.setRating(productModel.getRating());

        if (intentVal==2)
        {
            add_to_cart_btn.setVisibility(View.INVISIBLE);
        }
        recyclerView= findViewById(R.id.recyclerView);
        /* if (productModel.getRatingModelList()!=null&&productModel.getRatingModelList().size()>0)
         {

             ratingModelList=productModel.getRatingModelList();

             getRatingData();
         }*/


        getRatingsData();

        add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProductDetailsActivity.this);
                builder1.setMessage("Do you want to add this product in cart");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                addToCartProduct();

                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        getResources().getString(R.string.no),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });




    }


    /*
     *  CART TABLE IN FIREBASE
     * */
    private void addToCartProduct() {


        mDatabaseRef=  database.getReference().child(ConstantStrings.CART).child(sharedPref.getString(ConstantStrings.USER_ID,"null"));

        mDatabaseRef.orderByChild("title").equalTo(productModel.getTitle())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Toast.makeText(ProductDetailsActivity.this, "Already Added", Toast.LENGTH_SHORT).show();
                        } else {
                            CartModel cartModel = new CartModel();
                            String cart_id =  mDatabaseRef.push().getKey();
                            cartModel.setCart_id(cart_id);
                            cartModel.setTitle(productModel.getTitle());
                            cartModel.setPrice(productModel.getPrice());
                            cartModel.setImages(productModel.getImages());

                            cartModel.setQuantity(1);
                            cartModel.setProduct_id(productModel.getProduct_id());
                            cartModel.setProduct_seller_id(productModel.getSeller_id());
                            cartModel.setUser_id(sharedPref.getString(ConstantStrings.USER_ID,"null"));
                            mDatabaseRef.child(cart_id).setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(ProductDetailsActivity.this,"Product added to cart", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("cart_intent",1);
                                    startActivity(intent);
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});
    }
    /*------------ Closed Add to cart () -----------------------------------*/


    /*------------ get All Products ---------------------*/
    private void getRatingData() {

        adapter = new ProductRatingAdapter(ratingModelList, ProductDetailsActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this));
        recyclerView.setAdapter(adapter);


    }

    private void getRatingsData() {


        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference  databaseReference=  database.getReference().child(ConstantStrings.PRODUCTS).child(productModel.getSeller_id()).child(productModel.getProduct_id()).child("product_rating");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);



                    for (DataSnapshot productDs : dataSnapshot.getChildren())
                    {

                        RatingModel ratingModel= productDs.getValue(RatingModel.class);
                        ratingModelList.add(ratingModel);
                        adapter = new ProductRatingAdapter(ratingModelList, ProductDetailsActivity.this);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this));
                        recyclerView.setAdapter(adapter);

                    }





                }else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProductDetailsActivity.this, "No Rating Found.", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductDetailsActivity.this, "No Rating Found.", Toast.LENGTH_SHORT).show();
            }
        });




    }
}