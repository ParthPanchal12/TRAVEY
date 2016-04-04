package com.example.sarthak.navigationdrawer.ReportPanel;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.R;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Calendar;

public class EnterReportParameters extends Dialog {

    private String title;
    private String location;
    private int hours = 2;
    private String selectedPlaceName;
    private double selectedPlaceLatitude;
    private double selectedPlaceLongitude;
    private int minutes = 0;
    private String description = "";
    private TextView textView_Location;
    private EditText editText_description;
    private EditText editText_hour;
    private EditText editText_minute;
    private PlaceAutocompleteFragment autocompleteFragmentSource;
    private Context ctx;
    private ArrayList<NameValuePair> parametersDatabase;
    public EnterReportParameters(Context context) {
        super(context);
        this.ctx=context;
    }

    public EnterReportParameters(Context context, String title,String selectedPlaceName,double selectedPlaceLatitude,double selectedPlaceLongitude) {
        super(context);
        this.ctx=context;
        this.title = title;
        this.selectedPlaceName=selectedPlaceName;
        this.selectedPlaceLatitude=selectedPlaceLatitude;
        this.selectedPlaceLongitude=selectedPlaceLongitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter_report_parameters);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dialog);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        /*On back pressed on toolbar*/
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        editText_description = (EditText) findViewById(R.id.editText_description_report);
        textView_Location = (TextView) findViewById(R.id.textView_location_report);
        editText_hour = (EditText) findViewById(R.id.hours_picked);
        editText_minute = (EditText) findViewById(R.id.minutes_picked);

        textView_Location.setText(selectedPlaceName);
        textView_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Select the place again", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });



        ImageView currentLocation = (ImageView) findViewById(R.id.imageView_my_current_location);


        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clicked to find your current location", Toast.LENGTH_SHORT).show();
                location = getCurrentLocation();
            }
        });

        FloatingActionButton fab_save_details = (FloatingActionButton) findViewById(R.id.fab_save_details);
        fab_save_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Get all the details filled by the user in the dialog*/
                getFilledDetails();
                if (location.isEmpty()) {
                    Toast.makeText(getContext(), "Location is necessary", Toast.LENGTH_SHORT).show();
                } else {
                    saveDetailsToDataBase();
                    dismiss();
                }
            }
        });

    }

    private void saveDetailsToDataBase() {
        parametersDatabase=new ArrayList<NameValuePair>();

        /*To do*/
        parametersDatabase.add(new BasicNameValuePair("phone_number", "lalal"));

        /*Time format*/
        /*July 22, 2013 14:00:00*/

        Calendar calendar=Calendar.getInstance();
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hours=calendar.get(Calendar.HOUR_OF_DAY);
        int minutes=calendar.get(Calendar.MINUTE);
        String monthString=getMonthFormatted(month);
        int seconds=0;
        String finalStartDate=getFormattedDate(monthString,day,year,hours,minutes,seconds);
        parametersDatabase.add(new BasicNameValuePair("detail", description));
        parametersDatabase.add(new BasicNameValuePair("tag",title));
        parametersDatabase.add(new BasicNameValuePair("start_time", finalStartDate));
        parametersDatabase.add(new BasicNameValuePair("end_time", "lalal"));
        parametersDatabase.add(new BasicNameValuePair("lat", String.valueOf(selectedPlaceLatitude)));
        parametersDatabase.add(new BasicNameValuePair("long", String.valueOf(selectedPlaceLongitude)));

    }

    private String getFormattedDate(String monthString, int day, int year, int hours, int minutes, int seconds) {
        String formattedDate= monthString+" "+day+", "+year+" "+hours+":"+minutes+":"+seconds;
        Log.d("FormattedDate",formattedDate);
        return formattedDate;
    }

    private String getMonthFormatted(int month){
        String monthString[]={
                "January","February","March","April","May","June","July","August","September","October","November","December"
        };
        return monthString[month];
    }

    private String getCurrentLocation() {
        return "";
    }

    private void getFilledDetails() {
        /*Initialise all the editTexts*/
        /*Get the data from the editTexts*/
        description = editText_description.getText().toString();
        location = textView_Location.getText().toString();

        /*Number saves the string value that is entered by the user*/
        String number = editText_hour.getText().toString().trim();
        if (!number.isEmpty())
            hours = Integer.parseInt(number);
        number = editText_minute.getText().toString().trim();
        if (!number.isEmpty())
            minutes = Integer.parseInt(number);
    }

}
