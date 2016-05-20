package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CameraPage extends Menu {
    Button tabilde;
    Button lastopp;
    ImageView image;
    ImageView zoom;


    private String username;
    private String minor;
    private String format;

    private final int CAMERA_RESULT = 1;
    private static final String UPLOAD_URL = "https://home.hbv.no/110115/bac/upload.php";
    private static final String UPLOAD_KEY = "image";
    private static final String BILDENAVN = "bilde";
    private static final String KEY_CATID = "categoryId";
    public static final String KEY_USERID = "userId";
    Bitmap mBitmap;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        tabilde = (Button) findViewById(R.id.tabilde);
        lastopp = (Button) findViewById(R.id.lastopp);
        image = (ImageView) findViewById(R.id.image);
        zoom = (ImageView) findViewById(R.id.zoom);
        if (savedInstanceState != null) {
            image.setImageBitmap(mBitmap);
        }
        else {
            cameraActivity();
        }


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                zoom.setImageBitmap(mBitmap);
                zoom.setVisibility(View.VISIBLE);
                image.setVisibility(View.INVISIBLE);
                tabilde.setVisibility(View.INVISIBLE);
                lastopp.setVisibility(View.INVISIBLE);

            }
        });
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoom.setVisibility(View.INVISIBLE);
                image.setVisibility(View.VISIBLE);
                tabilde.setVisibility(View.VISIBLE);
                lastopp.setVisibility(View.VISIBLE);
            }
        });

        tabilde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraActivity();
            }
        });
        lastopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBitmap != null) {
                    uploadImage();
                } else {
                    Toast.makeText(getBaseContext(), "Her kommer ingen forbi", Toast.LENGTH_LONG).show();
                }
            }
        });

}
    public void cameraActivity()
    {

        PackageManager pm = getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            i.putExtra(MediaStore.EXTRA_OUTPUT, MyFileContentProvider.CONTENT_URI);

            startActivityForResult(i, CAMERA_RESULT);

        }
        else {

            Toast.makeText(getBaseContext(), "Enheten har ikke kamera", Toast.LENGTH_LONG).show();

        }

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

                    Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_LONG).show();

                    return;
                }

            mBitmap = BitmapFactory.decodeFile(out.getAbsolutePath());
         image.setImageBitmap(mBitmap);

        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mBitmap = savedInstanceState.getParcelable("BitmapImage");
        image.setImageBitmap(mBitmap);
        super.onRestoreInstanceState(savedInstanceState);

    }
    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putParcelable("BitmapImage", mBitmap);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void uploadImage(){
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        minor = sharedPreferences.getString(Config.KEY_MINOR, "5");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        format = simpleDateFormat.format(new Date());
        loading = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(CameraPage.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(CameraPage.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                String uploadImage = getStringImage(mBitmap);

                Map<String,String> params = new HashMap<String, String>();
                params.put(UPLOAD_KEY,uploadImage);
                params.put(BILDENAVN, format);
                params.put(KEY_USERID, username);
                params.put(KEY_CATID, minor);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
      /*  class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
           loading = ProgressDialog.show(CameraPage.this, "Laster opp..." + username, null,true,true);


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

                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                final String format = simpleDateFormat.format(new Date());
                final String uploadImage = getStringImage(bitmap);
                final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
                SharedPreferences sharedPref = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                final String minor = sharedPref.getString(Config.KEY_MINOR, "Not Available");

               HashMap<String, String> data = new HashMap<String, String>()
                {{
                        put(UPLOAD_KEY, uploadImage);
                        put(BILDENAVN, format);
                        put(KEY_USERID, username);
                        put(KEY_CATID, minor);

                    }};


                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(mBitmap); */
    }

    public void onBackPressed() {
        Intent intent = new Intent(this,MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}



