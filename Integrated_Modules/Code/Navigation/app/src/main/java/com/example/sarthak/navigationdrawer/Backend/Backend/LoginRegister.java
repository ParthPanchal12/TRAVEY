package com.example.sarthak.navigationdrawer.Backend.Backend;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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

                    }
                }).show();
    }

}
