package com.example.inger.beaconbeachler;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }


}
