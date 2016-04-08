package com.example.sarthak.navigationdrawer.GCM;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class GCM {
    private String regId = "";
    private Context context;
    private GoogleCloudMessaging gcm;
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private String TAG=GCM.class.getSimpleName();

    public GCM(Context context) {
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {

        gcm = GoogleCloudMessaging.getInstance(context);
        regId = getRegistrationId(context);

        if (regId.isEmpty()) {
            registerInBackground();
        }
        Log.d("reg",regId);
    }

    /*Returns already registered GCM registration token stored in shared preferences*/
    private String getRegistrationId(Context context) {
        SharedPreferences gcmPreferences = getPreferenceGCM(context);
        regId = gcmPreferences.getString(PROPERTY_REG_ID, "");
        if (regId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // If the app was updated then invalidate all the registration ids
        int registeredVersion = gcmPreferences.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Toast.makeText(context,regId,Toast.LENGTH_SHORT).show();
        return regId;
    }

    /*Returns a shared preference GCM instance*/
    private SharedPreferences getPreferenceGCM(Context context) {
        return context.getSharedPreferences(GCM.class.getSimpleName(), Context.MODE_PRIVATE);
    }

    /*Current App Version*/
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    /*Use instance id instead if register(deprecated)*/
                    regId = gcm.register(Config.project_id);
                    msg = "Device registered, registration ID=" + regId;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    //sendRegistrationIdToBackend();

                    storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("regid",msg);
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }
    private void storeRegistrationId(Context context,String id){
        final SharedPreferences prefs = getPreferenceGCM(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
}
