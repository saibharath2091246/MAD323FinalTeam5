package com.team5.seeshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.team5.seeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;

public class AdminProfileActivity extends AppCompatActivity {



    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference();
    SharedPreferences sharedPref;
    UserModel userModel;

    ProgressBar progressBar;
    EditText  email_et, password_et;
    Button update_btn;

    String loginemail, loginpassword,email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        update_btn = findViewById(R.id.update_btn);
        progressBar = findViewById(R.id.progressBar);


        getAdminData();

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateButtonClick();
            }
        });


    }

    public void updateButtonClick() {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

        updateProfile();
    }

    private boolean validateEmail() {
        loginemail = email_et.getText().toString().trim();
        if (TextUtils.isEmpty(loginemail)) {
            Toast.makeText(AdminProfileActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
            Toast.makeText(AdminProfileActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }

        else {
            return true;
        }
    }

    private boolean validatePassword() {
        loginpassword = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(loginpassword)) {
            Toast.makeText(AdminProfileActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private void getAdminData() { // showing admin data on profile page

        databaseReference.child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) { //if data is available in database it will be set to edit text fileds like below

                    email = String.valueOf(dataSnapshot.child("email").getValue());
                    password = String.valueOf(dataSnapshot.child("password").getValue());

                    email_et.setText(email);
                    password_et.setText(password);

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

        databaseReference.child("admin").child("email").setValue(loginemail);
        databaseReference.child("admin").child("password").setValue(loginpassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);

                    SharedPreferences.Editor editor = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, MODE_PRIVATE).edit();
                    editor.putString(ConstantStrings.USER_ID, loginemail);
                    editor.apply();

                    Toast.makeText(AdminProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }
            }


        });

    }
}