package com.example.inger.beaconbeachler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegsiterPage extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistrer;
    EditText etRFistname,etRLastname,etRUsername,etRPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter_page);
        etRFistname = (EditText)findViewById(R.id.etRFirstname);
        etRLastname = (EditText)findViewById(R.id.etRLastname);
        etRUsername = (EditText)findViewById(R.id.etRUsername);
        etRPassword = (EditText)findViewById(R.id.etRPassword);
        btnRegistrer = (Button)findViewById(R.id.btnRegistrer);

        btnRegistrer.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegistrer:

                break;
        }
    }
}
