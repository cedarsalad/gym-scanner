package com.example.chads.gymscanner;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    private Button btnPicture;
    private Button btnScan;
    private Button btnRegister;
    private ImageView imgPicture;
    private EditText etxtFirstName;
    private EditText etxtAddress;
    private EditText etxtDob;
    private EditText etxtAge;
    private EditText etxtCity;
    private EditText etxtCode;
    private EditText etxtProvince;
    private EditText etxtLastName;

    private  String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 10;
    static final int REQUEST_BARCODE = 100;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnPicture = (Button)findViewById( R.id.btnPicture );
        btnScan = (Button)findViewById( R.id.btnScan );
        btnRegister = (Button)findViewById( R.id.btnRegister );
        imgPicture = (ImageView)findViewById( R.id.imgPicture );
        etxtFirstName = (EditText)findViewById( R.id.etxtFirstName);
        etxtLastName = (EditText)findViewById( R.id.etxtLastName );
        etxtAddress = (EditText)findViewById( R.id.etxtAddress );
        etxtDob = (EditText)findViewById( R.id.etxtDob );
        etxtAge = (EditText)findViewById( R.id.etxtAge );
        etxtCity = (EditText)findViewById( R.id.etxtCity );
        etxtCode = (EditText)findViewById( R.id.etxtCode);
        etxtProvince = (EditText)findViewById( R.id.etxtProvince );

        imgPicture.setTag("default");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            btnPicture.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }


        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan(view);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean valid = true;
                ConstraintLayout c = (ConstraintLayout) findViewById(R.id.constraintLayout1);
                for( int i = 0; i < c.getChildCount(); i++ ){
                    if( c.getChildAt( i ) instanceof EditText )
                       if ((TextUtils.isEmpty(((EditText) c.getChildAt( i )).getText().toString()))){
                            valid = false;
                            break;
                       }
                }

                if (imgPicture.getTag().equals("default"))
                    valid = false;

                if (valid) {
                    DBManager db = new DBManager(SignupActivity.this);
                    Member m = new Member();
                    m.setId(-1);
                    m.setAvatar(mCurrentPhotoPath);
                    m.setFirstName(etxtFirstName.getText().toString());
                    m.setLastName(etxtLastName.getText().toString());
                    m.setDob(etxtDob.getText().toString());
                    m.setAge(Integer.parseInt(etxtAge.getText().toString()));
                    m.setAddress(etxtAddress.getText().toString());
                    m.setCity(etxtCity.getText().toString());
                    m.setProvince(etxtProvince.getText().toString());
                    m.setBarcode(etxtCode.getText().toString());
                    m.setVisits(0);
                    m.setStatus(false);
                    db.addMember(m);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.putExtra("status", "successful");
                    startActivity(intent);
                } else {
                    Toast.makeText(SignupActivity.this, "Please fill out all forms.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                btnPicture.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        //not saving the image
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            saveToInternalStorage(imageBitmap);
            imgPicture.setImageBitmap(imageBitmap);
            imgPicture.setTag(mCurrentPhotoPath);
            Log.d("create bitmap, here is the uri", mCurrentPhotoPath.toString());
        }else if (requestCode == REQUEST_BARCODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String code = data.getStringExtra("SCAN_RESULT");
            String format = data.getStringExtra("SCAN_RESULT_FORMAT");
            etxtCode.setText(code);
            Toast.makeText(this, "Barcode: " + code + " Format: " + format, Toast.LENGTH_LONG).show();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/images
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // name file by unique timestamp
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpeg_" + timeStamp;
        Log.d("image name", imageFileName);
        // Create imageDir
        File mypath = new File(directory,imageFileName);
        mCurrentPhotoPath = mypath.getAbsolutePath();
        Log.d("absolute path name", mCurrentPhotoPath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path)
    {

        try {
            File f = new File(path, "avatar.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            imgPicture.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    public void scan(View view){
        Intent scanIntent = new Intent(ACTION_SCAN);
        scanIntent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
        startActivityForResult(scanIntent, REQUEST_BARCODE);
    }

//    private File createImageFile() throws IOException{
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
}

//        }else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            imgPicture.setImageURI((Uri) extras.get("URI"));
//            Log.d("setImageUri", extras.get("URI").toString());


// Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
// Continue only if the File was successfully created
//            if (photoFile != null) {
//                Log.d("photoFile", mCurrentPhotoPath);
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra("URI", photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }