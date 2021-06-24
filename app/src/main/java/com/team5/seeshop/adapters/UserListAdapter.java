package com.team5.seeshop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team5.seeshop.R;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    List<UserModel> userList;
    Context context;

    public UserListAdapter(List<UserModel> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.user_list_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.ViewHolder holder, int position) {

         holder.name_tv.setText(userList.get(position).getUser_name());
        holder.email_tv.setText(userList.get(position).getUser_email());

        if (userList.get(position).getUser_enable()==1)
        {
            holder.disable_btn.setText("Enable");
        }

        if (userList.get(position).getUser_type().equals("seller"))
        {

            holder.company_tv.setVisibility(View.VISIBLE);
            holder.disable_btn.setVisibility(View.VISIBLE);
            holder.company_tv.setText(userList.get(position).getUser_company());

            holder.disable_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (userList.get(position).getUser_enable()==0)
                    {
                        sellerDisableEnable(position,1);

                            holder.disable_btn.setText("Enable");
                        userList.get(position).setUser_enable(1);

                    }
                    else{
                        sellerDisableEnable(position,0);
                        holder.disable_btn.setText("Disable");
                        userList.get(position).setUser_enable(0);


                    }
                }
            });
        }

        holder.del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeUser(position,holder);
            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
         public TextView name_tv,email_tv,company_tv;
        public Button del_btn,disable_btn;
        public ViewHolder(View itemView) {
            super(itemView);
             name_tv = (TextView) itemView.findViewById(R.id.name_tv);
            email_tv = (TextView) itemView.findViewById(R.id.email_tv);
            company_tv = (TextView) itemView.findViewById(R.id.company_tv);
            del_btn =  itemView.findViewById(R.id.del_btn);
            disable_btn =  itemView.findViewById(R.id.disable_btn);
        }
    }

    private void sellerDisableEnable(int pos,int val) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference()
                .child(ConstantStrings.USERS_TABLE_KEY)
                .child(userList.get(pos).getUser_id());
        mDatabaseRef.child("user_enable").setValue(val).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

              //  userList.get(pos).setUser_enable(val);
                Toast.makeText(context, "seller account updated", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeUser(int position,ViewHolder viewHolder)
    {
         FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference()
                .child(ConstantStrings.USERS_TABLE_KEY)
                .child(userList.get(position).getUser_id());
        mDatabaseRef.removeValue();
        viewHolder.itemView.setVisibility(View.GONE);
        Toast.makeText(context, "account has been deleted!", Toast.LENGTH_SHORT).show();
    }
}
