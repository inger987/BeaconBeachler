package com.example.inger.beaconbeachler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

/**
 * Created by Marielle on 18-May-16.
 */
public class Menu extends AppCompatActivity {

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Er du sikker på at du ønsker å logge ut?");
        alertDialogBuilder.setPositiveButton("JA",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to username and minor
                        editor.putString(Config.USERNAME_SHARED_PREF, "");
                        //  editor.putString(Config.KEY_MINOR,"");
                        //Saving the sharedpreferences
                        editor.commit();

                        LoginManager.getInstance().logOut();

                        //Starting login activity
                        //   Intent intent = new Intent(this, LoginPage.class);
                        startActivity(new Intent(Menu.this,LoginPage.class));
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
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
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

        switch (id){
            case R.id.action_sound:
                startActivity(new Intent(this, AudioPage.class));
                break;
            case R.id.action_camera:
                startActivity(new Intent(this, CameraPage.class));
                break;
            case R.id.action_text:
                startActivity(new Intent(this, WritingPage.class));
                break;
            case R.id.action_beacon:
                startActivity(new Intent(this, BeaconPage.class));
                break;
            case R.id.action_settings:
                logout();
                break;
            case R.id.action_favorite:
                showprofil();
                break;
        }

        return super.onOptionsItemSelected(item);
    }




}
