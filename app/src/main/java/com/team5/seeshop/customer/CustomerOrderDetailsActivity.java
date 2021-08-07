package com.team5.seeshop.customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.team5.seeshop.R;
import com.team5.seeshop.adapters.CustomerOrderCartListAdapter;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.PlaceOrderModel;

import java.util.List;

public class CustomerOrderDetailsActivity extends AppCompatActivity {

    PlaceOrderModel placeOrderModel;

    TextView order_id_tv,price_tv,date_time_tv,user_id_tv,phone_tv,address_tv;

    List<CartModel> cartModelList;

    RecyclerView recyclerView;

    public static String orderStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_details);

        if (getIntent().hasExtra("order_details"))
        {
            placeOrderModel= (PlaceOrderModel) getIntent().getSerializableExtra("order_details");

            cartModelList=placeOrderModel.getCartItems();

            orderStatus=placeOrderModel.getOrder_status();
        }

        order_id_tv=findViewById(R.id.order_id_tv);
        price_tv=findViewById(R.id.price_tv);
        date_time_tv=findViewById(R.id.date_time_tv);
        user_id_tv=findViewById(R.id.user_id_tv);
        phone_tv=findViewById(R.id.phone_tv);
        address_tv=findViewById(R.id.address_tv);

        recyclerView=findViewById(R.id.recyclerView);

        /*---set the data---*/
        order_id_tv.setText("Order id : #"+ placeOrderModel.getOrder_id());
        price_tv.setText("Total Amount: $"+ placeOrderModel.getTotal_amount());
        date_time_tv.setText("Date : "+ placeOrderModel.getOrder_date() + " , "+ placeOrderModel.getOrder_time());
        //user_id_tv.setText("User Name : "+ placeOrderModel.getUser_name());
        phone_tv.setText("Contact Number : "+ placeOrderModel.getPhone_number());
        address_tv.setText("Address : #"+ placeOrderModel.getAddress() + " , " +placeOrderModel.getCity()+ " \n " + placeOrderModel.getPostal_code());






        CustomerOrderCartListAdapter adapter = new CustomerOrderCartListAdapter(cartModelList, CustomerOrderDetailsActivity.this,placeOrderModel.getOrder_id(),placeOrderModel.getSeller_id_list().get(0));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerOrderDetailsActivity.this));
        recyclerView.setAdapter(adapter);
    }


}