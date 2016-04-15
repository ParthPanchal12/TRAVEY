package com.example.sarthak.navigationdrawer.ProfilePage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.Backend.Backend.VerificationActivity;
import com.example.sarthak.navigationdrawer.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditProfile extends AppCompatActivity {
    private Toolbar toolbar;
    private Intent intent;
    private ImageView profileImage;
    private String title_toolbar;
    private String userInfo;
    private EditText editTextDescription;
    private String phone_number;
    SharedPreferences pref;
    ServerRequest sr;
    SharedPreferences.Editor edit;
    Button save;
    String ip,s;
    ArrayList<NameValuePair> parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent=getIntent();
        if(intent==null){
            Toast.makeText(EditProfile.this,"Error:No information recieved",Toast.LENGTH_SHORT).show();
            finish();
        }
        pref = this.getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        edit = pref.edit();
        phone_number=pref.getString(Config.phone_number,"");
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title_toolbar=intent.getExtras().getString("Title");
        userInfo=intent.getExtras().getString("Description");
        sr = new ServerRequest(getApplicationContext());

        save = (Button) findViewById(R.id.button);
        setTitle(title_toolbar);
        editTextDescription=(EditText)findViewById(R.id.editText_UserInfoEdit);
        editTextDescription.setText(userInfo);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo=editTextDescription.getText().toString();
                if(userInfo == null){
                    Toast.makeText(EditProfile.this, "Please fill data", Toast.LENGTH_SHORT).show();
                }
                else{
                    s = getKey();
                    if(s.equals("new_phone_number")){
                        s = "phone_number";
                        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(Config.phone_number, userInfo));
                        JSONObject json = sr.getJSON(Config.ip + "/ckeckExistNumber", params);
                        Log.d("here", "json received");
                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if (jsonstr.equals("yes")) {
                                    if(userInfo.length() == 10){
                                        openActivity(getE164Number());
                                        edit.putString(s, userInfo);

                                        Toast.makeText(getApplicationContext(), "New Phone number saved!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Enter 10 digit number!", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "New Phone number already existed!", Toast.LENGTH_SHORT).show();
                                }
                                Log.d("Hello", jsonstr);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        saveDetailsToDatabases();
                        edit.putString(s, userInfo);

                        Toast.makeText(getApplicationContext(), "New "+ s +" !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void saveDetailsToDatabases(){
        userInfo=editTextDescription.getText().toString();
        //sr = new ServerRequest(getApplicationContext());
        parameters=new ArrayList<>();
        //String key=getKey();
        parameters.add(new BasicNameValuePair(Config.phone_number,phone_number));
        parameters.add(new BasicNameValuePair(s,userInfo));
        Log.d("edit_detail",userInfo);
        Log.d("edit_ip",ip);
        Log.d("edit_key",s);
        String jsonstr = "not getting it";
        JSONObject json = sr.getJSON(Config.ip+ip,parameters);
        try {
            jsonstr = json.getString("response");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(EditProfile.this, jsonstr, Toast.LENGTH_SHORT).show();
    }

    private String getKey(){
        Log.d("title_needed",title_toolbar);
        if(title_toolbar.equals("Name")){
            ip = "/editProfile/user_name";
            return Config.user_name;
        }else if(title_toolbar.equals("Phone number")){
            Log.d("in_phone",phone_number);
            ip = "/editProfile/phone_number";
            return "new_phone_number";
        }else if(title_toolbar.equals("E-Mail")){
            ip = "/editProfile/email";
            return Config.email;
        }
        return "";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(EditProfile.this, MainActivity_ProfilePage.class);
        edit.commit();
        startActivity(intent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        edit.commit();
        Intent intent = new Intent(EditProfile.this, MainActivity_ProfilePage.class);
        startActivity(intent);
        finish();
    }

    private void openActivity(String phoneNumber) {
        Intent verification = new Intent(this, VerificationActivity.class);
        verification.putExtra("phonenumber", phoneNumber);
        verification.putExtra("code", "91");
        verification.putExtra("email",pref.getString(Config.email,""));
        verification.putExtra("user_name",pref.getString(Config.user_name,""));
        verification.putExtra("from","edit_phone");
        startActivity(verification);
        Log.d("start", "Activity started");
    }

    private String getE164Number() {
        return userInfo.replaceAll("\\D", "").trim();
        // return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
    }
}
