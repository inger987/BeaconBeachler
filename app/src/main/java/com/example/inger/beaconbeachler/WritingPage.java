package com.example.inger.beaconbeachler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Marielle on 10-Jan-16.
 */
public class WritingPage extends AppCompatActivity implements View.OnClickListener {

    private static final String INSERTWRITING_URL = "https://home.hbv.no/110118/bachelor/insertWriting.php";
    public static final String KEY_TEXT = "text";
    public static final String KEY_USERID = "userId";

    private EditText etText;
    private Button btnSave;
    private Button emtyetText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_page);

        etText = (EditText) findViewById(R.id.etText);
        btnSave = (Button) findViewById(R.id.btnSave);
        emtyetText = (Button) findViewById(R.id.emtyetText);


        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                insertText();
            case R.id.emtyetText:
                emtyTextView();
                break;
        }
    }

    private void emtyTextView() {

            etText.setText(null);
    }

    private void insertText() {
        // har lagret brukernavnet på den som er logget inn. Dette brukes til å kjenne igjen brukere med spørring i php
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        final String text = etText.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSERTWRITING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(WritingPage.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WritingPage.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TEXT,text);
                params.put(KEY_USERID,username);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
