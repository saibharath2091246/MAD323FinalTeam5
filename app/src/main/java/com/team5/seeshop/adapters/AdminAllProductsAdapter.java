package com.team5.seeshop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class AdminAllProductsAdapter extends RecyclerView.Adapter<AdminAllProductsAdapter.ViewHolder>implements Filterable {

    List<ProductModel> productModelList;
    Context context;
    private ProductFilter productFilter;

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;


    public AdminAllProductsAdapter(List<ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdminAllProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.admin_all_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAllProductsAdapter.ViewHolder holder, int position) {
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice());

        if (productModelList.get(position).getImages() !=null && productModelList.get(position).getImages().size()>0)
        {
            Picasso.get().load(productModelList.get(position).getImages().get(0)).into(holder.image_iv);
        }


    }


    @Override
    public Filter getFilter() {
        if (productFilter == null) {
            productFilter = new ProductFilter();
        }
        return productFilter;
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv,price_tv,seller_name_tv;
        ImageView image_iv;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =  itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            seller_name_tv = (TextView) itemView.findViewById(R.id.seller_name_tv);


        }
    }


    public void filterList(List<ProductModel> filterllist) {
        productModelList = filterllist;
        notifyDataSetChanged();
    }


    private class ProductFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

//below checks the match for the cityId and adds to the filterlist
            String sellerId= constraint.toString();
            FilterResults results = new FilterResults();

            if (sellerId.length() > 0) {
                List<ProductModel> filterList = new ArrayList<ProductModel>();
                for (int i = 0; i < productModelList.size(); i++) {

                    if ( (productModelList.get(i).getSeller_id() )== sellerId) {

                        ProductModel address = productModelList.get(i);
                        filterList.add(address);
                    }
                }

                results.count = filterList.size();
                results.values = filterList;

            } else {

                results.count = productModelList.size();
                results.values = productModelList;

            }
            return results;
        }
        //Publishes the matches found, i.e., the selected cityids
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            productModelList = (ArrayList<ProductModel>)results.values;
            notifyDataSetChanged();
        }
    }
}







