
package com.example.inger.beaconbeachler;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresPermission;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import static android.media.MediaRecorder.AudioSource.*;
import static junit.framework.Assert.fail;

public class Lyd extends Activity {

    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    private String outputFile = null;
    private String file = null;
    private Button startBtn;
    private Button stopBtn;
    private Button playBtn;
    private Button stopPlayBtn;
    private TextView text;
    public TextView textView6;
    //  public String urlString = null;
    private static final int FILE_SELECT_CODE = 0;

    public Button lagre;
    public Button VelgLydklipp;
    private static final int SELECT_AUDIO = 2;
    String path = "";


    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;

    /**********  File Path *************/
    final String uploadFilePath = "/storage/emulated/0/";
    // "/mnt/sdcard/";

    final String uploadFileName = "lydfil.3gpp";

    //  private Context mContext;
    // private boolean isRecording = false;
    // boolean clicked = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        //Intent intent = new Intent(this, UploadFileToServer.class);

        //  String urlString = "https://home.hbv.no/110030/lyd/UploadToServer.php";

        setContentView(R.layout.activity_lyd_activity);

        //  myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        text = (TextView) findViewById(R.id.text1);
        // lagrer pÃ¥ minnekort
        outputFile = Environment.getExternalStorageDirectory().
                getAbsolutePath() + "/lydfil.3gpp";

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(outputFile);

        startBtn = (Button) findViewById(R.id.start);
        startBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startBtn.setVisibility(View.GONE);
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
            }
        });

        lagre = (Button) findViewById(R.id.Lagre);
        lagre.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog = ProgressDialog.show(Lyd.this, "", "Uploading file...", true);

                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                textView6.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);

                    }
                }).start();
            }
        });

        VelgLydklipp = (Button) findViewById(R.id.VelgLydklipp);
        VelgLydklipp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v){
                // TODO Auto-generated method stub
                openGalleryAudio();
            }
        });

        playBtn = (Button) findViewById(R.id.play);
        playBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                play(v);
            }
        });

        stopPlayBtn = (Button) findViewById(R.id.stopPlay);
        stopPlayBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopPlay(v);
            }
        });

        textView6.setText("Uploading file path :- '/mnt/sdcard/" + uploadFileName + "'");

        /************* Php script path ****************/
        upLoadServerUri = "https://home.hbv.no/110115/bac/UploadToServer.php";
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

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setOutputFile(outputFile);


        try {
            //  myRecorder.reset();
            myRecorder.prepare();
            myRecorder.start();
            text.setText("funker");
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }

        text.setText(": Tar opp lyd");
        //startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        Toast.makeText(getApplicationContext(), "Start recording...",
                Toast.LENGTH_SHORT).show();

        //if (outputFile.isEmpty()) {
        //  System.out.print("hei");
        //}
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

                //outputFile = Environment.getExternalStorageDirectory().
                //       getAbsolutePath() + "/lydfil.3gpp";

          /*      String filename = "lydfil.3gpp";
                File outputFile = new File(Environment.getExternalStorageDirectory(), filename);
                FileOutputStream fos;
                byte[] data = new String("data to write to file").getBytes();
                try {
                    fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    // handle exception
                } catch (IOException e) {
                    // handle exception
                }
                */




            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private void openGalleryAudio() {
    }

    public int uploadFile(String sourceFileUri) {

        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);


        if (!sourceFile.isFile())

        {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    textView6.setText("Source File not exist :"
                            + uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;

        } else

        {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + fileName +"\"" + lineEnd);


                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

              /*  URL url1 = new URL("https://home.hbv.no/lyd/UploadToServer.php");
                HttpURLConnection mUrlConnection = (HttpURLConnection) url1.openConnection();
                mUrlConnection.setDoInput(true);
                int i;
                char c;
                InputStream is = new BufferedInputStream(mUrlConnection.getInputStream());
               // String s = readStream(is);
                while((i=is.read())!=-1)
                {
                    // converts integer to character
                    c=(char)i;

                    // prints character
                    System.out.print(c);
                }
                */

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                    + " http://www.androidexample.com/media/uploads/"
                                    + uploadFileName;

                            textView6.setText(msg);
                            Toast.makeText(Lyd.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });
                    try {

                        HttpURLConnection urlConnection = (HttpURLConnection) url
                                .openConnection();
                        InputStream in = urlConnection.getInputStream();

                        InputStreamReader isw = new InputStreamReader(in);
                        int data = isw.read();
                        while (data != -1) {
                            char current = (char) data;
                            data = isw.read();
                            System.out.print(current);
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        textView6.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(Lyd.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        textView6.setText("Got Exception : see logcat ");
                        Toast.makeText(Lyd.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Up fil 2 serv Exception", "Exception : "
                        + e.getMessage(), e);
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

}
