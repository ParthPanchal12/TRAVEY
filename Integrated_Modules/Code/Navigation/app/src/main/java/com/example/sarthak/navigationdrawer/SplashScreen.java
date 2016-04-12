package com.example.sarthak.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.LoginRegister;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.GCM.GCM;

/**
 * Created by parth panchal on 11-04-2016.
 */
public class SplashScreen extends AppCompatActivity {

    private Boolean isInternetPresent;
    private String token;
    private GCM gcm;
    private String regId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Hiiiithere11");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gcm = new GCM(SplashScreen.this);
        regId = gcm.getRegId();
        ServerRequest sr = new ServerRequest(SplashScreen.this);
        isInternetPresent = sr.isConnectingToInternet(); // true or false

        SharedPreferences pref = SplashScreen.this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        token = pref.getString("token", "");
        if (isInternetPresent) {

            if (regId == null || regId.isEmpty()) {
                Toast.makeText(SplashScreen.this, "Could not create Registration id for GCM", Toast.LENGTH_SHORT).show();
            }

            if (token != null && token != "") {
                Log.d("TestTest", "Test");
                startActivity(new Intent(SplashScreen.this, MapsActivity.class));
                finish();
            } else {
                Intent intent = new Intent(SplashScreen.this, LoginRegister.class);
                startActivity(intent);
                finish();
            }


            Log.d("here", "json received");


        } else {
            Toast.makeText(SplashScreen.this, "No internet Connection!", Toast.LENGTH_SHORT).show();
        }


    }

}

