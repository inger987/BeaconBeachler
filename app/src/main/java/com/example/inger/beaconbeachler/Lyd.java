package com.example.inger.beaconbeachler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import com.android.volley.RequestQueue;
import static android.media.MediaRecorder.AudioSource.*;

public class Lyd extends Activity {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;

    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button stopPlayBtn;
    public Button lagre;

    private TextView recordingPoint;
    public TextView txtUploadprogress;
    public TextView txtUsername;
    public TextView textView6;

    public String outputfile = null;
    private String UPLOAD_URL = "https://home.hbv.no/110030/lyd/uploadToServer.php";
    private String UPLOAD_KEY = "audio";
    private String KEY_USERID = "userId";
    private String FILNAVN = "filnavn";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyd_activity);

        // to determine the named of current logged in user
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtUsername.setText("Current User: " + username);

        textView6 = (TextView) findViewById(R.id.textView6);

        // Show current recording point
        recordingPoint = (TextView) findViewById(R.id.recordingPoint);
        recordingPoint = (TextView) findViewById(R.id.recordingPoint);

        // Shows current uploading point
        txtUploadprogress = (TextView) findViewById(R.id.txtUploadprogress);


        startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //  startBtn.setVisibility(View.GONE);
                //  stopBtn.setVisibility(View.VISIBLE);
                start(v);
            }
        });

        stopBtn = (Button) findViewById(R.id.stop);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
                //stopBtn.setVisibility(View.GONE);
                //playBtn.setVisibility(View.VISIBLE);
            }
        });


        playBtn = (Button) findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
                //playBtn.setVisibility(View.GONE);
                //stopBtn.setVisibility(View.VISIBLE);
            }
        });

        stopPlayBtn = (Button) findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
                //stopBtn.setVisibility(View.GONE);
                //startBtn.setVisibility(View.VISIBLE);
            }
        });

        lagre = (Button) findViewById(R.id.Lagre);
        lagre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });

    }

    public void start(View view) {

        outputfile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + File.separator +System.currentTimeMillis() + ".mp3";

        //Shows fileName --> for testing purpose
        String fileName = new File(outputfile).getName();
        txtUploadprogress.setText(fileName);


        recordingPoint = (TextView) findViewById(R.id.recordingPoint);
        recordingPoint.setText(": Tar opp lyd");

        // Everytime start is click; intiate myRecorder
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
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


        //startBtn.setEnabled(false);
        stopBtn.setEnabled(true);
        playBtn.setEnabled(false);
        stopPlayBtn.setEnabled(false);

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();

    }

    public void stop(View view) {
        try {
            myRecorder.stop();
            //  myRecorder.release();
            //     myRecorder.reset();
            //  myRecorder  = null;

            startBtn.setEnabled(true);
            playBtn.setEnabled(true);

            // startBtn.setVisibility(View.VISIBLE);
            //stopBtn.setVisibility(View.GONE);

            recordingPoint.setText(": Recording stopped");

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

    public void play(View view) {
        try {
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputfile);
            myPlayer.prepare();
            myPlayer.start();

            playBtn.setEnabled(false);
            stopPlayBtn.setEnabled(true);

            recordingPoint.setText("Recording Point: Playing");

            Toast.makeText(getApplicationContext(), "Start play the recording...",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myRecorder.reset();

                myPlayer = null;

                playBtn.setEnabled(true);
                stopPlayBtn.setEnabled(false);
                startBtn.setEnabled(true);

                recordingPoint.setText("Recording Point: Stop playing");

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();

                myPlayer = null;

                uploadFile1();
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

    public void uploadFile1() {

        class uploadFile1 extends AsyncTask<String, Void, String> {

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

                SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
                final String currentDateandTime = sdfDate.format(new Date());
                final String uploadfile = getStringAudio();

                HashMap<String, String> data = new HashMap<String, String>() {{
                    put(UPLOAD_KEY, uploadfile);
                    put(FILNAVN, currentDateandTime);
                    put(KEY_USERID, username);


                }};

                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        uploadFile1 ui = new uploadFile1();
        ui.execute();
    }
}
