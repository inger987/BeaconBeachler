package com.example.inger.beaconbeachler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin,btnNewUser;
    EditText etUsername,etPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        btnLogin =(Button)findViewById(R.id.btnLogin);
        etUsername = (EditText)findViewById(R.id.etUsername);
        etPassword = (EditText)findViewById(R.id.etPassword);
        btnNewUser = (Button) findViewById(R.id.btnNewUser);
        btnLogin.setOnClickListener(this);
        btnNewUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                startActivity(new Intent(LoginPage.this,MainPage.class));
                break;
            case R.id.btnNewUser:
                startActivity(new Intent(LoginPage.this,RegsiterPage.class));
                break;
        }
    }
}
