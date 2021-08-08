package com.team5.seeshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.R;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.SeeShopUtility;

public class AdminLoginActivity extends AppCompatActivity {

    private EditText email_et, password_et;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference();
    SharedPreferences sharedPref;

    String loginemail, loginpassword, email, password;

    ImageView show_pass_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        getSupportActionBar().hide();

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        show_pass_btn = findViewById(R.id.show_pass_btn);


        getAdminData();

    }


    public void openDashboard(View view) {

        if (!validateEmail() | !validatePassword()) {
            return;
        }

        if (loginemail.equals(email) && loginpassword.equals(password)) {
            SharedPreferences.Editor editor = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, MODE_PRIVATE).edit();
            editor.putString(ConstantStrings.USER_ID, email);
            editor.apply();

            SeeShopUtility.startActivity(AdminLoginActivity.this, AdminDashboardActivity.class);

            finish();

        } else {
            Toast.makeText(this, "Invalid login and password", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean validateEmail() {
        loginemail = email_et.getText().toString().trim();
        if (TextUtils.isEmpty(loginemail)) {
            Toast.makeText(AdminLoginActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
            Toast.makeText(AdminLoginActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        loginpassword = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(loginpassword)) {
            Toast.makeText(AdminLoginActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    private void getAdminData() {

        databaseReference.child("admin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    email = String.valueOf(dataSnapshot.child("email").getValue());
                    password = String.valueOf(dataSnapshot.child("password").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    public void ShowHidePass(View view) {
        if (view.getId() == R.id.show_pass_btn) {

            if (password_et.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                show_pass_btn.setImageResource(R.drawable.ic_show);

                //Show Password
                password_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                password_et.setSelection(password_et.getText().length());

            } else {
                show_pass_btn.setImageResource(R.drawable.ic_hide_pass);

                //Hide Password
                password_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
                password_et.setSelection(password_et.getText().length());

            }
        }
    }
}