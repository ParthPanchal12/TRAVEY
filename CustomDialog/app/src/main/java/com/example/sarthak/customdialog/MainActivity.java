package com.example.sarthak.customdialog;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

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
    String ar[]={"Share your location with friend","Get his location"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp);
        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(this)
                .title("Select an option")
                .items(ar)
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        Toast.makeText(MainActivity.this, ""+ar[which], Toast.LENGTH_SHORT).show();
                        return true;
                    }
                })
                .positiveText("Choose")
                .show();

    }
}
