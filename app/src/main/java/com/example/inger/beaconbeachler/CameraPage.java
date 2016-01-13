package com.example.inger.beaconbeachler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.net.URL;

public class CameraPage extends AppCompatActivity {
    Button tabilde;
    Button lastopp;
    ImageView image;
    private static final int CAMERA_REQUEST = 1888;

  //  private String urlen = "https://home.hbv.no/110115/bac/bilde.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        tabilde = (Button) findViewById(R.id.tabilde);
        lastopp = (Button) findViewById(R.id.lastopp);
        image = (ImageView) findViewById(R.id.image);

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

            }
        });



}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(photo);
        }
    }


}



