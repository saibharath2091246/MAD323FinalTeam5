package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.team5.seeshop.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.customer.ProductDetailsActivity;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatingListAdapter extends RecyclerView.Adapter<RatingListAdapter.ViewHolder> {

    List<ProductModel> productModelList;
    Context context;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;

     int intentVal;

    public RatingListAdapter(List<ProductModel> productModelList, Context context, int intentVal) {
        this.productModelList = productModelList;
        this.context = context;
        this.intentVal = intentVal;
    }

    @NonNull
    @Override
    public com.team5.seeshop.adapters.RatingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.rating_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingListAdapter.ViewHolder holder, int position) {
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

         holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice());



       /* if (productModelList.get(position).getRatingModelList()!=null) {
            for (int i=0;i<productModelList.get(position).getRatingModelList().size();i++) {
                float total = 0;
                total += productModelList.get(position).getRatingModelList().get(i).getRating();
                float average = total / 2;
                holder.rating_bar.setRating(average);
            }
        }*/


        if (productModelList.get(position).getAverage_rating()>0)
        holder.rating_bar.setRating(productModelList.get(position).getAverage_rating());


        if (productModelList.get(position).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getImages().get(0)).into(holder.image_iv);
        }






holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra(ConstantStrings.PRODUCT_ITEM,productModelList.get(position));
                intent.putExtra("intentVal",intentVal);
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

        ImageView image_iv;
        LinearLayout layout;

        MaterialRatingBar rating_bar;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =  itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);

            layout =  itemView.findViewById(R.id.layout);
            rating_bar =  itemView.findViewById(R.id.rating_bar);


        }
    }






}
