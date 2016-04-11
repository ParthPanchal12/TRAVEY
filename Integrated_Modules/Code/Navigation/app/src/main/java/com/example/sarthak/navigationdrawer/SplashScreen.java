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

import org.apache.http.NameValuePair;

import java.util.ArrayList;

/**
 * Created by parth panchal on 11-04-2016.
 */
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ArrayList<NameValuePair> params = new ArrayList<>();

        ServerRequest sr = new ServerRequest(this);
        Boolean isInternetPresent = sr.isConnectingToInternet(); // true or false
        if (isInternetPresent) {
            GCM gcm = new GCM(this);

            String regId = gcm.getRegId();
            if (regId == null && regId.isEmpty()) {
                Toast.makeText(this, "Could not create Registration id for GCM", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences pref = this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = pref.edit();
                //Storing Data using SharedPreferences
                edit.putString("gcm_id", regId);
                edit.commit();
            }


            Log.d("here", "json received");
            Intent intent = new Intent(SplashScreen.this, LoginRegister.class);
            startActivity(intent);

        } else {
            Toast.makeText(SplashScreen.this, "No internet Connection!", Toast.LENGTH_SHORT).show();
        }
        System.out.println("Hiiiithere11");

    }
}
