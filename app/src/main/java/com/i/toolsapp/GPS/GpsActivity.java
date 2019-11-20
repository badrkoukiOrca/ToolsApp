package com.i.toolsapp.GPS;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.capur16.digitspeedviewlib.DigitSpeedView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.i.toolsapp.R;
import com.jignesh13.speedometer.SpeedoMeterView;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GpsActivity extends AppCompatActivity implements LocationListener, GpsStatus.Listener, OnMapReadyCallback {

    private SharedPreferences sharedPreferences;
    private LocationManager mLocationManager;
    private static Data data;
    private GoogleMap mMap;
    SpeedoMeterView speedoMeterView;
    Chronometer chronometer;
    DigitSpeedView speedometer;
    Location userlocation ;
    Button stats ;


    Double initial_lat;
    Double initial_long ;

    private FloatingActionButton fab;
    private Data.OnGpsServiceUpdate onGpsServiceUpdate;
    private boolean firstfix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_activity);

        initial_lat = 36.8355025 ;
        initial_long = 10.2477709 ;


        data = new Data(onGpsServiceUpdate);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        fab = findViewById(R.id.fab);


        stats = findViewById(R.id.stats);
        stats.setEnabled(false);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpsActivity.this,StatsActivity.class));
                finish();
            }
        });



        speedometer = findViewById(R.id.speed_view);
        speedoMeterView = findViewById(R.id.speedometerview);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        onGpsServiceUpdate = new Data.OnGpsServiceUpdate() {
            @Override
            public void update() {
                double maxSpeedTemp = data.getMaxSpeed();
                double distanceTemp = data.getDistance();
                double averageTemp;
                if (sharedPreferences.getBoolean("auto_average", false)) {
                    averageTemp = data.getAverageSpeedMotion();
                } else {
                    averageTemp = data.getAverageSpeed();
                }

                String speedUnits;
                String distanceUnits;
                if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                    maxSpeedTemp *= 0.62137119;
                    distanceTemp = distanceTemp / 1000.0 * 0.62137119;
                    averageTemp *= 0.62137119;
                    speedUnits = "mi/h";
                    distanceUnits = "mi";
                } else {
                    speedUnits = "km/h";
                    if (distanceTemp <= 1000.0) {
                        distanceUnits = "m";
                    } else {
                        distanceTemp /= 1000.0;
                        distanceUnits = "km";
                    }
                }

                SpannableString s = new SpannableString(String.format("%.0f %s", maxSpeedTemp, speedUnits));
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - speedUnits.length() - 1, s.length(), 0);

                SpeedHistory.MaxSpeed =  data.getMaxSpeed();

                s = new SpannableString(String.format("%.0f %s", averageTemp, speedUnits));
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - speedUnits.length() - 1, s.length(), 0);

                SpeedHistory.AverageSpeed = averageTemp ;



                s = new SpannableString(String.format("%.3f %s", distanceTemp, distanceUnits));
                s.setSpan(new RelativeSizeSpan(0.5f), s.length() - distanceUnits.length() - 1, s.length(), 0);

                SpeedHistory.TotalDistance = distanceTemp;
            }
        };

        mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        Criteria criteria = new Criteria();
        criteria.setAccuracy( Criteria.ACCURACY_FINE );

        final String mocLocationProvider = LocationManager.GPS_PROVIDER;


        mLocationManager.addTestProvider(mocLocationProvider, false, false,
                false, false, true, true, true, 0, 5);
        mLocationManager.setTestProviderEnabled(mocLocationProvider, true);


        //************ TODO SIMULATION CODE SNIPPET
        //set first location -- (For simulate GPS)
        Location loc = new Location(mocLocationProvider);
        Location mockLocation = new Location(mocLocationProvider); // a string
        mockLocation.setLatitude(initial_lat);
        mockLocation.setLongitude(initial_long);
        mockLocation.setAltitude(loc.getAltitude());
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setAccuracy(1);
        mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        mLocationManager.setTestProviderLocation( mocLocationProvider, mockLocation);
        SimulateGPSMouvement(mocLocationProvider); //simulate GPS if you are moving comment this code
        //************ TODO SIMULATION CODE SNIPPET


        chronometer = (Chronometer) findViewById(R.id.chrono);
        chronometer.setText("00:00:00");
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            boolean isPair = true;

            @Override
            public void onChronometerTick(Chronometer chrono) {
                long time;
                if (data.isRunning()) {
                    time = SystemClock.elapsedRealtime() - chrono.getBase();
                    data.setTime(time);
                } else {
                    time = data.getTime();
                }

                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                chrono.setText(hh + ":" + mm + ":" + ss);

                if (data.isRunning()) {
                    chrono.setText(hh + ":" + mm + ":" + ss);
                } else {
                    if (isPair) {
                        isPair = false;
                        chrono.setText(hh + ":" + mm + ":" + ss);
                    } else {
                        isPair = true;
                        chrono.setText("");
                    }
                }

            }
        });
    }

    public void onFabClick(View v) {
        if (!data.isRunning()) {
            SpeedHistory.speedHistory = new ArrayList<>();
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_pause));
            data.setRunning(true);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            data.setFirstTime(true);
            startService(new Intent(getBaseContext(), GpsServices.class));
            stats.setEnabled(true);
        } else {
            fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
            data.setRunning(false);
            stopService(new Intent(getBaseContext(), GpsServices.class));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        firstfix = true;
        if (!data.isRunning()) {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("data", "");
            data = gson.fromJson(json, Data.class);
        }
        if (data == null) {
            data = new Data(onGpsServiceUpdate);
        } else {
            data.setOnGpsServiceUpdate(onGpsServiceUpdate);
        }

        if (mLocationManager.getAllProviders().indexOf(LocationManager.GPS_PROVIDER) >= 0) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
            }

        } else {
            Log.w("GpsActivity", "No GPS location provider found. GPS data display will not be available.");
        }
        mLocationManager.addGpsStatusListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        mLocationManager.removeGpsStatusListener(this);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        prefsEditor.putString("data", json);
        prefsEditor.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(getBaseContext(), GpsServices.class));
    }


    @Override
    public void onLocationChanged(Location location) {

        userlocation = location ;
        UpdateMap();

        SpeedHistory.speedHistory = new ArrayList<>();

        if (location.hasAccuracy()) {
            double acc = location.getAccuracy();
            String units;
            if (sharedPreferences.getBoolean("miles_per_hour", false)) {
                units = "ft";
                acc *= 3.28084;
            } else {
                units = "m";
            }
            SpannableString s = new SpannableString(String.format("%.0f %s", acc, units));
            s.setSpan(new RelativeSizeSpan(0.75f), s.length() - units.length() - 1, s.length(), 0);

            if (firstfix) {
                firstfix = false;
            }
        } else {
            firstfix = true;
        }

        if (location.hasSpeed()) {

            //double speed = location.getSpeed() * 3.6;
            double speed = location.getSpeed() ;
            int speeds = (int) speed;

            speedoMeterView.setSpeed(speeds,false);
            speedometer.updateSpeed(speeds);

            if(data!=null && data.isRunning()){
                SpeedHistory.speedHistory.add(speed);
            }

        }

    }

    @Override
    public void onGpsStatusChanged(int event) {
        switch (event) {
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                GpsStatus gpsStatus = null ;
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }else{
                    gpsStatus = mLocationManager.getGpsStatus(null);
                }
                if(gpsStatus!=null){
                    int satsUsed = 0;
                    Iterable<GpsSatellite> sats = gpsStatus.getSatellites();
                    for (GpsSatellite sat : sats) {
                        if (sat.usedInFix()) {
                            satsUsed++;
                        }
                    }
                    if (satsUsed == 0) {
                        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_play));
                        data.setRunning(false);
                        stopService(new Intent(getBaseContext(), GpsServices.class));
                        firstfix = true;
                    }
                }

                break;
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
        }
    }



    public static Data getData() {
        return data;
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(0, 0);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Boat"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void  UpdateMap(){
        mMap.clear();
        LatLng sydney = new LatLng(userlocation.getLatitude(),userlocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Boat"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    public void SimulateGPSMouvement(final String mocLocationProvider){
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {

                Location previous_location = new Location("");
                previous_location.setLatitude(initial_lat);
                previous_location.setLongitude(initial_long);

                initial_lat += 0.00001; //steps mouvement
                initial_long += 0.00001; //steps mouvement

                Location new_location = new Location("");
                new_location.setLatitude(initial_lat);
                new_location.setLongitude(initial_long);


                float distanceInMeters = previous_location.distanceTo(new_location); //distance between the previous location and the new one
                // in this case distanceInMeters = 1.48 meter


                Location loc = new Location(mocLocationProvider);
                Location mockLocation = new Location(mocLocationProvider); // a string
                mockLocation.setLatitude(initial_lat);
                mockLocation.setLongitude(initial_long);
                mockLocation.setAltitude(loc.getAltitude());
                mockLocation.setTime(System.currentTimeMillis());
                mockLocation.setAccuracy(1);
                Random random = new Random();
                mockLocation.setSpeed(random.nextInt(56-50)+50); //speed is should be reasonable with distance
                mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
                mLocationManager.setTestProviderLocation( mocLocationProvider, mockLocation); //invoke location change
            }
        }, 0, 100);
    }

}
