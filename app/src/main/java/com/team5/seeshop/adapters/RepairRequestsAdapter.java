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

import com.team5.seeshop.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.customer.RepairRequestDetailsActivity;
import com.team5.seeshop.models.RepairModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

import static com.team5.seeshop.customer.CustomerDashboardActivity.home_cart_tv;

public class RepairRequestsAdapter extends RecyclerView.Adapter<RepairRequestsAdapter.ViewHolder> {

    List<RepairModel> repairModelList;
    Context context;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference ;
    SharedPreferences sharedPref;

    int valOfCart=0;
    int intentVal;

    public RepairRequestsAdapter(List<RepairModel> repairModelList, Context context,int intentVal) {
        this.repairModelList = repairModelList;
        this.context = context;
        this.intentVal = intentVal;


    }

    @NonNull
    @Override
    public RepairRequestsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.repair_requests_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RepairRequestsAdapter.ViewHolder holder, int position) {
        sharedPref = context.getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        holder.title_tv.setText(repairModelList.get(position).getProduct_name());
        holder.subject_tv.setText("Subject : "+repairModelList.get(position).getSubject());
        holder.customer_tv.setText("Customer : "+repairModelList.get(position).getUser_name());



        if (repairModelList.get(position).getImage()!=null)
        {

            Picasso.get().load(repairModelList.get(position).getImage()).into(holder.image_iv);
        }







        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, RepairRequestDetailsActivity.class);
                intent.putExtra("data",repairModelList.get(position));
                intent.putExtra("intentVal",intentVal);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return repairModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_tv,customer_tv,subject_tv;
        ImageView image_iv;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            image_iv =  itemView.findViewById(R.id.image_iv);
            title_tv = (TextView) itemView.findViewById(R.id.title_tv);
            customer_tv = (TextView) itemView.findViewById(R.id.customer_tv);
            subject_tv = (TextView) itemView.findViewById(R.id.subject_tv);
            layout =  itemView.findViewById(R.id.layout);


        }
    }





}
