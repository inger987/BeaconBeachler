package com.example.inger.beaconbeachler;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

public class MainPage extends AppCompatActivity implements View.OnClickListener {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    Button btnCamera;
    Button btnText;
    Button btnBeacon;
    Button btnSound;
    TextView tvUsername;
    BluetoothAdapter mBluetoothAdapter;


    private final static int REQUEST_ENABLE_BT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main_page);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        verifyBluetooth();
        btnText = (Button)findViewById(R.id.btnText);
        btnSound = (Button)findViewById(R.id.btnSound);
        btnBeacon = (Button)findViewById(R.id.btnBeacon);
        btnCamera = (Button)findViewById(R.id.btnCamera);


        btnText.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnBeacon.setOnClickListener(this);
        btnSound.setOnClickListener(this);

        //Fetching username from shared preferences
     /*   SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

        //Showing the current logged in username to textview
        tvUsername.setText("Velkommen, " + username+"!"); */

    }

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(null);
    }

    private void verifyBluetooth() {

        if (!mBluetoothAdapter.isEnabled()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Vennligst aktiver bluetooth")
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                        }
                    });
            builder.setNegativeButton("Nei, ellers takk!", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();


        }
        else {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnText:
                startActivity(new Intent(MainPage.this,WritingPage.class));
                break;
            case R.id.btnCamera:
                startActivity(new Intent(MainPage.this,CameraPage.class));
                break;
            case R.id.btnBeacon:

                if (mBluetoothAdapter.isEnabled()) {

                    startActivity(new Intent(MainPage.this, BeaconPage.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
                else {
                    Toast.makeText(getBaseContext(), "Du må aktivere bluetooth hvis du ønsker å bruke denne funksjonen", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnSound:
                startActivity(new Intent(MainPage.this, Lyd.class));
                break;
        }
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

                        LoginManager.getInstance().logOut();

                        //Starting login activity
                        Intent intent = new Intent(MainPage.this, LoginPage.class);
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
            startActivity(new Intent(MainPage.this, Lyd.class));
        }

        if (id== R.id.action_camera){
            startActivity(new Intent(MainPage.this, CameraPage.class));
        }

        if (id==R.id.action_text){
            startActivity(new Intent(MainPage.this, WritingPage.class));
        }

        if(id==R.id.action_beacon){
            startActivity(new Intent(this, BeaconPage.class));
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }


}
