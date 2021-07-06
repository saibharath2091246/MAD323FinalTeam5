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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.team5.seeshop.customer.CartActivity;
import com.team5.seeshop.customer.ProductDetailsActivity;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {

    List<ProductModel> productModelList;
    Context context;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;


    public AllProductsAdapter(List<ProductModel> productModelList, Context context) {
        this.productModelList = productModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.all_products_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductsAdapter.ViewHolder holder, int position) {
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

         holder.title_tv.setText(productModelList.get(position).getTitle());
        holder.price_tv.setText("$"+productModelList.get(position).getPrice());

        if (productModelList.get(position).getImages().size()>0)
        {

            Picasso.get().load(productModelList.get(position).getImages().get(0)).into(holder.image_iv);
        }
        holder.add_to_cart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCartProduct(position);
            }
        });




holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductDetailsActivity.class);
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
        public Button add_to_cart_btn;
        ImageView image_iv;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =  itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            price_tv = (TextView) itemView.findViewById(R.id.price_tv);
            add_to_cart_btn =  itemView.findViewById(R.id.add_to_cart_btn);
            layout =  itemView.findViewById(R.id.layout);


        }
    }


    private void addToCartProduct(int i) {
        databaseReference=  mDatabase.getReference().child(ConstantStrings.CART).child(sharedPref.getString(ConstantStrings.USER_ID,"null"));
        databaseReference.orderByChild("title").equalTo(productModelList.get(i).getTitle())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){

                            Toast.makeText(context, "Already Added", Toast.LENGTH_SHORT).show();
                        } else {
                            CartModel cartModel = new CartModel();
                            String cart_id =  databaseReference.push().getKey();
                            cartModel.setCart_id(cart_id);
                            cartModel.setTitle(productModelList.get(i).getTitle());
                            cartModel.setPrice(productModelList.get(i).getPrice());
                            cartModel.setImages(productModelList.get(i).getImages());
                            cartModel.setQuantity(1);
                            cartModel.setProduct_id(productModelList.get(i).getProduct_id());
                            cartModel.setProduct_seller_id(productModelList.get(i).getSeller_id());
                            cartModel.setUser_id(sharedPref.getString(ConstantStrings.USER_ID,"null"));
                            databaseReference.child(cart_id).setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Toast.makeText(context,"Product added to cart", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, CartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("cart_intent",1);
                                    context.startActivity(intent);
                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }});
    }

     public void filterList(List<ProductModel> filterllist) {

        productModelList = filterllist;
        notifyDataSetChanged();
    }

}
