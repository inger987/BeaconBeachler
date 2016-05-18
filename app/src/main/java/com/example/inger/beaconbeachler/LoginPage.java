package com.example.inger.beaconbeachler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private TextView textViewRegistrer;
    private EditText etUsername, etPassword;
    private LoginButton btnFacebook;
    private boolean loggedIn = false;

    // FACEBOOK VARIABLES
    private String facebookID;
    private String firstName;
    private String lastName;
    private String passwordF;

    CallbackManager callbackManager;
    Config config;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login_page);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        textViewRegistrer = (TextView) findViewById(R.id.textViewRegistrer);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        textViewRegistrer.setOnClickListener(this);

        btnFacebook = (LoginButton) findViewById(R.id.login_button);
        btnFacebook.setReadPermissions("public_profile");


        btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {

                                Log.e("response: ", response + "");
                                try {
                                   // config = new Config();
                                    facebookID = object.getString("id").toString();
                                    firstName = object.getString("first_name").toString();
                                    lastName = object.getString("last_name").toString();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                passwordF = firstName + Math.random() * 50;
                                loggedInShare();
                                finish();
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.textViewRegistrer:
                startActivity(new Intent(LoginPage.this, RegsiterPage.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //In onresume fetching value from sharedpreference
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences, false is a standard value
        loggedIn = sharedPreferences.getBoolean(Config.LOGGEDIN_SHARED_PREF, false);

        //If we will get true

        if (loggedIn) {
            //We will start the MainPage Activity
            Intent intent = new Intent(LoginPage.this, MainPage.class);
            startActivity(intent);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void loggedInShare() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(LoginPage.this,response,Toast.LENGTH_LONG).show();
                        if (response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                            SharedPreferences sharedPreferences = LoginPage.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.USERNAME_SHARED_PREF, facebookID);

                            //Saving values to editor
                            editor.commit();
                            Intent intent = new Intent(LoginPage.this, MainPage.class);
                            startActivity(intent);
                            Toast.makeText(LoginPage.this, "Velkommen " + firstName + " " + lastName, Toast.LENGTH_LONG).show();
                        }else
                            Toast.makeText(LoginPage.this, "Noe gikk galt, prøv igjen " , Toast.LENGTH_LONG).show();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_USERNAME, facebookID);
                params.put(Config.KEY_PASSWORD, passwordF);
                params.put(Config.KEY_FIRSTNAME, firstName);
                params.put(Config.KEY_LASTNAME, lastName);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void login() {
        //Getting values from edittext
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
    /*    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // response
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Error
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USERNAME, facebookID);
                params.put(Config.KEY_PASSWORD, passwordF);
                params.put(Config.KEY_FIRSTNAME, firstName);
                params.put(Config.KEY_LASTNAME, lastName);
                return params;
            }
        };*/

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //If we are getting success from server
                        if (response.trim().equalsIgnoreCase(Config.LOGIN_SUCCESS)) {
                            //Creating a sharedPreference
                            SharedPreferences sharedPreferences = LoginPage.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                            //Creating editor to store values to shared preferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            //Adding values to editor
                            editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, true);
                            editor.putString(Config.USERNAME_SHARED_PREF, username);

                            //Saving values to editor
                            editor.commit();

                            //Starting mainPage activity
                            Intent intent = new Intent(LoginPage.this, MainPage.class);
                            startActivity(intent);
                        } else {
                            //If the server response is not success
                            //Displaying an error message on toast
                            Toast.makeText(LoginPage.this, "Feil brukernavn eller passord", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(Config.KEY_USERNAME, username);
                params.put(Config.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.inger.beaconbeachler/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "LoginPage Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.inger.beaconbeachler/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
