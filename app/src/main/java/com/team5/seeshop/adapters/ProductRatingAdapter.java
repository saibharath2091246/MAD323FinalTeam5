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
import com.squareup.picasso.Picasso;
import com.team5.seeshop.models.RatingModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ProductRatingAdapter extends RecyclerView.Adapter<ProductRatingAdapter.ViewHolder> {

    List<RatingModel> ratingModelList;
    Context context;
    SharedPreferences sharedPref;

    public ProductRatingAdapter(List<RatingModel> ratingModelList, Context context) {
        this.ratingModelList = ratingModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductRatingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.product_rating_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRatingAdapter.ViewHolder holder, int position) {

        holder.name_tv.setText(ratingModelList.get(position).getName());

        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);


        holder.rating_bar.setRating(ratingModelList.get(position).getRating());


    }

    @Override
    public int getItemCount() {
        return ratingModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_tv;

        MaterialRatingBar rating_bar;

        public ViewHolder(View itemView) {
            super(itemView);
            name_tv = (TextView) itemView.findViewById(R.id.name_tv);

            rating_bar = itemView.findViewById(R.id.rating_bar);


        }
    }
}



