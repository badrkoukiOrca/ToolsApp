package com.i.toolsapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.i.toolsapp.Agenda.MainActivity;
import com.i.toolsapp.GPS.GpsActivity;
import com.i.toolsapp.Streaming.StreamingConnection;


public class LauncherActivity extends AppCompatActivity implements View.OnClickListener{


    Button StreamingBtn;
    Button GpsBtn ;
    Button AgendaBtn ;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StreamingBtn = findViewById(R.id.StreamingBtn);
        GpsBtn = findViewById(R.id.GpsBtn);
        AgendaBtn = findViewById(R.id.AgendaBtn);


        GpsBtn.setOnClickListener(this);
        StreamingBtn.setOnClickListener(this);
        AgendaBtn.setOnClickListener(this);

        GpsBtn.setEnabled(false);

        StreamingBtn.setEnabled(false);

        if(!hasPermissions(LauncherActivity.this, PERMISSIONS)){
            ActivityCompat.requestPermissions(LauncherActivity.this, PERMISSIONS, PERMISSION_ALL);
        }else{
            GpsBtn.setEnabled(true);
            StreamingBtn.setEnabled(true);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.StreamingBtn:
                startActivity(new Intent(LauncherActivity.this, StreamingConnection.class));
                break;
            case R.id.AgendaBtn:
                startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                break;
            case R.id.GpsBtn:
                startActivity(new Intent(LauncherActivity.this, GpsActivity.class));
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(hasPermissions(this, PERMISSIONS)){
                GpsBtn.setEnabled(true);
                StreamingBtn.setEnabled(true);
            }else{
                Toast.makeText(LauncherActivity.this,"You need to grant permission to use the application",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
