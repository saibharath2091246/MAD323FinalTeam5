package com.team5.seeshop.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.team5.seeshop.LoginActivity;
import com.team5.seeshop.R;
import com.team5.seeshop.utils.ConstantStrings;


public class AdminDashboardActivity extends AppCompatActivity {


    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu_logout,menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

    }

    public void openSellersList(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, UsersListActivity.class);
        intent.putExtra("user_type","seller");
        startActivity(intent);
    }

    public void openCustomersList(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, UsersListActivity.class);
        intent.putExtra("user_type","customer");
        startActivity(intent);
    }

    public void openAdminProfile(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, AdminProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_admin:
                mAuth.signOut();
                SharedPreferences preferences =getSharedPreferences(ConstantStrings.SEESHOP_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(AdminDashboardActivity.this, "Logout Successful ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void openAllProducts(View view) {
        Intent intent = new Intent(AdminDashboardActivity.this, AdminAllProductsActivity.class);
        startActivity(intent);
    }
}