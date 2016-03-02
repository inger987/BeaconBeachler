package com.example.inger.beaconbeachler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;

import static android.media.MediaRecorder.AudioSource.*;

public class Lyd extends Activity {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    String myFileName = "";
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button stopPlayBtn;
    public Button lagre;
    private TextView recordingPoint;
    public TextView txtUploadprogress;
    public TextView txtUsername;

    String UploadServerUri = null;

    private String UPLOAD_URL = "https://home.hbv.no/110030/lyd/uploadToServer.php";
    private String UPLOAD_KEY = "audio";
    private String KEY_USERID = "userId";
    private String FILNAVN = "filnavn";
    private String AUDIO_RECORDER_FOLDER = "/storage/emulated/0/";

    final String uploadFileName = "lydfil";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyd_activity);

        // to determine the named of current logged in user
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");
        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtUsername.setText("Current User: " + username);

        // Show current recording point
        recordingPoint = (TextView) findViewById(R.id.recordingPoint);
        recordingPoint = (TextView) findViewById(R.id.recordingPoint);

        // Shows current uploading point
        txtUploadprogress = (TextView) findViewById(R.id.txtUploadprogress);
        txtUploadprogress.setText("Uploading file path :- '\"/storage/emulated/0/\"" + uploadFileName + "'");

        // Store file on SD card
        myFileName = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/storage/emulated/0/";

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
                uploadFile(myFileName);
            }
        });

        // Php script path
        UploadServerUri = "https://home.hbv.no/110030/lyd/UploadToServer.php";
    }

    public void start(View view) {

        myFileName = getFilename();

        recordingPoint = (TextView) findViewById(R.id.recordingPoint);
        recordingPoint.setText(": Tar opp lyd");

        // Store file on SD card
        myFileName = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/lydfil.mp4";

        myFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lydfil.mp4";
        FileOutputStream fos;
        byte[] data = new String("data to write to file").getBytes();
        try {
            fos = new FileOutputStream(myFileName);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }
        // Everytime start is click; intiate myRecorder
        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myRecorder.setOutputFile(myFileName);


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
            myPlayer.setDataSource(myFileName);
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

                myFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lydfil";

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MMM-yyyy hh-mm-ss");
        String currentDateandTime = sdfDate.format(new Date());

        if (!file.exists()) {
            file.mkdirs();
        }
        return (file.getAbsolutePath() + "/" + currentDateandTime + ".mp4");
    }

    public void uploadFile(final String myFileName){

        class uploadFile extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              loading = ProgressDialog.show(Lyd.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                //  Bitmap bitmap = params[0];

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                final String format = simpleDateFormat.format(new Date());
                final String uploadFile = myFileName;

                SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                final String username = sharedPreferences.getString(Config.USERNAME_SHARED_PREF, "Not Available");

                HashMap<String, String> data = new HashMap<String, String>()
                {{
                    put(UPLOAD_KEY, uploadFile);
                    put(FILNAVN, format);
                    put(KEY_USERID, username);


                }};

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        uploadFile ui = new uploadFile();
        ui.execute();
    }
}