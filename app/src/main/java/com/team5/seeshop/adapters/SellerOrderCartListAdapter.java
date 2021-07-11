package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class SellerOrderCartListAdapter extends RecyclerView.Adapter<SellerOrderCartListAdapter.ViewHolder> {

    List<CartModel> productModelList;
    Context context;
    SharedPreferences sharedPref;
    public SellerOrderCartListAdapter(List<CartModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public SellerOrderCartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.orders_cart_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerOrderCartListAdapter.ViewHolder holder, int position) {

         holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice() +"/" + "item");

        holder.quantity_tv.setText("Quantity :"+productModelList.get(position).getQuantity());
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);


        if (productModelList.get(position).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getImages().get(position)).into(holder.image_iv);
        }









    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         public TextView title_tv,price_tv,quantity_tv;
         ImageView image_iv;
         public ViewHolder(View itemView) {
            super(itemView);
             image_iv =   itemView.findViewById(R.id.image_iv);
             title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
             quantity_tv = (TextView) itemView.findViewById(R.id.quantity_tv);



        }
    }




}
