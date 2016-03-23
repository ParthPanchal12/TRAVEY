package com.example.sarthak.profilepage_travey;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;
    private ImageView profileImage;
    private String title_toolbar;
    private String userInfo;
    private EditText editTextDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        if(intent==null){
            Toast.makeText(EditProfile.this,"Error:No information recieved",Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title_toolbar=intent.getExtras().getString("Title");
        userInfo=intent.getExtras().getString("Description");
        setTitle(title_toolbar);
        editTextDescription=(EditText)findViewById(R.id.editText_UserInfoEdit);
        editTextDescription.setText(userInfo);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
