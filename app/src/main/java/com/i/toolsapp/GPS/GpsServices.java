package com.i.toolsapp.GPS;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.RequiresApi;

import com.i.toolsapp.R;

public class GpsServices extends Service implements LocationListener, GpsStatus.Listener {
    private LocationManager mLocationManager;

    Location lastlocation = new Location("last");
    Data data;

    double currentLon = 0;
    double currentLat = 0;
    double lastLon = 0;
    double lastLat = 0;

    PendingIntent contentIntent;

    @Override
    public void onCreate() {

        Intent notificationIntent = new Intent(this, GpsActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);

        updateNotification(false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                mLocationManager.addGpsStatusListener(this);
            }
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        data = GpsActivity.getData();
        if (data.isRunning()){
            currentLat = location.getLatitude();
            currentLon = location.getLongitude();

            if (data.isFirstTime()){
                lastLat = currentLat;
                lastLon = currentLon;
                data.setFirstTime(false);
            }

            lastlocation.setLatitude(lastLat);
            lastlocation.setLongitude(lastLon);
            double distance = lastlocation.distanceTo(location);

            if (location.getAccuracy() < distance){
                data.addDistance(distance);

                lastLat = currentLat;
                lastLon = currentLon;
            }

            if (location.hasSpeed()) {
                data.setCurSpeed(location.getSpeed() * 3.6);
                if(location.getSpeed() == 0){
                    new isStillStopped().execute();
                }
            }
            data.update();
            updateNotification(true);
        }
    }

    public void updateNotification(boolean asData){

        String channelID = "" ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           channelID =  createNotificationChannel(getString(R.string.app_name), "My Background Service");
        } else {

        }

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getBaseContext(),channelID)
                    .setContentTitle(getString(R.string.running))
                    .setContentIntent(contentIntent);
        }else{
            builder = new Notification.Builder(getBaseContext())
                    .setContentTitle(getString(R.string.running))
                    .setContentIntent(contentIntent);
        }

        if(asData){
            builder.setContentText("Max Speed : "+ data.getMaxSpeed()+", Distance : "+data.getDistance());
        }else{
            builder.setContentText("Max Speed : - , Distance : -");
        }
        Notification notification = builder.build();
        startForeground(R.string.noti_id, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }   
       
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if(mLocationManager!=null){
            mLocationManager.removeUpdates(this);
            mLocationManager.removeGpsStatusListener(this);
        }

        stopForeground(true);
    }

    @Override
    public void onGpsStatusChanged(int event) {}

    @Override
    public void onProviderDisabled(String provider) {}
   
    @Override
    public void onProviderEnabled(String provider) {}
   
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    class isStillStopped extends AsyncTask<Void, Integer, String> {
        int timer = 0;
        @Override
        protected String doInBackground(Void... unused) {
            try {
                while (data.getCurSpeed() == 0) {
                    Thread.sleep(1000);
                    timer++;
                }
            } catch (InterruptedException t) {
                return ("The sleep operation failed");
            }
            return ("return object when task is finished");
        }

        @Override
        protected void onPostExecute(String message) {
            data.setTimeStopped(timer);
        }
    }
}
