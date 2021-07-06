package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.seller.AddProductsActivity;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class ManageProductsAdapter extends RecyclerView.Adapter<ManageProductsAdapter.ViewHolder> {

    List<ProductModel> productModelList;
    Context context;

    public ManageProductsAdapter(List<ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ManageProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.manage_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageProductsAdapter.ViewHolder holder, int position) {

         holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice());

        if (productModelList.get(position).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getImages().get(0)).into(holder.image_iv);
        }
        holder.delete_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProducts(position,holder);
            }
        });


holder.edit_ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AddProductsActivity.class);
                intent.putExtra(ConstantStrings.PRODUCT_ITEM,productModelList.get(position));
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
         public TextView title_tv,price_tv;
        public ImageButton delete_ib,edit_ib;
        ImageView image_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =  itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);

            delete_ib =  itemView.findViewById(R.id.delete_ib);
            edit_ib =  itemView.findViewById(R.id.edit_ib);
        }
    }



    private void removeProducts(int position,ViewHolder viewHolder)
    {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference()
                .child(ConstantStrings.PRODUCTS)
                .child(productModelList.get(position).getSeller_id()).child(productModelList.get(position).getProduct_id());
        mDatabaseRef.removeValue();
        viewHolder.itemView.setVisibility(View.GONE);
        Toast.makeText(context, "Product has been deleted!", Toast.LENGTH_SHORT).show();
    }
}
