package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team5.seeshop.R;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.SeeShopUtility;

import java.util.ArrayList;
import java.util.List;

import static com.team5.seeshop.customer.CartActivity.totalAmount;

public class CheckoutActivity extends AppCompatActivity {

    Intent intent;
    List<CartModel> cartModelList;


    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;


    int cash_on_delivery=0;

    EditText address_et,city_et,postal_code_et,phone_et,card_et,exp_et,cvv_et;

    RadioButton debit_rb,cod_rb;
    LinearLayout debit_card_layout;

    List<String> seller_id_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);


//        getSupportActionBar().hide();
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);
        databaseReference = mDatabase.getReference(ConstantStrings.ORDERS).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));

        intent=getIntent();
        if (intent.hasExtra("productModelList"))
        {
            cartModelList = (List<CartModel>) intent.getSerializableExtra("productModelList");
        }

        seller_id_list =new ArrayList<>();
        address_et = findViewById(R.id.address_et);
        city_et = findViewById(R.id.city_et);
        postal_code_et = findViewById(R.id.postal_code_et);

        phone_et = findViewById(R.id.phone_et);
        card_et = findViewById(R.id.card_et);
        exp_et = findViewById(R.id.exp_et);
        cvv_et = findViewById(R.id.cvv_et);
        debit_rb = findViewById(R.id.debit_rb);
        cod_rb = findViewById(R.id.cod_rb);
        debit_card_layout = findViewById(R.id.debit_card_layout);


        for (int i=0;i<cartModelList.size();i++)
        {
            seller_id_list.add(cartModelList.get(i).getProduct_seller_id());
        }

        cod_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {cash_on_delivery=1;
                    debit_card_layout.setVisibility(View.GONE);
                }

                else cash_on_delivery=0;
            }
        });
        debit_rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    cash_on_delivery = 0;
                    debit_card_layout.setVisibility(View.VISIBLE);

                }else cash_on_delivery=1;
            }
        });


    }

    public void placeOrder(View view) throws Exception {

        if (TextUtils.isEmpty(address_et.getText().toString())
                || TextUtils.isEmpty(city_et.getText().toString())
                || TextUtils.isEmpty(postal_code_et.getText().toString())
                || TextUtils.isEmpty(phone_et.getText().toString())

        )
        {
            Toast.makeText(CheckoutActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
        }else if (phone_et.getText().toString().length()<10)
        {
            Toast.makeText(CheckoutActivity.this, "Enter correct phone number.", Toast.LENGTH_SHORT).show();

        }
        if(cash_on_delivery==0)
        {
            if (TextUtils.isEmpty(card_et.getText().toString())
                    || TextUtils.isEmpty(exp_et.getText().toString())
                    || TextUtils.isEmpty(cvv_et.getText().toString())

            )
            {
                Toast.makeText(CheckoutActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
            }else{

                if (card_et.getText().toString().length()<16)
                {
                    Toast.makeText(CheckoutActivity.this, "Enter valid card number.", Toast.LENGTH_SHORT).show();

                }else if (exp_et.getText().toString().length()<4)
                {
                    Toast.makeText(CheckoutActivity.this, "Enter valid Exp.", Toast.LENGTH_SHORT).show();

                }else if (cvv_et.getText().toString().length()<3)
                {
                    Toast.makeText(CheckoutActivity.this, "Enter valid cvv.", Toast.LENGTH_SHORT).show();

                }else {
                    cartPlaceOrder();
                }}

        }
        else
        {
            cartPlaceOrder();
        }

    }



    /*------------------------cart place order work---------------------------*/
    //Click on checkout Button
    private void cartPlaceOrder() throws Exception {


        databaseReference = mDatabase.getReference(ConstantStrings.ORDERS).child(sharedPref.getString(ConstantStrings.USER_ID,"null"));
        String _id =  databaseReference.push().getKey();

        final PlaceOrderModel placeOrderModel = new PlaceOrderModel();
        placeOrderModel.setId(_id);
        placeOrderModel.setUser_id(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        placeOrderModel.setOrder_date(SeeShopUtility.getCurrentDate());
        placeOrderModel.setOrder_id(SeeShopUtility.createOrderId());
        placeOrderModel.setCartItems(cartModelList);
        placeOrderModel.setTotal_amount(String.valueOf(totalAmount));
        placeOrderModel.setCash_on_delivery(cash_on_delivery);
        placeOrderModel.setOrder_status("in progress");
        placeOrderModel.setOrder_time(SeeShopUtility.getCurrentTime());

        //adding address details
        placeOrderModel.setAddress(address_et.getText().toString());
        placeOrderModel.setCity(city_et.getText().toString());
        placeOrderModel.setPostal_code( postal_code_et.getText().toString());
        placeOrderModel.setPhone_number(phone_et.getText().toString());
        placeOrderModel.setSeller_id_list(seller_id_list);
        placeOrderModel.setUser_name(sharedPref.getString(ConstantStrings.USER_NAME,"0"));

        if (cash_on_delivery==0)
        {
            placeOrderModel.setCard(card_et.getText().toString());
            placeOrderModel.setExp(exp_et.getText().toString());
            placeOrderModel.setCvv(cvv_et.getText().toString());
        }

        databaseReference.child(_id).setValue(placeOrderModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(CheckoutActivity.this,"Your order placed successfully !", Toast.LENGTH_SHORT).show();
                DatabaseReference databaseReference =   mDatabase.getReference(ConstantStrings.CART).child(sharedPref.getString(ConstantStrings.USER_ID,"null"));
                databaseReference.removeValue();
                Intent intent = new Intent(CheckoutActivity.this,OrderPlacedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


    }

    /*------------------------------------------------------------------------------------------------------*/

}