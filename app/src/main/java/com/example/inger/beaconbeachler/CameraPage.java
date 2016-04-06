package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CameraPage extends AppCompatActivity {
    Button tabilde;
    Button lastopp;
    ImageView image;
    ImageView zoom;


    private final int CAMERA_RESULT = 1;
    private String UPLOAD_URL = "https://home.hbv.no/110115/bac/upload.php";
    private String UPLOAD_KEY = "image";
    private String BILDENAVN = "bilde";
    private String KEY_CATID = "categoryId";
    public static final String KEY_USERID = "userId";
    Bitmap mBitmap;

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
        ui.execute(mBitmap);
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.USERNAME_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(CameraPage.this, LoginPage.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void showprofil() {
        //linked to webside /profil

        String url = "https://home.hbv.no/110118/bachelor/homepage/mainPage.php#";

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
       /* --> Try to show icon in overflow       MenuItem item = menu.findItem(R.id.action_sound);
        SpannableStringBuilder builder = new SpannableStringBuilder("sound.png Login");
        // replace "*" with icon
        builder.setSpan(new ImageSpan(this, R.mipmap.sound), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            logout();
        }

        if (id == R.id.action_favorite) {
            //se din profil / endre profil
            showprofil();
        }

        if (id == R.id.action_sound){
            startActivity(new Intent(CameraPage.this, Lyd.class));
        }

        if (id== R.id.action_camera){
            startActivity(new Intent(CameraPage.this, CameraPage.class));
        }

        if (id==R.id.action_text){
            startActivity(new Intent(CameraPage.this, WritingPage.class));
        }

        if(id==R.id.action_beacon){
            startActivity(new Intent(CameraPage.this, BeaconPage.class));
        }

        return super.onOptionsItemSelected(item);
    }


}



