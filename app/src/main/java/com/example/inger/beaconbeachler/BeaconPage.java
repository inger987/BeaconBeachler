package com.example.inger.beaconbeachler;


import android.bluetooth.BluetoothAdapter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class BeaconPage extends AppCompatActivity implements BeaconConsumer{
    protected static final String TAG = "readme";
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_page);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                 Intent intent = new Intent(getApplicationContext(), EggAndBeacon.class);
               intent.putExtra("uuid", region.getId1().toString());
               intent.putExtra("major", region.getId2().toString());
                intent.putExtra("minor", region.getId3().toString());
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i(TAG, "Beacon!!!!!!"+region.getId1().toString());
                Log.i(TAG, "Yeeeey!!!!!!"+region.getId2().toString());
                Log.i(TAG, "Jiiiiihaaaaa!!!!" + region.getId3().toString());
             //   Toast.makeText(getBaseContext(), region.getId1().toString(), Toast.LENGTH_LONG).show();
            //    Intent intent = new Intent(BeaconPage.this,CameraPage.class);
             //   startActivity(intent);
            }


            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see any beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: " + state);
            }
        });

        try {

            beaconManager.startMonitoringBeaconsInRegion(new Region("com.example.inger.beaconbeachler.boostrapRegion",
                    Identifier.parse(Constants.BT_UUID),
                    Identifier.fromInt(Constants.BT_MAJOR),
                    Identifier.fromInt(Constants.BT_MINOR)));
        } catch (RemoteException e) { e.printStackTrace(); }
    }


}


