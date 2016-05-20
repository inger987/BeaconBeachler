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
        minor = extras.getString("minor");

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