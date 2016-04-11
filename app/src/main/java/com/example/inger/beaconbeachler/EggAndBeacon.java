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
            imageView.setImageResource(R.drawable.vr_briller);
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + "\n" + ds + "\n" +
                    "Kunstig virkelighet eller virtuell virkelighet (etter det engelske virtual reality), ofte forkortet VR, " +
                    "er en datateknologi som lar brukeren påvirke og bli påvirket av et dataskapt miljø som skal etterlikne " +
                    "en virkelighet. De fleste «miljøene» med kunstig virkelighet omfatter både syns- og lydinntrykk og blir vist" +
                    " på en skjerm eller gjennom et spesielt, apperat. Det mest kjente apperatet er VR briller (som vist på bildet)." +
                    " Ved å bruke brillene tilater teknologien brukeren å se seg 360 grader rundt i den dataskapte virkeligheten. ");
        }

        else if (id2.equals("2")) {
            imageView.setImageResource(R.drawable.un_hand);
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Dette er en teknologi som kan brukes sammen med VR briller. Du plaserer sensoren rundt armen(som vist på bildet) " +
                    "og den finner ut hvordan og hvor du beveger hånden. Dette gjør det mulig å bruke hendene når du spiller " +
                    "med VR-briller. Du kan for eksempel klappe dyr, bygge, bruke skytevåpen og mye mer. Kun fantasien setter grenser.");
        }

        else if (id2.equals("3")) {
            imageView.setImageResource(R.drawable.contact_lens);
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + ds + "\n" +
                    "Google kontaktliser er en smartteknologi som er spesielt utviklet for personer med diabetes." +
                    " Den sjekker konstant glukose(sukker) - nivået i tårene. Produktet er enda ikke på markedet, " +
                    "men under testing.");

        }

        else if (id2.equals("4")) {
            imageView.setImageResource(R.drawable.driverless_car);
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Tenk deg biler uten fører! Ny teknologi, tankegang og forskning gjør dette mulig. " +
                    "Ved hjelp av kameraer og sensorer som er plassert rundt bilen, leser den trafikkskilt, " +
                    "veimerkering og andre bilers plassering. \n" +
                    "Eksperter mener dette er fremtiden, og at det vil føre til færre trafikkulykker. ");
        }



    }
    public void onBackPressed() {
        Intent intent = new Intent(this,MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}