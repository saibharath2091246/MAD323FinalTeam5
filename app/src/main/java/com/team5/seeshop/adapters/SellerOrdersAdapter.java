package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team5.seeshop.R;
import com.team5.seeshop.models.PlaceOrderModel;
import com.team5.seeshop.seller.SellerOrderDetailsActivity;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class SellerOrdersAdapter extends RecyclerView.Adapter<SellerOrdersAdapter.ViewHolder> {

    List<PlaceOrderModel> productModelList;
    Context context;
    SharedPreferences sharedPref;
    public SellerOrdersAdapter(List<PlaceOrderModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public SellerOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.orders_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull SellerOrdersAdapter.ViewHolder holder, int position) {

       // holder.title_tv.setText(productModelList.get(position).getCartItems().get(0).getTitle());

        if (productModelList.get(position).getCartItems().size()==1)
            holder.title_tv.setText(productModelList.get(position).getCartItems().size() +" item" );
        else
            holder.title_tv.setText(productModelList.get(position).getCartItems().size() +" items" );

        holder.price_tv.setText("$"+productModelList.get(position).getTotal_amount());
        holder.order_id_tv.setText(productModelList.get(position).getOrder_id());
        holder.status_tv.setText(""+productModelList.get(position).getOrder_status());
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS,0);


       /* if (productModelList.get(position).getCartItems().get(0).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getCartItems().get(0).getImages().get(0)).into(holder.image_iv);
        }
*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SellerOrderDetailsActivity.class);
                intent.putExtra("order_details",productModelList.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         public TextView title_tv,price_tv,order_id_tv,status_tv;
         ImageView image_iv;
          public ViewHolder(View itemView) {
            super(itemView);
             title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
             order_id_tv = (TextView) itemView.findViewById(R.id.order_id_tv);
             status_tv = (TextView) itemView.findViewById(R.id.status_tv);
              image_iv =  itemView.findViewById(R.id.image_iv);


        }
    }





}
