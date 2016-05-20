package com.example.inger.beaconbeachler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Elin on 01.03.2016.
 */
public class EggAndBeacon extends AppCompatActivity {


    private static final String KEY_CATID = "categoryId";
    private static final String UPLOAD_URL = "https://home.hbv.no/110118/bachelor/beaconinfo.php";
    private static final String KEY_PICTURE = "picture";
    private static final String KEY_INFOTEXT = "infoText";
    private static final String KEY_TITLE = "title";
    public static final String JSON_ARRAY = "result";
    private String minor;

    TextView textViewTitle,textViewInfo;
    ImageView imageViewBeacon;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggandbeacon);
        Bundle extras = getIntent().getExtras();
        String uuid = extras.getString("uuid");
        String id1 = extras.getString("major");
        minor = extras.getString("minor");
        String ds = extras.getString("distance");

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewInfo = (TextView) findViewById(R.id.textViewInfo);
        imageViewBeacon = (ImageView) findViewById(R.id.imageViewBeacon);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_CATID,minor);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    /*    TextView tekst = new TextView(this);
        tekst.setTextSize(7 * getResources().getDisplayMetrics().density);
        tekst.setMovementMethod(new ScrollingMovementMethod());

        overskrift.setTextSize(6 * getResources().getDisplayMetrics().density);

        TextView beacon = (TextView) findViewById(R.id.beacon);
        beacon.setTextSize(7 * getResources().getDisplayMetrics().density);

        ImageView imageView = (ImageView)findViewById(R.id.bilde);
        if (id2.equals("1")) {
            imageView.setImageResource(R.mipmap.vrbriller);
            overskrift.setText("VIRITUELL VIRKELIGHET");
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + "\n" + ds + "\n" +
                    "Kunstig virkelighet eller virtuell virkelighet (etter det engelske virtual reality), ofte forkortet VR, " +
                    "er en datateknologi som lar brukeren påvirke og bli påvirket av et dataskapt miljø som skal etterlikne " +
                    "en virkelighet. De fleste «miljøene» med kunstig virkelighet omfatter både syns- og lydinntrykk og blir vist" +
                    " på en skjerm eller gjennom et spesielt, apperat. Det mest kjente apperatet er VR briller (som vist på bildet)." +
                    " Ved å bruke brillene tilater teknologien brukeren å se seg 360 grader rundt i den dataskapte virkeligheten. ");
        }

        else if (id2.equals("2")) {
            imageView.setImageResource(R.mipmap.unhand);
            overskrift.setText("UNLIMITED HAND");
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Dette er en teknologi som kan brukes sammen med VR briller. Du plaserer sensoren rundt armen(som vist på bildet) " +
                    "og den finner ut hvordan og hvor du beveger hånden. Dette gjør det mulig å bruke hendene når du spiller " +
                    "med VR-briller. Du kan for eksempel klappe dyr, bygge, bruke skytevåpen og mye mer. Kun fantasien setter grenser.");
        }

        else if (id2.equals("3")) {
            imageView.setImageResource(R.mipmap.contactlens);
            overskrift.setText("GOOGLE KONTAKTLINSER");
            view.setText(uuid + "\n+" + id1 + "\n" + id2 + "\n" + ds + "\n" +
                    "Google kontaktliser er en smartteknologi som er spesielt utviklet for personer med diabetes." +
                    " Den sjekker konstant glukose(sukker) - nivået i tårene. Produktet er enda ikke på markedet, " +
                    "men under testing.");

        }

        else if (id2.equals("4")) {
            imageView.setImageResource(R.mipmap.driverless);
            overskrift.setText("FØRERLØS BIL");
            view.setText(uuid +"\n+"+ id1 +"\n"+ id2 +"\n"+ ds +"\n" +
                    "Tenk deg biler uten fører! Ny teknologi, tankegang og forskning gjør dette mulig. " +
                    "Ved hjelp av kameraer og sensorer som er plassert rundt bilen, leser den trafikkskilt, " +
                    "veimerkering og andre bilers plassering. \n" +
                    "Eksperter mener dette er fremtiden, og at det vil føre til færre trafikkulykker. ");
        } */



    }

    private void getJson(String response){
        String title = "";
        String picture = "";
        String infoText = "";
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject beaconData = result.getJSONObject(0);
            title = beaconData.getString(KEY_TITLE);
            picture = beaconData.getString(KEY_PICTURE);
            infoText = beaconData.getString(KEY_INFOTEXT);

        } catch(JSONException e){
            e.printStackTrace();
        }
        textViewTitle.setText(title);
        textViewInfo.setText(infoText);

        Picasso.with(this).load(picture).resize(900,600).into(imageViewBeacon);

    }


    public void onBackPressed() {
        Intent intent = new Intent(this,MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}