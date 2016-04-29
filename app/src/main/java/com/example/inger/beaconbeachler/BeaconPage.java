package com.example.inger.beaconbeachler;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;


public class BeaconPage extends AppCompatActivity implements BeaconConsumer{
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    public static Context mContext;

    Region region1;
    Intent intent;
    ProgressDialog loading;
    private Handler mHandler;

  //  String minor;

    public static Context getContext() {
        //  return instance.getApplicationContext();
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_beacon_page);
        loading = ProgressDialog.show(BeaconPage.this, "Leter etter beacons", null,true,true);

        beaconManager.bind(this);
        mHandler = new Handler();
        mContext = getApplicationContext();
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
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                beaconManager.stopRangingBeaconsInRegion(region1);

                loading.dismiss();
                //    intent = new Intent(getApplicationContext(), MainPage.class);
                //   intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //    startActivity(intent);

                ingenBeacon();
            } catch (RemoteException e) {
                e.printStackTrace();

            }
        }
    };
    public void ingenBeacon() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle( Html.fromHtml("<font color='#ffffff'>Ingen beacon funnet</font>"));
              //  builder.setMessage("Ingen beacons funnet")
                builder.setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent = new Intent(getApplicationContext(), MainPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(30);
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.WHITE);

    //    alert.getWindow().setLayout(500, 350);

    }


    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {

            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                for (Beacon beacon : beacons) {
                    if (beacon.getDistance() < 1.0) {
                        Log.d(TAG, "Det er er beacon en halvannen meter unna");
                        try {
                            beaconManager.stopRangingBeaconsInRegion(region1);
                            intent = new Intent(getApplicationContext(), EggAndBeacon.class);
                            intent.putExtra("uuid", beacon.getId1().toString());
                            intent.putExtra("major", beacon.getId2().toString());
                            intent.putExtra("minor", beacon.getId3().toString());
                            intent.putExtra("distance", beacon.getDistance());

                            // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                            loading.dismiss();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else { mHandler.postDelayed(mUpdateTimeTask, 2000);

                      //  try {
                      //
                     //       beaconManager.stopRangingBeaconsInRegion(region1);
                     //       beaconManager.stopRangingBeaconsInRegion(region2);

                     //       loading.dismiss();

                     //       intent = new Intent(getApplicationContext(), MainPage.class);
                     //       intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                     //       startActivity(intent);

                            //   ingenBeacon();
                    //    } catch (RemoteException e) {
                       //     e.printStackTrace();

                    //    }

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
            region1 = new Region("myIdentifier1", Identifier.parse("00000000-0000-0000-c000-000000000028"), Identifier.parse("1"),null);


            //      beaconManager.startMonitoringBeaconsInRegion(region1);
            //      beaconManager.startMonitoringBeaconsInRegion(region2);
            beaconManager.startRangingBeaconsInRegion(region1);



            // beaconManager.startMonitoringBeaconsInRegion(new Region("com.example.inger.beaconbeachler.boostrapRegion",
            //    Identifier.parse(Constants.BT_UUID),
            //     Identifier.fromInt(Constants.BT_MAJOR),
            //     Identifier.fromInt(Constants.BT_MINOR)));

        } catch (RemoteException e) {

            e.printStackTrace(); }

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
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.USERNAME_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(BeaconPage.this, LoginPage.class);
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
            startActivity(new Intent(BeaconPage.this, Lyd.class));
        }

        if (id== R.id.action_camera){
            startActivity(new Intent(BeaconPage.this, CameraPage.class));
        }

        if (id==R.id.action_text){
            startActivity(new Intent(BeaconPage.this, WritingPage.class));
        }

        if(id==R.id.action_beacon){
            startActivity(new Intent(BeaconPage.this, BeaconPage.class));
        }

        return super.onOptionsItemSelected(item);
    }


}
