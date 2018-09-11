package org.ml.ondevice.classifier;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class SuperMainActivity extends Activity {

    Button Btn1;
    Button Btn2;

    // private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LoggingService1", "**********************");
        setContentView(R.layout.super_activity_main);
        Log.e("LoggingService2", "***********************");

       /*
        Btn1 = (Button) findViewById(R.id.sensor);
        Btn2 = (Button) findViewById(R.id.sensor);


        createOutput();
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Intent intent = new Intent(SuperMainActivity.this, MainActivity.class);
                //startActivity(intent);
                // File fdir = new File(mGlobal.media_path);
            }
        });
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(SuperMainActivity.this, MainActivity.class);
                // startActivity(intent);

            }
        });

        */


    }

    private void createOutput() {
        File fdir = new File(mGlobal.media_path);
        if (!fdir.isDirectory()) {
            if (!fdir.mkdirs()) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }



    public native String stringFromJNI();



}
