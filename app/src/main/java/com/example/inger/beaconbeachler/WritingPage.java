package com.example.inger.beaconbeachler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
public class WritingPage extends com.example.inger.beaconbeachler.Menu implements View.OnClickListener {

    private static final String INSERTWRITING_URL = "https://home.hbv.no/110118/bachelor/insertText.php";
    public static final String KEY_TEXT = "text";
    public static final String KEY_USERID = "userId";
    private static final String KEY_CAT = "categoryId";

    private EditText etText;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_page);

        etText = (EditText) findViewById(R.id.etText);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(this);

        ((BeaconReference) this.getApplicationContext()).setMonitoriact(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                insertText();
        }
    }

    private void insertText() {
        // Store username of current logged in user (To connect user in database)
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        final String minor = sharedPreferences.getString(Config.KEY_MINOR, "5");
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
                params.put(KEY_CAT, minor);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed() {
        Intent intent = new Intent(this,MainPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

}
