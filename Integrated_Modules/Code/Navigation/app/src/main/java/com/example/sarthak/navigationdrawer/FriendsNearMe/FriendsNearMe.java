package com.example.sarthak.navigationdrawer.FriendsNearMe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.ContactDisplay.Friends;
import com.example.sarthak.navigationdrawer.LeaderBoard.User;
import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sarthak on 9/4/16.
 */
public class FriendsNearMe extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    private Location myLastKnownLocation;
    private int success = 0;
    private ArrayList<User> friendsNearMe;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    ArrayList<Friends> actualFriends;
    private SweetAlertDialog progressBar;
    private GoogleApiClient mGoogleApiClient;

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

        pref = this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        edit = pref.edit();

         /*Progress Bar*/
        progressBar = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        progressBar.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressBar.setTitleText("Getting friends near you..");
        progressBar.setCancelable(false);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


       /*location manager*/

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
            Toast.makeText(FriendsNearMe.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER


        friendsNearMe = new ArrayList<>();


    }

    private void getFriendsNearMe() {
        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Config.latitude, "" + 23.187699));
        params.add(new BasicNameValuePair(Config.longitude, "" + 72.6275614));
        ServerRequest sr = new ServerRequest(FriendsNearMe.this);
        Log.d("FriendsNearMe", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONArray json = sr.getJSONArray(Config.ip + "/friendsNearBy", params);
        Log.d("FriendsNearMe", "json received" + json);

        if (json != null) {

            Log.d("JsonAllFriends", "" + json);
            for (int i = 0; i < json.length(); i++) {
                Gson gson = new Gson();
                try {
                    User user = gson.fromJson(json.getString(i), new TypeToken<User>() {
                    }.getType());
                    user.getCoordinates();
                    Log.d("UserLat", "" + user.getLatitude() + user.getName());
                    friendsNearMe.add(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            success = 1;
            addMarkerForFriends();
            progressBar.hide();
        }
    }

    private void addMarkerForFriends() {

        for (int i = 0; i < friendsNearMe.size(); i++) {
            Log.d("MarkerPosition", "" + friendsNearMe.get(i).getLatitude() + "," + friendsNearMe.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(friendsNearMe.get(i).getLatitude(), friendsNearMe.get(i).getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        }
        animateCameraToShowNearMeArea();
    }

    private void animateCameraToShowNearMeArea() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
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
        myLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("LatMy",""+myLastKnownLocation.getLatitude());
        builder.include(new LatLng(myLastKnownLocation.getLatitude(),myLastKnownLocation.getLongitude()));
        builder.include(new LatLng(friendsNearMe.get(0).getLatitude(),friendsNearMe.get(0).getLongitude()));
        LatLngBounds bounds = builder.build();
        int padding = 310; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        progressBar.show();
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
            Toast.makeText(FriendsNearMe.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        getFriendsNearMe();
    }

    @Override
    public void onLocationChanged(Location location) {
        myLastKnownLocation = location;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
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
            Toast.makeText(FriendsNearMe.this, "Enable Permissions", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(FriendsNearMe.this, "Enable Provider", Toast.LENGTH_SHORT).show();
        createGPSEnableDialog();
    }

    private void createGPSEnableDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FriendsNearMe.this);
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
                            Toast.makeText(FriendsNearMe.this, "GPS Enabled", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FriendsNearMe.this, "Please Enable GPS", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressBar.dismiss();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
