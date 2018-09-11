package org.ml.ondevice.classifier;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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


        Btn1 = (Button) findViewById(R.id.iris);
        Btn2 = (Button) findViewById(R.id.sensor);



        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(SuperMainActivity.this, MainActivity.class);
               startActivity(intent);

            }
        });


        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuperMainActivity.this, RuntimeshowActivity.class);
                startActivity(intent);

            }
        });




    }


    private void requestPermission(String permission) {
        //check API version, do nothing if API version < 23!
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Activity", "Granted!");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d("Activity", "Denied!");
                    // finish();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void requestPermissions() {
        requestPermission(Manifest.permission.RECORD_AUDIO);
        requestPermission(Manifest.permission.READ_PHONE_STATE);
        requestPermission(Manifest.permission.WAKE_LOCK);
        requestPermission(Manifest.permission.VIBRATE);
        requestPermission(Manifest.permission.KILL_BACKGROUND_PROCESSES);
        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        requestPermission(Manifest.permission.ACCESS_WIFI_STATE);
        requestPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        requestPermission(Manifest.permission.INTERNET);
        requestPermission(Manifest.permission.RESTART_PACKAGES);
        requestPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        requestPermission(Manifest.permission.CHANGE_CONFIGURATION);
    }














    public native String stringFromJNI();



}
