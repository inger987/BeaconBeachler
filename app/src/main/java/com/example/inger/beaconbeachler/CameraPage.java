package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;

public class CameraPage extends AppCompatActivity {
    Button tabilde;
    Button lastopp;
    ImageView image;

    private static final int CAMERA_REQUEST = 1888;

   private String UPLOAD_URL = "https://home.hbv.no/110115/bac/bilde.php";
    private String UPLOAD_KEY = "image";
    private Bitmap photo;

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
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);

            }
        });
        lastopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            uploadImage();

            }
        });



}
    public String getStringImage (Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        photo = savedInstanceState.getParcelable("BitmapImage");
        this.photo = photo;
        image.setImageBitmap(photo);
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putParcelable("BitmapImage", photo);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void uploadImage () {
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(CameraPage.this, "Bildet lastes opp", "Vent litt...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Bildet er nå lastet opp", Toast.LENGTH_LONG).show();
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
        ui.execute(photo);


    }


}



