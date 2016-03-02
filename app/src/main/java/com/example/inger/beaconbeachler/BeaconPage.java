package com.example.inger.beaconbeachler;


import android.bluetooth.BluetoothAdapter;

import android.content.Intent;
import android.os.Bundle;

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class BeaconPage extends AppCompatActivity implements BeaconConsumer{
    protected static final String TAG = "readme";
    private BeaconManager beaconManager;
    Region region1;
    Region region2;
    Button start;
    Button stopp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_page);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.bind(this);
        start = (Button)findViewById(R.id.start);
        stopp = (Button)findViewById(R.id.stopp);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBeaconServiceConnect();
            }
        });
        stopp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            }
        });
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {

                 intent = new Intent(getApplicationContext(), EggAndBeacon.class);
               intent.putExtra("uuid", region.getId1().toString());
               intent.putExtra("major", region.getId2().toString());
                intent.putExtra("minor", region.getId3().toString());
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Log.i(TAG, "Beacon!!!!!!"+region.getId1().toString());
                Log.i(TAG, "Yeeeey!!!!!!"+region.getId2().toString());
                Log.i(TAG, "Jiiiiihaaaaa!!!!" + region.getId3().toString());

               Boolean beacon = true;
                if (beacon==true) {
                    try {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        beaconManager.stopMonitoringBeaconsInRegion(region1);
                        beaconManager.stopMonitoringBeaconsInRegion(region2);
                    }
                    catch (RemoteException e) { e.printStackTrace(); }
                }
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
            region1 = new Region("myIdentifier1", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("1"));
            region2 = new Region("myIdentifier2", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("2"));

            beaconManager.startMonitoringBeaconsInRegion(region1);
            beaconManager.startMonitoringBeaconsInRegion(region2);
            // beaconManager.startMonitoringBeaconsInRegion(new Region("com.example.inger.beaconbeachler.boostrapRegion",
                //    Identifier.parse(Constants.BT_UUID),
               //     Identifier.fromInt(Constants.BT_MAJOR),
               //     Identifier.fromInt(Constants.BT_MINOR)));

        } catch (RemoteException e) { e.printStackTrace(); }
    }


}


