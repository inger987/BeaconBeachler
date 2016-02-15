
package com.example.inger.beaconbeachler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;

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
    private String outputFile = null;
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button stopPlayBtn;
    public Button lagre;
    private TextView text;
    public TextView textView6;

    String upLoadServerUri = null;

    private String UPLOAD_URL = "https://home.hbv.no/110030/lyd/UploadToServer.php";
    private String UPLOAD_KEY = "audio";
    private String FILNAVN = "lyd";

    /**********  File Path *************/
    final String uploadFilePath = "/storage/emulated/0/";
    // "/mnt/sdcard/";

    final String uploadFileName = "lydfil.3gpp";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_lyd_activity);

        text = (TextView) findViewById(R.id.text1);
        // lagrer pÃ¥ minnekort
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/lydfil.3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(outputFile);
/*
        startBtn.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);
        playBtn.setVisibility(View.INVISIBLE);
        stopPlayBtn.setVisibility(View.INVISIBLE);
*/

        startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.VISIBLE);
                start(v);
            }
        });

        textView6 = (TextView) findViewById(R.id.textView6);
        stopBtn = (Button) findViewById(R.id.stop);
        stopBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stop(v);
                stopBtn.setVisibility(View.INVISIBLE);
                playBtn.setVisibility(View.VISIBLE);
            }
        });

        lagre = (Button) findViewById(R.id.Lagre);
        lagre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                        uploadFile();
                    }
        });


        playBtn = (Button) findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
                playBtn.setVisibility(View.INVISIBLE);
                stopPlayBtn.setVisibility(View.VISIBLE);
                startBtn.setVisibility(View.INVISIBLE);
            }
        });

        stopPlayBtn = (Button) findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
                stopBtn.setVisibility(View.INVISIBLE);
                stopPlayBtn.setVisibility(View.INVISIBLE);
                startBtn.setVisibility(View.VISIBLE);
            }
        });

       // textView6.setText("Uploading file path :- '\"/storage/emulated/0/\"" + uploadFileName + "'");

        /************* Php script path ****************/
        upLoadServerUri = "https://home.hbv.no/110030/lyd/UploadToServer1.php";
    }

    public void start(View view) {

        text = (TextView) findViewById(R.id.text1);
        // lagrer pÃ¥ minnekort
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/lydfil.3gpp";

        //   String filename = "lydfil.3gpp";
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lydfil.3gpp";
        FileOutputStream fos;
        byte[] data = new String("data to write to file").getBytes();
        try {
            fos = new FileOutputStream(outputFile);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            // handle exception
        } catch (IOException e) {
            // handle exception
        }

    /*    myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(outputFile);
       */


        try {
            //  myRecorder.reset();
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

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();


    }

    public void stop(View view) {
        try {
            myRecorder.stop();
            //  myRecorder.release();
            //     myRecorder.reset();
            //  myRecorder  = null;


            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            startBtn.setVisibility(View.VISIBLE);
            stopBtn.setVisibility(View.GONE);
            text.setText("Recording Point: Stop recording");

            Toast.makeText(getApplicationContext(), "Stop recording...",
                    Toast.LENGTH_SHORT).show();
            //    outputFile = Environment.getExternalStorageDirectory().
            //          getAbsolutePath() + "/lydfil.3gpp";
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
            myPlayer.setDataSource(outputFile);
            myPlayer.prepare();
            myPlayer.start();

            playBtn.setEnabled(false);
            stopPlayBtn.setEnabled(true);
            text.setText("Recording Point: Playing");

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
                text.setText("Recording Point: Stop playing");

                Toast.makeText(getApplicationContext(), "Stop playing the recording...",
                        Toast.LENGTH_SHORT).show();

                myPlayer = null;
                startBtn.setEnabled(true);

                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lydfil.3gpp";

             //   String fileName = "lydfil.3gpp";

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void uploadFile(){
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

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                final String format = simpleDateFormat.format(new Date());
                final String uploadFile = outputFile;

                HashMap<String, String> data = new HashMap<String, String>()
                {{
                    put(UPLOAD_KEY, uploadFile);
                    put(FILNAVN, format);

                }};


                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        uploadFile ui = new uploadFile();
        ui.execute();
    }
}
