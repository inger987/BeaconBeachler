package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

public class CameraPage extends AppCompatActivity {
    Button tabilde;
    Button lastopp;
    ImageView image;
    private Uri filePath;
    private static final int CAMERA_REQUEST = 1888;

    private final int CAMERA_RESULT = 1;
    private final String Tag = getClass().getName();
   private String UPLOAD_URL = "https://home.hbv.no/110115/bac/upload.php";
    private String UPLOAD_KEY = "image";
  //  private Bitmap photo;
  //  private Bitmap bitmap;
    Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        tabilde = (Button) findViewById(R.id.tabilde);
        lastopp = (Button) findViewById(R.id.lastopp);
        image = (ImageView) findViewById(R.id.image);
      //  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      //  startActivityForResult(cameraIntent, CAMERA_REQUEST);


        tabilde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();

                if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                    i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);

                    startActivityForResult(i, CAMERA_RESULT);

                }
                else {

                    Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();

                }
            }
        });
        lastopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            uploadImage();

            }
        });



}
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_RESULT) {


                File out = new File(getFilesDir(), "Image.jpg");

                if(!out.exists()) {

                    Toast.makeText(getBaseContext(), "Error while capturing image", Toast.LENGTH_LONG).show();

                    return;
                }

           //  mBitmap = (Bitmap) data.getExtras().get("data");
          //  filePath = data.getData();
      //     try {
        //        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
        //        image.setImageBitmap(bitmap);
        //    } catch (IOException e) {
        //        e.printStackTrace();
        //    }
            mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
         image.setImageBitmap(mBitmap);

        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mBitmap = savedInstanceState.getParcelable("BitmapImage");
     //   this.photo = photo;
        image.setImageBitmap( mBitmap);
        super.onRestoreInstanceState(savedInstanceState);

    }
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putParcelable("BitmapImage",  mBitmap);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CameraPage.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(mBitmap);
    }


}



