package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.SellerOrderCartListAdapter;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class SellerOrderDetailsActivity extends AppCompatActivity {

    PlaceOrderModel placeOrderModel;

    TextView order_id_tv,price_tv,date_time_tv,user_id_tv,phone_tv,address_tv;

    List<CartModel> cartModelList;

    RecyclerView recyclerView;

    String orderStatus;
    Button update_status_btn;

    TextView status_check_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_details);

        if (getIntent().hasExtra("order_details"))
        {
            placeOrderModel= (PlaceOrderModel) getIntent().getSerializableExtra("order_details");

            cartModelList=placeOrderModel.getCartItems();
        }

        order_id_tv=findViewById(R.id.order_id_tv);
        price_tv=findViewById(R.id.price_tv);
        date_time_tv=findViewById(R.id.date_time_tv);
//        user_id_tv=findViewById(R.id.user_id_tv);
        phone_tv=findViewById(R.id.phone_tv);
        address_tv=findViewById(R.id.address_tv);
        update_status_btn=findViewById(R.id.update_status_btn);
        status_check_tv=findViewById(R.id.status_check_tv);


        recyclerView=findViewById(R.id.recyclerView);

        /*---set the data---*/
        order_id_tv.setText("Order id : #"+ placeOrderModel.getOrder_id());
        price_tv.setText("Total Amount: $"+ placeOrderModel.getTotal_amount());
        date_time_tv.setText("Date : "+ placeOrderModel.getOrder_date() + " , "+ placeOrderModel.getOrder_time());
//        user_id_tv.setText("User id : "+ placeOrderModel.getUser_id());
        phone_tv.setText("Contact Number : "+ placeOrderModel.getPhone_number());
        address_tv.setText("Address : #"+ placeOrderModel.getAddress() + " , " +placeOrderModel.getCity()+ " \n " + placeOrderModel.getPostal_code());



        if(placeOrderModel.getOrder_status().equals("in progress")){

            orderStatus="shipping";
            status_check_tv.setText("You can update the status to " +orderStatus);
        }
        else if(placeOrderModel.getOrder_status().equals("shipping")){
            orderStatus="delivered";
            status_check_tv.setText("You can update the status to " +orderStatus);
        }
        else
        {
            //delivered
            update_status_btn.setVisibility(View.INVISIBLE);
            status_check_tv.setText("Your order successfully delivered.");

        }



        SellerOrderCartListAdapter adapter = new SellerOrderCartListAdapter(cartModelList, SellerOrderDetailsActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SellerOrderDetailsActivity.this));
        recyclerView.setAdapter(adapter);
    }

    public void updateStatus(View view) {
        updateStatusInFirebase();
    }

    private void updateStatusInFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference(ConstantStrings.ORDERS).child(placeOrderModel.getUser_id())
                .child(placeOrderModel.getId());

        mDatabaseRef.child("order_status").setValue(orderStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(orderStatus.equals("in progress")){

                    orderStatus="shipping";
                    status_check_tv.setText("You can update the status to " +orderStatus);
                }else if (orderStatus.equalsIgnoreCase("shipping"))
                {
                    orderStatus="delivered";
                    status_check_tv.setText("You can update the status to " +orderStatus);
                }else
                {
                    update_status_btn.setVisibility(View.INVISIBLE);
                    status_check_tv.setText("Your order successfully delivered.");

                }
                Toast.makeText(SellerOrderDetailsActivity.this, "order status updated!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}