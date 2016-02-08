package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class RegsiterPage extends AppCompatActivity implements View.OnClickListener {

    Button btnRegistrer;
    EditText etRFistname,etRLastname,etRUsername,etRPassword;
    private static final String REGISTER_URL = "https://home.hbv.no/110118/bachelor/register.php";
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
                registerUser();
                break;
        }
    }
    private void registerUser(){
        String firstName = etRFistname.getText().toString().trim().toLowerCase();
        String lastName = etRLastname.getText().toString().trim().toLowerCase();
        String username = etRUsername.getText().toString().trim().toLowerCase();
        String password = etRPassword.getText().toString().trim().toLowerCase();

        register(firstName,lastName,username,password);
    }

    private void register(String firstName,String lastName, String username, String password) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegsiterPage.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("firstName",params[0]);
                data.put("lastName",params[1]);
                data.put("username",params[2]);
                data.put("password",params[3]);

                String result = ruc.sendPostRequest(REGISTER_URL,data);

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(firstName,lastName,username,password);
    }
}