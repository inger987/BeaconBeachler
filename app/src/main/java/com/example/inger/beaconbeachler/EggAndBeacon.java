package com.example.inger.beaconbeachler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Elin on 01.03.2016.
 */
public class EggAndBeacon extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggandbeacon);
        Bundle extras = getIntent().getExtras();
        String uuid = extras.getString("uuid");
        String id1 = extras.getString("major");
        String id2 = extras.getString("minor");
        String ds = extras.getString("distance");
        TextView view = (TextView) findViewById(R.id.tekst);

        ImageView imageView = (ImageView)findViewById(R.id.bilde);
        if (id2.equals("1")) {
            imageView.setImageResource(R.drawable.eggogbeacon);
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + "\n" + ds +"\n" +
                    "Egg og beacon inneholder mange viktige næringsstoffer, og anbefales av LHL som en sunn frokost");
        }
        else if (id2.equals("2")) {
            imageView.setImageResource(R.drawable.miiko);
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Fakta om kuer: "+"\n" +"\n"+
                    "-Kuer sier mø"+"\n"+
                    "-Kuer spiser grass" +"\n"+
                    "-Kuer blir til hamburgere når de dør");
        }
        else if (id2.equals("3")) {
            imageView.setImageResource(R.drawable.miiko);
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + ds + "\n" +
                    "Beacon 3!!!");
        }
        else if (id2.equals("4")) {
            imageView.setImageResource(R.drawable.miiko);
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Enda en beacon!");
        }



    }
    public void onBackPressed() {
        Intent intent = new Intent(this,MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}