package com.team5.seeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.SeeShopUtility;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText email_et, password_et;
    private Button login_btn;

    ProgressBar login_progress;

    String loginemail, loginpassword;

    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference().child(ConstantStrings.USERS_TABLE_KEY);
    LinearLayout customer_layout,seller_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        email_et = findViewById(R.id.email_et);
        password_et = findViewById(R.id.password_et);
        login_progress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        seller_layout=findViewById(R.id.seller_layout);
        customer_layout=findViewById(R.id.customer_layout);

        FirebaseAuth.getInstance().signOut();


    }

    public void openSignup(View view) {
        SeeShopUtility.startActivity(LoginActivity.this, SignUpActivity.class);
    }

    public void openHome(View view) {

        if (!validateEmail() | !validatePassword()) {
            return;
        }
        //    progressbar VISIBLE
        login_progress.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(loginemail, loginpassword).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //    progressbar GONE
                        login_progress.setVisibility(View.GONE);
                        if (task.isSuccessful()) {

                            databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
                                        UserModel user = dataSnapshot.getValue(UserModel.class);

                                        if (user.getUser_enable()==0)
                                        {
                                            SharedPreferences.Editor editor = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, MODE_PRIVATE).edit();
                                            editor.putString(ConstantStrings.USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            editor.putString(ConstantStrings.USER_NAME, user.getUser_name());
                                            editor.apply();

                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            Toast.makeText(LoginActivity.this, "your account is not enabled", Toast.LENGTH_SHORT).show();
                                            mAuth.signOut();
                                            SharedPreferences preferences =getSharedPreferences(ConstantStrings.SEESHOP_PREFS, Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                        }



                                    }else {
                                        Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }

                            });




                        } else {

                            //    progressbar GONE
                            login_progress.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }


                });


    }

    private boolean validateEmail() {
        loginemail = email_et.getText().toString().trim();
        if (TextUtils.isEmpty(loginemail)) {
            Toast.makeText(LoginActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(loginemail).matches()) {
            Toast.makeText(LoginActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        loginpassword = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(loginpassword)) {
            Toast.makeText(LoginActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    public void openSellerLogin(View view) {
        customer_layout.setBackgroundColor(getResources().getColor(R.color.lightappcolor));
        seller_layout.setBackgroundColor(getResources().getColor(R.color.app_color));
        email_et.setText("");
        password_et.setText("");


    }

    public void openCustomerLogin(View view) {
        customer_layout.setBackgroundColor(getResources().getColor(R.color.app_color));
        seller_layout.setBackgroundColor(getResources().getColor(R.color.lightappcolor));
        email_et.setText("");
        password_et.setText("");
    }

}