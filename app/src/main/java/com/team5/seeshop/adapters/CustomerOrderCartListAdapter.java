package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.customer.CustomerOrderDetailsActivity;
import com.team5.seeshop.customer.SendRepairRequestActivity;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.RatingModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CustomerOrderCartListAdapter extends RecyclerView.Adapter<CustomerOrderCartListAdapter.ViewHolder> {

    List<CartModel> productModelList;
    Context context;
    SharedPreferences sharedPref;
    String order_id,seller_id;
    float rating=1;
    public CustomerOrderCartListAdapter(List<CartModel> productModelList, Context context,String order_id,String seller_id) {
        this.productModelList = productModelList;
        this.context = context;
        this.order_id = order_id;
        this.seller_id = seller_id;
    }

    @NonNull
    @Override
    public CustomerOrderCartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.customer_orders_cart_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderCartListAdapter.ViewHolder holder, int position) {

        holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice() +"/" + "item");

        holder.quantity_tv.setText("Quantity :"+productModelList.get(position).getQuantity());
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);


        if (productModelList.get(position).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getImages().get(0)).into(holder.image_iv);
        }


        if (CustomerOrderDetailsActivity.orderStatus.equals("delivered"))
        {
            holder.rating_layout.setVisibility(View.VISIBLE);
            holder.repair_btn.setVisibility(View.VISIBLE);
        }

        holder.rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(context, "rating : " + v , Toast.LENGTH_SHORT).show();
                rating=v;
            }
        });

        holder.rate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRatingInFirebase(position,holder);

            }
        });

        holder.repair_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SendRepairRequestActivity.class);
                intent.putExtra("product_data",productModelList.get(position));
                intent.putExtra("order_id", order_id);
                intent.putExtra("seller_id", seller_id);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        getratingstatus(position,holder);




    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv,price_tv,quantity_tv;
        ImageView image_iv;

        LinearLayout rating_layout;
        Button rate_button;
        MaterialRatingBar rating_bar;
        AppCompatButton repair_btn;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =   itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            quantity_tv = (TextView) itemView.findViewById(R.id.quantity_tv);
            rate_button =   itemView.findViewById(R.id.rate_button);
            rating_layout =   itemView.findViewById(R.id.rating_layout);
            rating_bar =   itemView.findViewById(R.id.rating_bar);
            repair_btn =   itemView.findViewById(R.id.repair_btn);



        }
    }


    private void updateRatingInFirebase(int pos,ViewHolder viewHolder)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference(ConstantStrings.PRODUCTS).child(productModelList.get(pos).getProduct_seller_id())
                .child(productModelList.get(pos).getProduct_id()).child("product_rating");

        String _id =  mDatabaseRef.push().getKey();

        RatingModel ratingModel= new RatingModel();
        ratingModel.setRating(rating);
        ratingModel.setName(sharedPref.getString(ConstantStrings.USER_NAME,"0"));
        ratingModel.setUser_id(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        mDatabaseRef.child(sharedPref.getString(ConstantStrings.USER_ID,"0")).setValue(ratingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                viewHolder.rating_layout.setVisibility(View.GONE);
                Toast.makeText(context, "rating updated for this product!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getratingstatus(int pos,ViewHolder holder)
    {

        if (CustomerOrderDetailsActivity.orderStatus.equals("delivered")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference(ConstantStrings.PRODUCTS).child(productModelList.get(pos).getProduct_seller_id())
                    .child(productModelList.get(pos).getProduct_id()).child("product_rating");
            mDatabaseRef.orderByKey().equalTo(sharedPref.getString(ConstantStrings.USER_ID, "0"))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                //    Toast.makeText(context, "exists", Toast.LENGTH_SHORT).show();

                                holder.rating_layout.setVisibility(View.GONE);
                            } else {
                                holder.rating_layout.setVisibility(View.VISIBLE);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }


    }




}
