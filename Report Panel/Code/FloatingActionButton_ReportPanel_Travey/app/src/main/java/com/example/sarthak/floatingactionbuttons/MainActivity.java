package com.example.sarthak.floatingactionbuttons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

public class MainActivity extends AppCompatActivity {

    private PlaceAutocompleteFragment placeAutocompleteFragment_Source;
    private String typeOfReport;
    private PlaceAutocompleteFragment autocompleteFragmentSource;
    private final int PLACE_PICKER_REQUEST_CODE_INTENT = 1;
    private String selectedPlaceName;
    private double selectedPlaceLatitude;
    private double selectedPlaceLongitude;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionsMenu fab_menu_report_panel = (FloatingActionsMenu) findViewById(R.id.fab_report_panel);

        /*PlaceAutoCompleteFragment Initialisation*/
//        autocompleteFragmentSource = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_destination);
//        autocompleteFragmentSource.setHint("Select Source");

        FloatingActionButton fab_accident = (FloatingActionButton) findViewById(R.id.fab_report_accident);
        fab_accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                typeOfReport = "Accident";
                callPlaceAutoCompleteFragment();
            }
        });
        FloatingActionButton fab_roadblock = (FloatingActionButton) findViewById(R.id.fab_report_roadblock);
        fab_roadblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                typeOfReport = "Roadblock";
                callPlaceAutoCompleteFragment();
            }
        });
        FloatingActionButton fab_event = (FloatingActionButton) findViewById(R.id.fab_report_event);
        fab_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                typeOfReport = "Event";
                callPlaceAutoCompleteFragment();
            }
        });
        FloatingActionButton fab_other = (FloatingActionButton) findViewById(R.id.fab_report_other);
        fab_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                typeOfReport = "Other";
                callPlaceAutoCompleteFragment();
            }
        });

        FloatingActionButton fab_traffic = (FloatingActionButton) findViewById(R.id.fab_report_traffic);
        fab_traffic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                typeOfReport = "Traffic";
                callPlaceAutoCompleteFragment();
            }
        });
    }

    private void callPlaceAutoCompleteFragment() {
        Intent intent = new Intent(MainActivity.this, PlaceAutocompleteActivity.class);
        startActivityForResult(intent, PLACE_PICKER_REQUEST_CODE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLACE_PICKER_REQUEST_CODE_INTENT:
                if (data != null) {
                    selectedPlaceName = data.getStringExtra("Name");
                    selectedPlaceLatitude = data.getDoubleExtra("Latitude", 0);
                    selectedPlaceLongitude = data.getDoubleExtra("Longitude", 0);
                    createDialog();
                    return;
                }
                return;
        }
    }
    private void createDialog(){
        new EnterReportParameters(MainActivity.this,typeOfReport,selectedPlaceName,selectedPlaceLatitude,selectedPlaceLongitude).show();
    }
}
