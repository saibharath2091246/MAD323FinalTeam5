package com.team5.seeshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.adapters.UserListAdapter;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {

    Intent intent;
    String user_type;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference().child(ConstantStrings.USERS_TABLE_KEY);

    List<UserModel> userModelList;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        user_type=intent.getStringExtra("user_type");

        getSupportActionBar().setTitle(user_type + " List");
        userModelList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);

        getUserData();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserData() {


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    // progressBar.setVisibility(View.GONE);

                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        UserModel userModel2 = ds.getValue(UserModel.class);
                        UserModel userModel = new UserModel();


                        if (userModel2.getUser_type().equals(user_type))
                        {
                            Log.e("dedefdededed", userModel2.getUser_name());

                            userModel.setUser_name(userModel2.getUser_name());
                            userModel.setUser_email(userModel2.getUser_email());
                            userModel.setUser_type(userModel2.getUser_type());
                            userModel.setUser_enable(userModel2.getUser_enable());

                            userModel.setUser_id(userModel2.getUser_id());
                            if (userModel2.getUser_type().equals("seller")) {
                                userModel.setUser_company(userModel2.getUser_company());
                            }

                            userModelList.add(userModel);
                            UserListAdapter adapter = new UserListAdapter(userModelList,UsersListActivity.this);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(UsersListActivity.this));
                            recyclerView.setAdapter(adapter);


                        }
                    }







                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}