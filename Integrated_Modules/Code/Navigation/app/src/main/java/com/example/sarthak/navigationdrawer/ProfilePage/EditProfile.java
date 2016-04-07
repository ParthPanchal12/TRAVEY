package com.example.sarthak.navigationdrawer.ProfilePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;
    private ImageView profileImage;
    private String title_toolbar;
    private String userInfo;
    private EditText editTextDescription;
    private String phone_number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        if(intent==null){
            Toast.makeText(EditProfile.this,"Error:No information recieved",Toast.LENGTH_SHORT).show();
            finish();
        }
        phone_number="";
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title_toolbar=intent.getExtras().getString("Title");
        userInfo=intent.getExtras().getString("Description");
        setTitle(title_toolbar);
        editTextDescription=(EditText)findViewById(R.id.editText_UserInfoEdit);
        editTextDescription.setText(userInfo);
        //to add button to confirm save
        getDetails();
    }

    private void getDetails(){
        userInfo=editTextDescription.getText().toString();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveDetailsToDatabases();
    }
    private void saveDetailsToDatabases(){
        ArrayList<NameValuePair> parameters=new ArrayList<>();
        String key=getKey();
        parameters.add(new BasicNameValuePair(Config.phone_number,phone_number));
        parameters.add(new BasicNameValuePair(key,userInfo));
    }
    private String getKey(){
        if(title_toolbar.equals("Name")){
            return "user_name";
        }else if(title_toolbar.equals("Phone")){
            return "phone_number";
        }else if(title_toolbar.equals("Email")){
            return "email";
        }
//        else if(title_toolbar.equals("Location")){
//            return "location_shared";
//        }
        return "";
    }
}
