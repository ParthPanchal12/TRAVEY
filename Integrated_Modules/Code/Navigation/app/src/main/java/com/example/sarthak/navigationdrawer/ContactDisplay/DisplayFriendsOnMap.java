package com.example.sarthak.navigationdrawer.ContactDisplay;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by sarthak on 8/4/16.
 */
public class DisplayFriendsOnMap extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng friendPosition;
    private LatLng myPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        setContentView(R.layout.activity_main_display_friends_on_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_friends);
        mapFragment.getMapAsync(this);
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if(getMyLocation()){

        }else{
            Toast.makeText(DisplayFriendsOnMap.this, "Unable to get your location", Toast.LENGTH_SHORT).show();
        }
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
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private boolean getMyLocation(){
        return true;
    }
}
