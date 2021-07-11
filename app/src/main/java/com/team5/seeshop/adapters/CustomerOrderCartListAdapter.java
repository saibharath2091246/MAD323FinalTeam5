package com.team5.seeshop.adapters;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.customer.CustomerOrderDetailsActivity;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.RatingModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class CustomerOrderCartListAdapter extends RecyclerView.Adapter<CustomerOrderCartListAdapter.ViewHolder> {

    List<CartModel> productModelList;
    Context context;
    SharedPreferences sharedPref;

    float rating=1;
    public CustomerOrderCartListAdapter(List<CartModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
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
         public ViewHolder(View itemView) {
            super(itemView);
             image_iv =   itemView.findViewById(R.id.image_iv);
             title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
             quantity_tv = (TextView) itemView.findViewById(R.id.quantity_tv);
             rate_button =   itemView.findViewById(R.id.rate_button);
             rating_layout =   itemView.findViewById(R.id.rating_layout);
             rating_bar =   itemView.findViewById(R.id.rating_bar);



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
        mDatabaseRef.child(_id).setValue(ratingModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                viewHolder.rating_layout.setVisibility(View.GONE);
                Toast.makeText(context, "rating updated for this product!", Toast.LENGTH_SHORT).show();
            }
        });
    }




}
