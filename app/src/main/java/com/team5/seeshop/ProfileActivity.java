package com.team5.seeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

public class ProfileActivity extends AppCompatActivity {


    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference().child(ConstantStrings.USERS_TABLE_KEY);
    SharedPreferences sharedPref;
    UserModel userModel;

    ProgressBar progressBar;
    EditText name_et, email_et, company_et;
    LinearLayout company_layout;
    Button update_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();
        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        progressBar = findViewById(R.id.progressBar);
        name_et = findViewById(R.id.name_et);
        email_et = findViewById(R.id.email_et);
        company_et = findViewById(R.id.company_et);
        update_btn = findViewById(R.id.update_btn);
        company_layout = findViewById(R.id.company_layout);
        userModel = new UserModel();
        getProfileData();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


    }


    private void getProfileData() {



        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(sharedPref.getString(ConstantStrings.USER_ID, "0")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    progressBar.setVisibility(View.GONE);


                    UserModel userModel2 = dataSnapshot.getValue(UserModel.class);
                    Log.e("dedefdededed", dataSnapshot.getValue(UserModel.class).toString());


                    if (userModel2.getUser_id().equalsIgnoreCase(sharedPref.getString(ConstantStrings.USER_ID, "0"))) {
                        userModel.setUser_name(userModel2.getUser_name());
                        userModel.setUser_email(userModel2.getUser_email());
                        userModel.setUser_type(userModel2.getUser_type());
                        userModel.setUser_id(userModel2.getUser_id());
                        userModel.setUser_company(userModel2.getUser_company());


                        name_et.setText(userModel2.getUser_name());
                        email_et.setText(userModel2.getUser_email());

                        if (userModel2.getUser_type().equals("seller")) {
                            company_layout.setVisibility(View.VISIBLE);
                            company_et.setText(userModel2.getUser_company()); }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }




    private void updateProfile()
    {

        progressBar.setVisibility(View.VISIBLE);

        //databaseReference.child(userModel.getUser_id()).child("user_name").setValue(name_et.getText().toString());

        if (userModel.getUser_type().equals("seller"))
        {
            databaseReference.child(userModel.getUser_id()).child("user_company").setValue(company_et.getText().toString().trim());

        }
        databaseReference.child(userModel.getUser_id()).child("user_name").setValue(name_et.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);

                    SharedPreferences.Editor editor = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, MODE_PRIVATE).edit();
                    editor.putString(ConstantStrings.USER_NAME, name_et.getText().toString());
                    editor.apply();

                    Toast.makeText(ProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }



        });

    }

}