package com.example.inger.beaconbeachler;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;


import java.util.Collection;

/**
 * Created by Elin on 08.03.2016.
 */
public class BeaconReferenceApplication extends Application implements BootstrapNotifier {


    private static final String TAG = "BeaconReferenceApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private MainPage monitoringActivity = null;
    private Lyd monitoringAct = null;
    Region region1;
    Region region2;
    private static final String uuid = "00000000-0000-0000-c000-000000000028";
    private BluetoothAdapter mBluetoothAdapter;
    private String Minor;
    private String Major;
    private String UUID1;
    private BluetoothManager bluemanager;
    private BeaconManager mBeaconManager;
    public boolean enabler;
    public Context context;
   // private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);


    public void onCreate() {
        super.onCreate();

        bluemanager= (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluemanager.getAdapter();

        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        //BeaconManager.getInstanceForApplication(this).getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.setBackgroundScanPeriod(1100l);

        mBeaconManager.setBackgroundBetweenScanPeriod(1100l);

        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
       // BeaconManager.getInstanceForApplication(this).getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        // By default the AndroidBeaconLibrary will only find AltBeacons.  If you wish to make it
        // find a different type of beacon, you must specify the byte layout for that beacon's
        // advertisement with a line like below.  The example shows how to find a beacon with the
        // same byte layout as AltBeacon but with a beaconTypeCode of 0xaabb.  To find the proper
        // layout expression for other beacon types, do a web search for "setBeaconLayout"
        // including the quotes.
        //



        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        // beaconManager.setBackgroundBetweenScanPeriod(60000l);
        // Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                Identifier.parse("00000000-0000-0000-c000-000000000028"), null, null);
        final Region region1 = new Region("myIdentifier1", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("1"));
        final Region region2 = new Region("myIdentifier2", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("2"));

        regionBootstrap = new RegionBootstrap(this, region1);
        regionBootstrap = new RegionBootstrap(this, region2);

        BeaconManager.getInstanceForApplication(this).getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));


        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();

        SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.KEY_MINOR, "5");
        editor.commit();
    }

   // @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.

        Log.d(TAG, "did enter region.");

        Log.d(TAG, "Got a didEnterRegion call region:" + arg0.getId3());

        /*
        SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(Config.KEY_MINOR, arg0.getId3().toString());
        editor.commit();
        */
/*
        mBeaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    if (beacon.getDistance() < 0.1) {
                        Log.d(TAG, "Det er er beacon en halvannen meter unna");
                        try {
                            mBeaconManager.stopRangingBeaconsInRegion(region1);
                            mBeaconManager.stopRangingBeaconsInRegion(region2);


                            SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(Config.KEY_MINOR, beacon.getId3().toString());
                            editor.commit();


                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            mBeaconManager.stopRangingBeaconsInRegion(region1);
                            mBeaconManager.stopRangingBeaconsInRegion(region2);

                            SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(Config.KEY_MINOR, "5");
                            editor.commit();

                            //   ingenBeacon();
                        } catch (RemoteException e) {
                            e.printStackTrace();

                        }

                    }
                    break;

                }

            }
        });
        */

            //     @Override
            //       public void didExitRegion(Region region) {
            //           Log.i(TAG, "Kor e beacon?");
            //       }

            //    @Override
            //       public void didDetermineStateForRegion(int state, Region region) {
            //           Log.i(TAG, "Eg så ein beacon/beacon blei borte " + state);
            //        }


        //Beacon.getId2().toString();

/*        SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("categoryId",arg0.getId3().toString());
        editor.commit();
        */


        sendNotification();

        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            //    Intent intent = new Intent(this, MainPage.class);
            //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            //   this.startActivity(intent);

            SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Config.KEY_MINOR, arg0.getId3().toString());
            editor.commit();

            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                //    monitoringActivity.logToDisplay("I see a beacon again" );
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                //sendNotification();
            }

            if (monitoringAct != null) {
            }
                else {
                    // If we have already seen beacons before, but the monitoring activity is not in
                    // the foreground, we send a notification to the user on subsequent detections.
                    Log.d(TAG, "Sending notification.");
                    //sendNotification();
                }
            }
        }



    @Override
    public void didExitRegion(Region region) {
        if (monitoringActivity != null) {
            //    monitoringActivity.logToDisplay("I no longer see a beacon.");

            SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Config.KEY_MINOR, "5");
            editor.commit();

        }

        if (monitoringAct != null){
            SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(Config.KEY_MINOR, "5");
            editor.commit();
        }

    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        if (monitoringActivity != null) {
            //   monitoringActivity.logToDisplay("I have just switched from seeing/not seeing beacons: " + state);
        }
    }
/*
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        // TODO Auto-generated method stub
        Log.d(TAG, "Entra en onLeScan");
        Major = "";
        Minor = "";
        UUID1 = "";

        for (int i = 0; i < scanRecord.length; i++) {
            if (i > 8 && i < 25)
                UUID1 += String.format("%02x", scanRecord[i]);
            else if (i > 24 && i < 27)
                Major += String.format("%02x", scanRecord[i]);
            else if (i > 26 && i < 29)
                Minor += String.format("%02x", scanRecord[i]);

        }
        stop();
    }

    public void stop(){

        mBluetoothAdapter.stopLeScan(this);
        Log.d(TAG, ": scanLeDevice-> REGION Stopped");
        Log.d(TAG, "Got a didExitRegion call with MAJOR:"+Major+" MINOR: "+Minor );


    }
*/
    public void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Egg and Beacon app")
                        .setContentText("Du er i nærheten av en beacon")
                        .setSmallIcon(R.mipmap.ibeaconicon);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, BeaconPage.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    public void setMonitoringActivity(MainPage activity) {
        this.monitoringActivity = activity;

    }

    public void setMonitoringAct(Lyd activity) {
        this.monitoringAct = activity;

    }




}