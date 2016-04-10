package com.example.sarthak.customdialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {

    private EditText location;
    private EditText description;
    private int hours;
    private int minutes;
    private int days;
    private FButton hour_minute_selector;
    private FButton day_selector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Enter Report Parameters")
                .customView(R.layout.activity_main, wrapInScrollView)
                .positiveText("Add")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //add to database and dismiss dialog
                    }
                })
                .negativeText("Dismiss")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
        hour_minute_selector = (FButton) dialog.getCustomView().findViewById(R.id.hour_minute_selectorButton_ReportAdd);
        hour_minute_selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog durationPickerDialog = new DurationPickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hours = hourOfDay;
                        minutes = minute;
                    }
                }, 0, 0);
                durationPickerDialog.show();
            }
        });

    }
}
