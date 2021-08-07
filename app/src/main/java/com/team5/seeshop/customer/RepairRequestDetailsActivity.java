package com.team5.seeshop.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.models.RepairModel;
import com.team5.seeshop.utils.ConstantStrings;

public class RepairRequestDetailsActivity extends AppCompatActivity {

    TextView title_tv,order_id_tv,price_tv,subject_tv,description_tv,status_tv,status_check_tv;
    LinearLayout status_layout;

    RepairModel repairModel;
    ImageView imageView;
    int intentVal;
    private String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_request_details);
        //    getSupportActionBar().hide();

        repairModel= (RepairModel) getIntent().getSerializableExtra("data");
        intentVal=getIntent().getIntExtra("intentVal",0);

        title_tv=findViewById(R.id.title_tv);
        order_id_tv=findViewById(R.id.order_id_tv);
        price_tv=findViewById(R.id.price_tv);
        subject_tv=findViewById(R.id.subject_tv);
        description_tv=findViewById(R.id.description_tv);
        status_tv=findViewById(R.id.status_tv);
        status_check_tv=findViewById(R.id.status_check_tv);
        status_layout=findViewById(R.id.status_layout);
        imageView=findViewById(R.id.imageView);

        title_tv.setText(repairModel.getProduct_name());
        order_id_tv.setText("#"+repairModel.getOrder_no());
        price_tv.setText("Price : $" +repairModel.getProduct_price());
        subject_tv.setText(repairModel.getSubject());
        description_tv.setText(repairModel.getDescription());
        status_tv.setText("Status : " +repairModel.getStatus());

        orderStatus=repairModel.getStatus();

        if (repairModel.getUpload_image()!=null) {
            Picasso.get().load(repairModel.getUpload_image()).into(imageView);
        }else {
            Picasso.get().load(repairModel.getImage()).into(imageView);

        }
        if (intentVal==2)
        {

            status_layout.setVisibility(View.VISIBLE);

           /* if(repairModel.getStatus().equals("in process")){

                orderStatus="request received";
                status_check_tv.setText("You can update the status to " +orderStatus);
            }*/
            if(repairModel.getStatus().equals("request received")){
                orderStatus="repair in process";
                status_check_tv.setText("You can update the status to " +orderStatus);
            } else if(repairModel.getStatus().equals("repair in process")){
                orderStatus="repaired";
                status_check_tv.setText("You can update the status to " +orderStatus);
            }
            else
            {

                status_layout.setVisibility(View.INVISIBLE);
                status_check_tv.setText("Your product successfully repaired.");

            }

        }


    }

    public void updateStatus(View view) {
        updateStatusInFirebase();
    }

    private void updateStatusInFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference(ConstantStrings.REPAIR_REQUEST).child(repairModel.getUser_id())
                .child(repairModel.getId());

        mDatabaseRef.child("status").setValue(orderStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

              /*  if(orderStatus.equals("in process")){

                    orderStatus="request received";
                    status_check_tv.setText("You can update the status to " +orderStatus);
                    status_tv.setText("Status : " +orderStatus);

                }
                else*/
                if(orderStatus.equals("request received")){
                    orderStatus="repair in process";
                    status_check_tv.setText("You can update the status to " +orderStatus);
                    status_tv.setText("Status : " +"repair in process");

                }
                else if(orderStatus.equals("repair in process")){
                    orderStatus="repaired";
                    status_check_tv.setText("You can update the status to " +orderStatus);
                    status_tv.setText("Status : " +"repair in process");

                }
                else
                {
                    status_tv.setText("Status : " +"repaired");
                    status_layout.setVisibility(View.INVISIBLE);
                    status_check_tv.setText("Your product successfully repaired.");

                }
                Toast.makeText(RepairRequestDetailsActivity.this, "status updated!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}