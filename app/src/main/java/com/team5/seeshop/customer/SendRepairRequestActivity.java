package com.team5.seeshop.customer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.team5.seeshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.team5.seeshop.models.CartModel;
import com.team5.seeshop.models.RepairModel;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.ImagePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SendRepairRequestActivity extends AppCompatActivity {

    EditText subject_et,description_et;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef;
    SharedPreferences sharedPref;
    ProgressBar progressBar;

    Intent intent;
    CartModel productModel;
    String order_id,seller_id;

    private Uri filePath;
    Bitmap bitmap;

    int image_count=0;
    StorageReference storageReference;

    FirebaseStorage storage;

    Uri uploadUrl=null;

    LinearLayout image_choose_layout;
    ImageView image1_iv;
    private static final int PICK_IMAGE_ID = 101;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_repair_request);
        getSupportActionBar().hide();
        subject_et= findViewById(R.id.subject_et);
        description_et= findViewById(R.id.description_et);
        progressBar = findViewById(R.id.progressBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        }

        image_choose_layout = findViewById(R.id.image_choose_layout);
        image1_iv = findViewById(R.id.image1_iv);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (getIntent().hasExtra("product_data"))
        {
            productModel= (CartModel) getIntent().getSerializableExtra("product_data");

            order_id=getIntent().getStringExtra("order_id");
            seller_id=getIntent().getStringExtra("seller_id");
        }

        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        mDatabaseRef = database.getReference(ConstantStrings.REPAIR_REQUEST).child(sharedPref.getString(ConstantStrings.USER_ID,"0"));

        image_choose_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectGalleryOrCameraImage();


            }
        });
    }

    private void saveDataToFirebase() {

        RepairModel repairModel= new RepairModel();

        String _id =  mDatabaseRef.push().getKey();

        repairModel.setId(_id);
        repairModel.setImage(productModel.getImages().get(0));
        repairModel.setProduct_name(productModel.getTitle());
        repairModel.setUser_name(sharedPref.getString(ConstantStrings.USER_NAME,"0"));
        repairModel.setUser_id(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        repairModel.setSubject(subject_et.getText().toString());
        repairModel.setDescription(description_et.getText().toString());
        repairModel.setProduct_price(String.valueOf(productModel.getPrice()));
        repairModel.setOrder_no(order_id);
        repairModel.setStatus("request received");
        repairModel.setSeller_id(seller_id);
        if (uploadUrl!=null) {
            repairModel.setUpload_image(uploadUrl.toString());
        }
        mDatabaseRef.child(_id).setValue(repairModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(SendRepairRequestActivity.this, "Your request has been submitted. ", Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(AddProductsActivity.this, ManageProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();*/

                onBackPressed();

            }
        });



    }

    public void sendRequest(View view) {

        if ( subject_et.getText().toString().length()==0 &&  description_et.getText().toString().length()==0 )
            Toast.makeText(this, "All Fields are required", Toast.LENGTH_SHORT).show();
        else
            saveDataToFirebase();
    }

    private void selectGalleryOrCameraImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(SendRepairRequestActivity.this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                bitmap = ImagePicker.getImageFromResult(SendRepairRequestActivity.this, resultCode, data);

                if (requestCode == PICK_IMAGE_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {

                    image_count=image_count+1;
                    // Get the Uri of data
                    filePath = data.getData();

                    Log.e("fdfdsf",filePath.toString());
                    try {



                        if (bitmap!=null) {

                            image1_iv.setVisibility(View.VISIBLE);

                            uploadImage();
                            /*-- Previews Imageviews*/
                            image1_iv.setImageBitmap(bitmap);

                        }

                    }catch (Exception e)
                    {

                    }
                }

                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }



    /*--Runtime permissions -- */
    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(SendRepairRequestActivity.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(SendRepairRequestActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(SendRepairRequestActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(SendRepairRequestActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    //////////-- to upload image to Firebase Storage section ----///
    private void uploadImage()
    {
        if (filePath != null) {
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference ref = storageReference.child("uploads/"+ UUID.randomUUID().toString());


            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    progressBar.setVisibility(View.GONE);
                                    Toast
                                            .makeText(SendRepairRequestActivity.this,
                                                    "Image Loaded!!",
                                                    Toast.LENGTH_SHORT).show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            uploadUrl=uri;

                                            Log.e("uplosodsad",uploadUrl.toString());
                                            //imagesList.add(uploadUrl.toString());


                                        }


                                    });



                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            /* progressDialog.dismiss();*/

                            progressBar.setVisibility(View.GONE);
                            Toast
                                    .makeText(SendRepairRequestActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {


                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                }
                            });
        }

        filePath=null;
    }
}