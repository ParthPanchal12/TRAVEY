package com.example.sarthak.navigationdrawer.ContactDisplay;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sarthak on 8/4/16.
 */
public class DisplayFriendsOnMap extends AppCompatActivity implements OnMapReadyCallback, LocationListener {


    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private GoogleMap mMap;
    private LatLng friendPosition;
    private LatLng myPosition;
    private Location myLastKnownLocation;
    private LocationManager locationManager;
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_display_friends_on_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_friends_on_Map);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("Friend Tracking");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_friends);
        mapFragment.getMapAsync(this);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_friends)).getMap();



       /**/

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //enablePermissions();
            Toast.makeText(DisplayFriendsOnMap.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER

        if (getLocationOfFriend()) {
            displayFriend();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //enablePermissions();
            Toast.makeText(DisplayFriendsOnMap.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        myPosition=new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude());
        //disabled my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void displayFriend() {
        try {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(myPosition);
            builder.include(friendPosition);
            LatLngBounds bounds = builder.build();
            int padding = 310; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        } catch (Exception e) {
            Toast.makeText(DisplayFriendsOnMap.this, "An error occured", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean getLocationOfFriend() {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Config.phone_number, "8758964908"));
        ServerRequest sr = new ServerRequest();
        Log.d("here", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONArray json = sr.getJSONArray(Config.ip + "/getLocation", params);
        Log.d("here", "json received1" + json);
        if (json != null) {
            try {
                Log.d("JsonFriend", "" + json);
                latitude = json.getDouble(0);
                longitude = json.getDouble(1);
                friendPosition = new LatLng(latitude, longitude);
                Log.d("LatFriendLongFriend", "" + latitude + "," + longitude+":"+myPosition.latitude+","+myPosition.longitude);

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        } else return false;

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean canGetMyLocation() {
        return (myLastKnownLocation != null);
    }

    @Override
    public void onLocationChanged(Location location) {
        myLastKnownLocation = location;
        myPosition= new LatLng(location.getLatitude(), location.getLongitude());


        //locationManager.removeUpdates(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //enablePermissions();
            Toast.makeText(DisplayFriendsOnMap.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(DisplayFriendsOnMap.this, "Enable Provider", Toast.LENGTH_SHORT).show();
        createGPSEnableDialog();
    }

    private void createGPSEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayFriendsOnMap.this);
        builder.setMessage("Please Enable the GPS")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent gpsOptionsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                        if (checkGPSEnabled() == true) {
                            Toast.makeText(DisplayFriendsOnMap.this, "GPS Enabled", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DisplayFriendsOnMap.this, "Please Enable GPS", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.create().show();
    }

    private boolean checkGPSEnabled() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }
}
