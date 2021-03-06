package com.example.inger.beaconbeachler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AudioPage extends Menu implements View.OnClickListener {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;

    private Button btnStart, btnStop, btnPlay, btnStopPlay, btnLagre;
    public TextView tvTimer;

    long startTime = 0;

    public String outputfile = null;
    private String username ="";
    private String minor ="";
    private String uploadfile ="";
    private String currentDateandTime ="";

    private static String AUDIO_URL ="https://home.hbv.no/110115/bac/uploadToServer.php";
    private static String UPLOAD_KEY = "audio";
    private static String KEY_USERID = "userId";
    private static String KEY_CATID = "categoryId";
    private static String FILNAVN = "filnavn";

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            tvTimer.setText(String.format("%2S:%02d:%02d","00", minutes,seconds));
            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyd_activity);

        tvTimer = (TextView)findViewById(R.id.tvTimer);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStopPlay = (Button) findViewById(R.id.btnStopPlay);
        btnLagre = (Button) findViewById(R.id.btnLagre);

        btnLagre.setEnabled(false);
        btnPlay.setEnabled(false);

        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStopPlay.setOnClickListener(this);
        btnLagre.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

       ((BeaconReference) this.getApplicationContext()).setMonitoringAct(this);

    }

    @Override
    public void onPause() {
        super.onPause();

       ((BeaconReference) this.getApplicationContext()).setMonitoringAct(null);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnStart:
                startRecording(v);
                btnStart.setVisibility(v.INVISIBLE);
                btnStop.setVisibility(v.VISIBLE);

                btnPlay.setEnabled(false);

                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                break;

            case R.id.btnStop:
                stopRecording(v);
                btnStop.setVisibility(v.INVISIBLE);
                btnStart.setVisibility(v.VISIBLE);

                btnLagre.setEnabled(true);
                btnPlay.setEnabled(true);

                timerHandler.removeCallbacks(timerRunnable);
                break;

            case R.id.btnPlay:
                startPlayback(v);
                btnPlay.setVisibility(v.INVISIBLE);
                btnStopPlay.setVisibility(v.VISIBLE);
                break;

            case R.id.btnStopPlay:
                stopPlayback(v);
                btnStopPlay.setVisibility(v.INVISIBLE);
                btnPlay.setVisibility(v.VISIBLE);
                break;

            case R.id.btnLagre:
                UploadFile();
                break;
        }

    }

    public void startRecording(View v){
        outputfile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + File.separator +System.currentTimeMillis() + ".mp3";

        //initialize MediaPlayer
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(outputfile);

        try {
            myRecorder.prepare();
            myRecorder.start();

        } catch (IllegalStateException e) {
            // Exp: start:it is called before prepare()
            // Exp: prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Starter lydopptaket",
                Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View v){
        try {
            myRecorder.stop();
            Toast.makeText(getApplicationContext(), "Stopper lydopptaket",
                    Toast.LENGTH_SHORT).show();

        } catch (IllegalStateException e) {
            //  Exp: stop() is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }

    }

    public void startPlayback(View v){
        try {
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputfile);
            myPlayer.prepare();
            myPlayer.start();

            Toast.makeText(getApplicationContext(), "Starter avspilling",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void stopPlayback(View v){
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myRecorder.reset();
                myPlayer = null;

                Toast.makeText(getApplicationContext(), "Stopper avspilling",
                        Toast.LENGTH_SHORT).show();

                myPlayer = null;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String getStringAudio() {
        String outputFile = outputfile;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(outputFile));
            byte[] buf = new byte[1024];
            int n;
            while ((n = fis.read(buf)) != -1) {
                bos.write(buf, 0, n);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String base64EncodedString = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
        return base64EncodedString;
    }

    public void UploadFile() {
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences settings = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        minor = settings.getString(Config.KEY_MINOR, "5");

        if (minor == null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            //Adding values to editor
            editor.putString(Config.KEY_MINOR, "5");
        }


        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
        currentDateandTime = sdfDate.format(new Date());
        uploadfile = getStringAudio();

       StringRequest stringRequest = new StringRequest(Request.Method.POST, AUDIO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AudioPage.this, response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AudioPage.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(UPLOAD_KEY, uploadfile);
                params.put(FILNAVN, currentDateandTime);
                params.put(KEY_USERID, username);
                params.put(KEY_CATID, minor);
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
