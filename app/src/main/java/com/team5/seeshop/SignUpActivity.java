package com.team5.seeshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.team5.seeshop.models.UserModel;
import com.team5.seeshop.utils.SeeShopUtility;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG ="tag" ;
    LinearLayout company_layout,customer_layout,seller_layout;
    EditText name_et,email_et,password_et,company_et;

    boolean IS_CUSTOMER=true;
    ProgressBar signUp_progress;
    public FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    public DatabaseReference databaseReference = mDatabase.getReference().child("users");
    Button signup_btn;
    private FirebaseAuth mAuth;

    String user_type="customer";

    String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        customer_layout=findViewById(R.id.customer_layout);
        seller_layout=findViewById(R.id.seller_layout);
        company_layout=findViewById(R.id.company_layout);
        name_et=findViewById(R.id.name_et);
        email_et=findViewById(R.id.email_et);
        password_et=findViewById(R.id.password_et);
        company_et=findViewById(R.id.company_et);
        signUp_progress=findViewById(R.id.signUp_progress);
        signup_btn=findViewById(R.id.signup_btn);



        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!validateFullname() | !validateEmail() | !validatePassword()) {
                    return;
                }

                signupFirebase(name_et.getText().toString(),email_et.getText().toString(),password_et.getText().toString(),company_et.getText().toString());

            }
        });





    }

    public void openLogin(View view) {
        SeeShopUtility.startActivity(SignUpActivity.this, LoginActivity.class);

    }

    public void openSellerSignup(View view) {
        user_type="seller";
        company_layout.setVisibility(View.VISIBLE);
        customer_layout.setBackgroundColor(getResources().getColor(R.color.lightappcolor));
        seller_layout.setBackgroundColor(getResources().getColor(R.color.app_color));

    }

    public void openCustomerSignup(View view) {
        user_type="customer";
        company_layout.setVisibility(View.GONE);
        customer_layout.setBackgroundColor(getResources().getColor(R.color.app_color));
        seller_layout.setBackgroundColor(getResources().getColor(R.color.lightappcolor));
    }


    private void signupFirebase(String name,String email,String password,String company)
    {
        UserModel userModel =new UserModel();
        userModel.setUser_name(name);
        userModel.setUser_email(email);
        userModel.setUser_password(password);
        userModel.setUser_type(user_type);
        if (user_type.equals("seller"))
        { userModel.setUser_enable(1); }
        else { userModel.setUser_enable(0);}
        if (company.length()!=0)
            userModel.setUser_company(company);
        signUp_progress.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener
                (new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            userModel.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            databaseReference
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userModel).
                                    addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            signUp_progress.setVisibility(View.GONE);
                                            Toast.makeText(SignUpActivity.this, "Signed up Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });


                        } else {
                            //    progressbar GONE
                            signUp_progress.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, "Check Email id or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private boolean validateFullname() {
        username = name_et.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(SignUpActivity.this, "Enter Your Full Name", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private boolean validateEmail() {
        email = email_et.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(SignUpActivity.this, "Please enter valid Email", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePassword() {
        password = password_et.getText().toString().trim();

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "Enter Your Password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() <= 6) {
            Toast.makeText(SignUpActivity.this, "Password is Very Short", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

}