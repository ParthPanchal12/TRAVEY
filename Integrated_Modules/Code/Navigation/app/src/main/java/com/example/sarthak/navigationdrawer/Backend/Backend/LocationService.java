package com.example.sarthak.navigationdrawer.Backend.Backend;

/**
 * Created by parth panchal on 09-04-2016.
 */

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationService extends Service {
    private static final String TAG = "LOCATIONREFRESH";
    private static final int LOCATION_INTERVAL = 10;//decrease this to experiment
    private static final float LOCATION_DISTANCE = 10000f;//increase this
    public UpdateMyResults updater;
    public boolean stopThread = false;
    Location mLastLocation;
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    private LocationManager mLocationManager = null;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public synchronized int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        Log.d(getPackageName(), "Starting...");
        if (!updater.isAlive()) {
            updater = new UpdateMyResults();
            Log.d(getPackageName(), "New updater thread...");
            updater.start();        //Try commenting it once and check after moving your position..
        }
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        System.out.println("Hiiiithere");
        initializeLocationManager();
        updater = new UpdateMyResults();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    @Override
    public synchronized void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopThread = true;
        Log.d(getPackageName(), "Destroying MyResultsService");
        try {
            updater.join();
            Log.d(getPackageName(), "Closed updater thread");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {


        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);

            mLastLocation.set(location);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("AppPref", Context.MODE_PRIVATE);
            String phone_number = pref.getString(Config.phone_number, "");
            ServerRequest sr = new ServerRequest(getApplicationContext());
            ArrayList<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair(Config.phone_number, phone_number));
            params.add(new BasicNameValuePair(Config.latitude, "" + mLastLocation.getLatitude()));
            params.add(new BasicNameValuePair(Config.longitude, "" + mLastLocation.getLongitude()));
            JSONObject json = sr.getJSON(Config.ip + "/locationRefresh", params);//change the request url accordingly
            Log.d("here", "json received");
            System.out.println("Hiiiithere11");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    private class UpdateMyResults extends Thread {

        static final long DELAY = 30000;//(30 sec) Change the milliseconds accordingly

        @Override
        public void run() {
            while (!stopThread) {
                try {
                    //Do stuff and pause
                    Log.d(getName(), "Running" + mLastLocation);

                    SharedPreferences pref = getApplicationContext().getSharedPreferences("AppPref", Context.MODE_PRIVATE);
                    String phone_number = pref.getString(Config.phone_number, "");
                    ServerRequest sr = new ServerRequest(getApplicationContext());
                    ArrayList<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair(Config.phone_number, phone_number));
                    params.add(new BasicNameValuePair(Config.latitude, "" + mLastLocation.getLatitude()));
                    params.add(new BasicNameValuePair(Config.longitude, "" + mLastLocation.getLongitude()));
                    JSONObject json = sr.getJSON(Config.ip + "/locationRefresh", params);//change the request url accordingly
                    Log.d("here", "json received");
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    // Interrupt
                    e.printStackTrace();
                }
            }//end while
        }//end run
    }//end UpdateMyResults
}
