package com.example.inger.beaconbeachler;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import java.util.Collection;


public class BeaconPage extends Activity implements BeaconConsumer{
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);


    Region region1;
    Region region2;
    Button start;
    Button stopp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_page);


        beaconManager.bind(this);
    //    start = (Button)findViewById(R.id.start);
    //    stopp = (Button)findViewById(R.id.stopp);
      //  start.setOnClickListener(new View.OnClickListener() {
      //      @Override
         //   public void onClick(View v) {
         //       beaconManager = BeaconManager.getInstanceForApplication(BeaconPage.this);
          //      beaconManager.getBeaconParsers().add(new BeaconParser().
          //              setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
          //      beaconManager.bind(BeaconPage.this);
          //      onBeaconServiceConnect();
          //      Toast.makeText(getBaseContext(), "Beacon-scanner på", Toast.LENGTH_LONG).show();
        //    }
    //    });
      //  stopp.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View v) {
//
      //          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
      //          Toast.makeText(getBaseContext(), "Beacon-scanner av", Toast.LENGTH_LONG).show();
      //      }
    //    });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
        //    @Override

         //   public void didEnterRegion(Region region) {

          //      intent = new Intent(getApplicationContext(), EggAndBeacon.class);
          //      intent.putExtra("uuid", region.getId1().toString());
          //      intent.putExtra("major", region.getId2().toString());
          //      intent.putExtra("minor", region.getId3().toString());

         //       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //      startActivity(intent);
          //      Log.i(TAG, "Beacon!!!!!!" + region.getId1().toString());
          //      Log.i(TAG, "Yeeeey!!!!!!" + region.getId2().toString());
          //      Log.i(TAG, "Jiiiiihaaaaa!!!!" + region.getId3().toString());

           //     Boolean beacon = true;
           //     if (beacon == true) {
           //         try {
            //            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
             //           beaconManager.setRangeNotifier(this);
              //          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
              //          beaconManager.stopMonitoringBeaconsInRegion(region1);
              //          beaconManager.stopMonitoringBeaconsInRegion(region2);
              //      } catch (RemoteException e) {
              //          e.printStackTrace();
             //       }
            //    }
         //   }

             @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    if (beacon.getDistance() < 1.5) {
                        Log.d(TAG, "Det er er beacon en halvannen meter unna");
                        try {
                            beaconManager.stopRangingBeaconsInRegion(region1);
                            beaconManager.stopRangingBeaconsInRegion(region2);


                            intent = new Intent(getApplicationContext(), EggAndBeacon.class);
                            intent.putExtra("uuid", beacon.getId1().toString());
                            intent.putExtra("major", beacon.getId2().toString());
                            intent.putExtra("minor", beacon.getId3().toString());
                            intent.putExtra("distance", beacon.getDistance());

                           // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } catch (RemoteException e) {
                                 e.printStackTrace();
                               }
                    }
                }
            }

       //     @Override
     //       public void didExitRegion(Region region) {
     //           Log.i(TAG, "Kor e beacon?");
     //       }

        //    @Override
     //       public void didDetermineStateForRegion(int state, Region region) {
     //           Log.i(TAG, "Eg så ein beacon/beacon blei borte " + state);
    //        }
        });

        try {
            region1 = new Region("myIdentifier1", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("1"));
            region2 = new Region("myIdentifier2", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"), Identifier.parse("2"));

      //      beaconManager.startMonitoringBeaconsInRegion(region1);
      //      beaconManager.startMonitoringBeaconsInRegion(region2);
            beaconManager.startRangingBeaconsInRegion(region1);
            beaconManager.startRangingBeaconsInRegion(region2);

            // beaconManager.startMonitoringBeaconsInRegion(new Region("com.example.inger.beaconbeachler.boostrapRegion",
                //    Identifier.parse(Constants.BT_UUID),
               //     Identifier.fromInt(Constants.BT_MAJOR),
               //     Identifier.fromInt(Constants.BT_MINOR)));

        } catch (RemoteException e) { e.printStackTrace(); }
    }


}


