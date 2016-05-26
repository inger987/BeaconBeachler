package com.example.inger.beaconbeachler;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class BeaconReference extends Application implements BootstrapNotifier, BeaconConsumer, RangeNotifier {

    private static final String TAG = "BeaconReferenceApp";
    protected RegionBootstrap regionBootstrap;
    protected BackgroundPowerSaver backgroundPowerSaver;
    BeaconManager beaconManager;
    AudioPage monitoringAct = null;
    CameraPage monitorActi = null;
    WritingPage monitoriact = null;
    MainPage monitorActa = null;

    private Region region;

    public void onCreate() {
        super.onCreate();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.getInstanceForApplication(this).getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));
        region = new Region("backgroundRegion", Identifier.parse("00000000-0000-0000-c000-000000000028"), null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager.setBackgroundBetweenScanPeriod(30000l);
        beaconManager.setBackgroundScanPeriod(1100l);
        beaconManager.setForegroundScanPeriod(1100l);
        beaconManager.setForegroundBetweenScanPeriod(8000l);
        beaconManager.bind(this);

    }

    @Override
    public void didEnterRegion(Region region) {
        Log.d(TAG, "did enter region.");
        try {
            beaconManager.startRangingBeaconsInRegion(region);
        }
        catch (RemoteException e) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Can't start ranging");
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.d(TAG, "did exit region");
        try {
            beaconManager.stopRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG, "I have just switched from seeing/not seeing beacons: " + state);
    }

    private void sendNotification(String text) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Beacon Reference Application")
                        .setContentText(text)
                        .setSmallIcon(R.mipmap.ibeaconicon);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainPage.class));
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


    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        for (Beacon b : beacons) {
            if (b.getDistance() < 0.3) {
                Log.d(TAG, "Det er er beacon i nærheten");

                    SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(Config.KEY_MINOR, b.getId3().toString());
                    editor.commit();
                }
            if (b.getDistance() > 0.3){
                Log.d(TAG, "Du er utenfor rekkevidde av beacon");

                SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(Config.KEY_MINOR, "5");
                editor.commit();
            }

        if (beacons.size() > 0) {
                if(b.getId1().toString().equals("00000000-0000-0000-c000-000000000028")) {
                    Log.e(TAG, "Beacon with my Instance ID found!");
                    sendNotification("Beacon with my Instance ID found!");
                }
            }
        }
    }

    public void setMonitoringAct(AudioPage activity) {
        this.monitoringAct = activity;
    }
    public void setMonitorActi (CameraPage activity) {
        this.monitorActi = activity;
    }
    public void setMonitoriact (WritingPage activity) {this.monitoriact = activity;}
    public void setMonitorActa (MainPage activity) {this.monitorActa = activity;}

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(this);
    }
}