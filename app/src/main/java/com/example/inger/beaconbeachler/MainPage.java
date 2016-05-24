package com.example.inger.beaconbeachler;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;

public class MainPage extends com.example.inger.beaconbeachler.Menu implements View.OnClickListener {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    Button btnCamera;
    Button btnText;
    public static Button btnBeacon;
    Button btnSound;
    TextView tvUsername;
    BluetoothAdapter mBluetoothAdapter;
    private String minor ="";

    static boolean active = false;

    private final static int REQUEST_ENABLE_BT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_main_page);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean blueToothFinnish = verifyBluetooth();
        if(blueToothFinnish)
           // userManualDialog();

        btnText = (Button)findViewById(R.id.btnText);
       btnSound = (Button)findViewById(R.id.btnSound);
        btnBeacon = (Button)findViewById(R.id.btnBeacon);
        btnCamera = (Button)findViewById(R.id.btnCamera);

        btnText.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnBeacon.setOnClickListener(this);
        btnSound.setOnClickListener(this);

        SharedPreferences minor = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = minor.edit();
        editor.putString(Config.KEY_MINOR, "5");
        //Saving values to editor
        editor.apply();

        if (minor.equals ("2")){
            btnBeacon.setBackgroundResource(R.drawable.beaconclose);
        }

        if (minor.equals("5")){
            btnBeacon.setBackgroundResource(R.mipmap.ibeaconicon);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(this);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.BEACON_PICTURE_PREF, Context.MODE_PRIVATE);

        Button btnBeacon=(Button)findViewById(R.id.btnBeacon);
        btnBeacon.setEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(null);
      /*  SharedPreferences sharedPreferences = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        minor = sharedPreferences.getString(Config.KEY_MINOR, "Not available");
        if (sharedPreferences.equals("2")){
            btnBeacon.setBackgroundResource(R.drawable.beaconclose);
        }
*/

    }

    private boolean verifyBluetooth() {

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
        return true;
    }

    public void changeImage(){
                //Button btnBeacon;
                //btnBeacon = (Button)findViewById(R.id.btnBeacon);
                btnBeacon.setBackgroundResource(R.drawable.beaconclose);
    }

    public void changePic(){
       // Button btnBeacon;
        //btnBeacon = (Button)findViewById(R.id.btnBeacon);
        btnBeacon.setBackgroundResource(R.mipmap.ibeaconicon);
    }
    private void userManualDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("BRUKERVEILEDNING");

        LayoutInflater factory = LayoutInflater.from(MainPage.this);
        final View view = factory.inflate(R.layout.dialogboxmanual, null);

        ImageView image= (ImageView)view.findViewById(R.id.imageView);
        image.setImageResource(R.drawable.beaconclose);

        TextView text= (TextView) view.findViewById(R.id.textView);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
                if (mBluetoothAdapter.isEnabled())
                    startActivity(new Intent(MainPage.this, BeaconPage.class).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                else
                    Toast.makeText(getBaseContext(), "Du må aktivere bluetooth hvis du ønsker å bruke denne funksjonen", Toast.LENGTH_LONG).show();
                break;
            case R.id.btnSound:
                startActivity(new Intent(MainPage.this, AudioPage.class));
                break;
        }
    }

    @Override
    public void onStart() {
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(this);

        SharedPreferences sharedPreferences = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        minor = sharedPreferences.getString(Config.KEY_MINOR, "Not available");
        if (sharedPreferences.equals("2")){
            btnBeacon.setBackgroundResource(R.drawable.beaconclose);
        }
        if (sharedPreferences.equals("5")){
            btnBeacon.setBackgroundResource(R.mipmap.ibeaconicon);
        }
        super.onStart();
        active = true;
      //  changeImage();
    }

    @Override
    public void onStop() {

/*
        SharedPreferences sharedPreferences = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        minor = sharedPreferences.getString(Config.KEY_MINOR, "Not available");
        if (sharedPreferences.equals("2")){
            btnBeacon.setBackgroundResource(R.drawable.beaconclose);

        }
        if (sharedPreferences.equals("5")){
            btnBeacon.setBackgroundResource(R.mipmap.ibeaconicon);
        }
        */
        super.onStop();
        active=false;
    }


}
