package com.example.inger.beaconbeachler;

import android.app.Activity;
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
        TextView view = (TextView) findViewById(R.id.tekst);
        view.setText(uuid +"\n+"+ id1+"\n"+id2);
        ImageView imageView = (ImageView)findViewById(R.id.bilde);
        if (id2.equals("1")) {
            imageView.setImageResource(R.drawable.eggogbeacon);
        }
        else if (id2.equals("2")) {
            imageView.setImageResource(R.drawable.miiko);
        }



    }
}