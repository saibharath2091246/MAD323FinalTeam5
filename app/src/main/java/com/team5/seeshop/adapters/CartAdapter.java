package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team5.seeshop.R;
import com.team5.seeshop.customer.CartActivity;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import static com.team5.seeshop.customer.CartActivity.cart_main_layout;
import static com.team5.seeshop.customer.CartActivity.no_cart_tv;
import static com.team5.seeshop.customer.CartActivity.total_items_tv;
import static com.team5.seeshop.customer.CustomerDashboardActivity.home_cart_tv;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CartModel> productModelList;
    Context context;
    SharedPreferences sharedPref;
    public CartAdapter(List<CartModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {

        holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice());

        holder.quantity_tv.setText(""+productModelList.get(position).getQuantity());
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);


        if (productModelList.size()>0)
        {
            CartActivity.total_amount_tv.setText("Total Amount : $"+getTotalAmount());
            CartActivity.sub_total_tv.setText("$"+getTotalAmount());
            CartActivity.total_price_tv.setText("$"+getTotalAmount());

            total_items_tv.setText("("+ productModelList.size() +" items )");
            home_cart_tv.setText("("+ productModelList.size());
        }else {
            no_cart_tv.setVisibility(View.VISIBLE);
            cart_main_layout.setVisibility(View.GONE);
        }
        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProduct(position,holder);
            }
        });

        holder.inc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                productModelList.get(position).setQuantity(productModelList.get(position).getQuantity() + 1);

                notifyDataSetChanged();


                holder.quantity_tv.setText(String.valueOf(productModelList.get(position).getQuantity()));
                updateUnitInFirebase(position);

                int price = Integer.valueOf(productModelList.get(position).getPrice()) * productModelList.get(position).getQuantity();

                holder.sale_price_tv.setText("$"+price);


            }
        });

        holder.dec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (productModelList.get(position).getQuantity()>=2) {
                    productModelList.get(position).setQuantity(productModelList.get(position).getQuantity() - 1);
                    notifyDataSetChanged();
                    holder.quantity_tv.setText(String.valueOf(productModelList.get(position).getQuantity()));


                    updateUnitInFirebase(position);
                    int price = Integer.valueOf(productModelList.get(position).getPrice()) * productModelList.get(position).getQuantity();
                    holder.sale_price_tv.setText("$" + price);

                }
            }
        });







    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv,price_tv,quantity_tv,sale_price_tv;
        public Button inc_btn,dec_btn,remove_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_tv);
            sale_price_tv = (TextView) itemView.findViewById(R.id.sale_price_tv);
            inc_btn =  itemView.findViewById(R.id.inc_btn);
            dec_btn =  itemView.findViewById(R.id.dec_btn);
            remove_btn =  itemView.findViewById(R.id.remove_btn);


        }
    }


    private void removeProduct(int position,ViewHolder viewHolder)
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference()
                .child(ConstantStrings.CART)
                .child(sharedPref.getString(ConstantStrings.USER_ID,"0")).child(productModelList.get(position).getCart_id());
        mDatabaseRef.removeValue();
        viewHolder.itemView.setVisibility(View.GONE);
        Toast.makeText(context, "product has been deleted!", Toast.LENGTH_SHORT).show();
        productModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productModelList.size());
        if (productModelList.size()==0)
        {
            cart_main_layout.setVisibility(View.GONE);
            no_cart_tv.setVisibility(View.VISIBLE);
        }
        home_cart_tv.setText("("+ productModelList.size());


    }

    private double getTotalAmount() {
        CartActivity.totalAmount=0;
        for (int i = 0; i < productModelList.size(); i++) {
            CartActivity.totalAmount += productModelList.get(i).getPrice()* productModelList.get(i).getQuantity();
        }
        return  CartActivity.totalAmount;
    }

    private void updateUnitInFirebase(int pos)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference(ConstantStrings.CART).child(sharedPref.getString(ConstantStrings.USER_ID,"0"))
                .child(productModelList.get(pos).getCart_id());

        mDatabaseRef.child("quantity").setValue(productModelList.get(pos).getQuantity()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }



}
