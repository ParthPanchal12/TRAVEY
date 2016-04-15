package com.example.sarthak.navigationdrawer.Backend.Backend;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sarthak.navigationdrawer.R;


public class LoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            toolbar.setTitle("TRAVEY");
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        String[] tabTitles = new String[]{"Login", "Register"};
        LoginRegisterAdapter adapter = new LoginRegisterAdapter(getSupportFragmentManager(), tabTitles);
        viewPager.setAdapter(adapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.sliding_tabs);
        tabs.setupWithViewPager(viewPager);
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("Enter IP for the backend server")
                .customView(R.layout.layout_enter_ip, true)
                .positiveText("Okay")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText editText = (EditText) dialog.getCustomView().findViewById(R.id.et_ip);
                        String te = editText.getText().toString();
                        if (te == null) {
                            Toast.makeText(LoginRegister.this, "Ip can not be empty", Toast.LENGTH_SHORT).show();
                        } else if (te.trim().isEmpty() || te.trim().equals("")) {
                            Toast.makeText(LoginRegister.this, "Ip can not be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            Config.ip = te.trim();
                            dialog.dismiss();
                        }
                        enablePermissions();
                    }
                }).show();

    }

    int PERMISSION_CHECK_1 = 1;
    int PERMISSION_CHECK_2 = 2;
    int PERMISSION_CHECK_3 = 3;
    int PERMISSION_CHECK_4 = 4;
    int PERMISSION_CHECK_5 = 5;

    private void enablePermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegister.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(LoginRegister.this, "Error while requesting permissions", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ActivityCompat.requestPermissions(LoginRegister.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CHECK_1);
            }
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegister.this, Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(LoginRegister.this, "Error while requesting permissions", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ActivityCompat.requestPermissions(LoginRegister.this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_CHECK_2);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegister.this, Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(LoginRegister.this, "Error while requesting permissions", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ActivityCompat.requestPermissions(LoginRegister.this, new String[]{Manifest.permission.RECEIVE_SMS}, PERMISSION_CHECK_3);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegister.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(LoginRegister.this, "Error while requesting permissions", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ActivityCompat.requestPermissions(LoginRegister.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CHECK_4);
            }
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginRegister.this, Manifest.permission.CALL_PHONE)) {
                Toast.makeText(LoginRegister.this, "Error while requesting permissions", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                ActivityCompat.requestPermissions(LoginRegister.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CHECK_5);
            }
        }
    }
}
