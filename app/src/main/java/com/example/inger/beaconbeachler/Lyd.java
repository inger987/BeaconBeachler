package com.example.inger.beaconbeachler;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Lyd extends Menu implements View.OnClickListener {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;

    private Button btnStart, btnStop, btnPlay, btnStopPlay, btnLagre;
    public TextView tvTimer;

    long startTime = 0;

    public String outputfile = null;
    private String UPLOAD_URL ="https://home.hbv.no/110115/bac/uploadToServer.php";
    private String UPLOAD_KEY = "audio";
    private String KEY_USERID = "userId";
    private String KEY_CATID = "categoryId";
    private String FILNAVN = "filnavn";

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
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringAct(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringAct(null);
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

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(outputfile);

        try {
            myRecorder.prepare();
            myRecorder.start();

        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();
    }

    public void stopRecording(View v){
        try {
            myRecorder.stop();
            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();

        } catch (IllegalStateException e) {
            //  it is called before start()
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

            Toast.makeText(getApplicationContext(), "Start play the recording...",
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

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
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

        class uploadFile extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Lyd.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

                SharedPreferences sharedPref = getSharedPreferences(Config.KEY_MINOR, Context.MODE_PRIVATE);
                final String minor = sharedPref.getString(Config.KEY_MINOR, "Not Available");


                SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
                final String currentDateandTime = sdfDate.format(new Date());
                final String uploadfile = getStringAudio();

                HashMap<String, String> data = new HashMap<String, String>() {{
                    put(UPLOAD_KEY, uploadfile);
                    put(FILNAVN, currentDateandTime);
                    put(KEY_USERID, username);
                    put(KEY_CATID, minor);
                }};

                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        uploadFile ui = new uploadFile();
        ui.execute();
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
