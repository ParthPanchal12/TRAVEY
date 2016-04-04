package com.example.sarthak.floatingactionbuttons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class PlaceAutocompleteActivity extends AppCompatActivity{
    private PlaceAutocompleteFragment placeAutocompleteFragment_source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_autocomplete);
        placeAutocompleteFragment_source=(PlaceAutocompleteFragment)getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_source_report_panel);
        placeAutocompleteFragment_source.setHint("Enter the place");
        placeAutocompleteFragment_source.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if(place!=null){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Name",place.getName());
                    resultIntent.putExtra("Latitude",place.getLatLng().latitude);
                    resultIntent.putExtra("Longitude",place.getLatLng().longitude);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }

            @Override
            public void onError(Status status) {
                Log.e("PlaceAutoCompleteError",status.getStatusMessage());
            }
        });
    }
}