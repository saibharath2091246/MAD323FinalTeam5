package com.team5.seeshop.seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SwitchCompat;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;
import com.team5.seeshop.R;
import com.team5.seeshop.models.ProductModel;
import com.team5.seeshop.utils.ConstantStrings;
import com.team5.seeshop.utils.ImagePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddProductsActivity extends AppCompatActivity {


    private static final int PICK_IMAGE_ID = 101;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private static final int PICKER_REQUEST_CODE = 100;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseRef;
    SharedPreferences sharedPref;
    ProgressBar progressBar;
    List<String> imagesList;
    ProductModel productModel;

    AppCompatSpinner spinner;
    EditText title_et,price_et,quantity_et,description_et,gc_et,ram_et,hard_disk_et;
    SwitchCompat enable_switch;
    LinearLayout image_choose_layout,images_layout;
    ImageView image1_iv,image2_iv;
    Button add_btn;

    private String brand_name;

    int product_enable=0;
    private Uri filePath;
    Bitmap bitmap;

    int image_count=0;
    StorageReference storageReference;

    FirebaseStorage storage;

    Uri uploadUrl=null;

    Intent intentUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        getSupportActionBar().setTitle("Add Products");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();

        }

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        intentUpdate = getIntent();

        sharedPref = getSharedPreferences(ConstantStrings.SEESHOP_PREFS, 0);

        mDatabaseRef = database.getReference("products").child(sharedPref.getString(ConstantStrings.USER_ID,"0"));


        /*Views*/
        progressBar = findViewById(R.id.progressBar);
        spinner = findViewById(R.id.brand_spinner);
        title_et = findViewById(R.id.title_et);
        price_et = findViewById(R.id.price_et);
        quantity_et = findViewById(R.id.quantity_et);
        description_et = findViewById(R.id.description_et);
        enable_switch = findViewById(R.id.enable_switch);
        image_choose_layout = findViewById(R.id.image_choose_layout);
        images_layout = findViewById(R.id.images_layout);
        image1_iv = findViewById(R.id.image1_iv);
        image2_iv = findViewById(R.id.image2_iv);
        add_btn = findViewById(R.id.add_btn);

        gc_et = findViewById(R.id.gc_et);
        ram_et = findViewById(R.id.ram_et);
        hard_disk_et = findViewById(R.id.hard_disk_et);


        imagesList = new ArrayList<>();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                brand_name= adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        enable_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {
                    /*Enable*/
                    product_enable=1;
                }
                else {
                    product_enable=0;
                }
            }
        });

        /*get product data*/
        getProductIntentValues();

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(title_et.getText().toString())
                        || TextUtils.isEmpty(price_et.getText().toString())
                        || TextUtils.isEmpty(quantity_et.getText().toString())
                        || TextUtils.isEmpty(description_et.getText().toString())
                        || TextUtils.isEmpty(ram_et.getText().toString())
                        || TextUtils.isEmpty(hard_disk_et.getText().toString())
                        || TextUtils.isEmpty(gc_et.getText().toString())

                )
                {
                    Toast.makeText(AddProductsActivity.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }
                else if (imagesList.size()<2)
                {
                    Toast.makeText(AddProductsActivity.this, "Choose 2 Images!", Toast.LENGTH_SHORT).show();

                }
                else {
                    if (intentUpdate.hasExtra(ConstantStrings.PRODUCT_ITEM)) {
                        updateDataToFirebase();
                    } else {
                        saveDataToFirebase();
                    }
                }

            }
        });

        image_choose_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectGalleryOrCameraImage();


            }
        });



    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void saveDataToFirebase() {

        productModel= new ProductModel();

        productModel.setTitle(title_et.getText().toString());
        productModel.setPrice(Integer.parseInt(price_et.getText().toString()));
        productModel.setQuantity(Integer.parseInt(quantity_et.getText().toString()));
        productModel.setDescription(description_et.getText().toString());
        productModel.setBrand(brand_name);
        productModel.setProduct_enable(product_enable);
        productModel.setRam(Integer.parseInt(ram_et.getText().toString().trim()));
        productModel.setHard_disk(Integer.parseInt(hard_disk_et.getText().toString()));
        productModel.setGraphic_card(Integer.parseInt(gc_et.getText().toString()));



        if (imagesList.size()>0)
        {
            productModel.setImages(imagesList);
        }

        String _id =  mDatabaseRef.push().getKey();


        productModel.setSeller_id(sharedPref.getString(ConstantStrings.USER_ID,"0"));
        productModel.setProduct_id(_id);

        mDatabaseRef.child(_id).setValue(productModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AddProductsActivity.this, "Your "+ title_et.getText().toString() +" Successfully added ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProductsActivity.this, ManageProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });



    }




    /*Images get and uplaod work*/

    private void selectGalleryOrCameraImage() {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(AddProductsActivity.this);
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case PICK_IMAGE_ID:
                bitmap = ImagePicker.getImageFromResult(AddProductsActivity.this, resultCode, data);

                if (requestCode == PICK_IMAGE_ID && resultCode == RESULT_OK && data != null && data.getData() != null) {

                    image_count=image_count+1;
                    // Get the Uri of data
                    filePath = data.getData();
                    try {



                        if (bitmap!=null) {


                            images_layout.setVisibility(View.VISIBLE);
                            if (image_count==1){
                                uploadImage();
                                /*-- Previews Imageviews*/
                                image1_iv.setImageBitmap(bitmap);

                            }
                            else if (image_count==2)
                            {
                                uploadImage();
                                /*-- Previews Imageviews*/
                                image2_iv.setImageBitmap(bitmap);
                            }


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
        int camera = ContextCompat.checkSelfPermission(AddProductsActivity.this,
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
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
            ActivityCompat.requestPermissions(AddProductsActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
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
                                            .makeText(AddProductsActivity.this,
                                                    "Image Loaded!!",
                                                    Toast.LENGTH_SHORT).show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            uploadUrl=uri;
                                            imagesList.add(uploadUrl.toString());
//                                            if (imagesList.size()>0)imagesList.clear();
//                                            if (image_count==1)
//                                                imagesList.add(0,uploadUrl.toString());
//                                            else if (image_count==2)
//                                                imagesList.add(1,uploadUrl.toString());


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
                                    .makeText(AddProductsActivity.this,
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


    public void getProductIntentValues()
    {
        {
            if (intentUpdate.hasExtra(ConstantStrings.PRODUCT_ITEM))
            {
                getSupportActionBar().setTitle("Update Products");
                add_btn.setText("Update");
                productModel= (ProductModel) intentUpdate.getSerializableExtra(ConstantStrings.PRODUCT_ITEM);

                title_et.setText(productModel.getTitle());
                price_et.setText(""+productModel.getPrice());
                quantity_et.setText(""+productModel.getQuantity());
                description_et.setText(productModel.getDescription());
                ram_et.setText(String.valueOf(productModel.getRam()));
                hard_disk_et.setText(String.valueOf(productModel.getHard_disk()));
                gc_et.setText(String.valueOf(productModel.getGraphic_card()));


                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (productModel.getBrand() != null) {
                    int spinnerPosition = adapter.getPosition(productModel.getBrand());
                    spinner.setSelection(spinnerPosition);
                }

                product_enable= productModel.getProduct_enable();
                if (product_enable==1)
                {
                    enable_switch.setChecked(true);
                }else{
                    enable_switch.setChecked(false);
                }

                images_layout.setVisibility(View.GONE);

                if(indexExists(productModel.getImages(),0)){
                    imagesList.add(0,productModel.getImages().get(0));
                }

                if(indexExists(productModel.getImages(),1)){
                    imagesList.add(1,productModel.getImages().get(1));
                }
               /* if (productModel.getImages().size()>=1)
                {


                    *//*----- Image loading library from internet ----------------------*//*
                    Picasso.get().load(productModel.getImages().get(0)).into(image1_iv);
                    Picasso.get().load(productModel.getImages().get(1)).into(image2_iv);
                }
*/



            }
        }

    }

    public boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }

    /*-- Update product values ---*/
    private void updateDataToFirebase() {


        /*------------------ Update Product  -----------------------------------------*/
        mDatabaseRef = database.getReference("products").child(sharedPref.getString(ConstantStrings.USER_ID,"0")).child(productModel.getProduct_id());
        mDatabaseRef.child("title").setValue(title_et.getText().toString());
        mDatabaseRef.child("price").setValue(Integer.parseInt(price_et.getText().toString()));
        mDatabaseRef.child("quantity").setValue(Integer.parseInt(quantity_et.getText().toString()));
        mDatabaseRef.child("description").setValue(description_et.getText().toString());
        mDatabaseRef.child("brand").setValue(brand_name);
        mDatabaseRef.child("product_enable").setValue(product_enable);

        mDatabaseRef.child("ram").setValue(Integer.parseInt(ram_et.getText().toString().trim()));
        mDatabaseRef.child("hard_disk").setValue(Integer.parseInt(hard_disk_et.getText().toString()));
        mDatabaseRef.child("graphic_card").setValue(Integer.parseInt(gc_et.getText().toString()));
        if (imagesList.size()>0)
        {
            mDatabaseRef.child("images").setValue(imagesList);
        }

        mDatabaseRef.child("title").setValue(title_et.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AddProductsActivity.this, "Your "+ title_et.getText().toString() +" Successfully updated ", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(AddProductsActivity.this, ManageProductsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });



    }




}