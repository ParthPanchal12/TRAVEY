package com.example.sarthak.navigationdrawer.Backend.Backend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.MapsActivity;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends Fragment {
    EditText phone_number,password,res_email,code,newpass;
    Button login,cont,cancel,cancel1,cont_code;
    TextView forgot_password;
    String phone_number_txt,password_txt,email_res_txt,code_txt,npass_txt;
    List<NameValuePair> params,params_history,params_req_send;
    SharedPreferences pref;
    ServerRequest sr;
    Dialog reset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, container, false);

        sr = new ServerRequest();
        phone_number = (EditText)view.findViewById(R.id.login_phone_number);
        password = (EditText)view.findViewById(R.id.login_password);
        login = (Button)view.findViewById(R.id.login_login);
        forgot_password = (TextView)view.findViewById(R.id.login_forgot_password);
        pref = this.getActivity().getSharedPreferences("AppPref", Context.MODE_PRIVATE);



        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                phone_number_txt = phone_number.getText().toString();
                password_txt = password.getText().toString();

                char ph_1 = phone_number_txt.charAt(0);
                int ph1 = Integer.parseInt(String.valueOf(phone_number_txt.charAt(0)));
                int ph2 = Integer.parseInt(String.valueOf(phone_number_txt.charAt(1)));
                int ph3 = Integer.parseInt(String.valueOf(phone_number_txt.charAt(2)));

                if((ph_1=='+' && ph2==9 && ph3==1 && phone_number_txt.length()==13) || (ph1==0 && phone_number_txt.length()==11) || (phone_number_txt.length()==10)){

                    params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("phone_number", phone_number_txt));
                    params.add(new BasicNameValuePair("password", password_txt));

                    ServerRequest sr = new ServerRequest();
                    JSONObject json = sr.getJSON(Config.ip+"/login",params);
                    if(json != null){
                        try{
                            String jsonstr = json.getString("response");
                            if(json.getBoolean("res")){
                                String token = json.getString("token");
                                String grav = json.getString("grav");
                                SharedPreferences.Editor edit = pref.edit();
                                //Storing Data using SharedPreferences
                                edit.putString("token", token);
                                edit.putString("grav", grav);
                                edit.commit();


                                History h = new History();


                                h.date = "19/2/96";
                                h.source = "Gandhinagar";
                                h.destination = "Ahmedabad";

                                Gson gson = new Gson();
                                String s = gson.toJson(h);

                                params_history = new ArrayList<NameValuePair>();
                                params_history.add(new BasicNameValuePair("phone_number", phone_number_txt));
                                params_history.add(new BasicNameValuePair("history", s));
                                //JSONObject json1 = sr.getJSON(Register.IP+"/reportAdd",params_history);


                                params_req_send = new ArrayList<NameValuePair>();
                                params_req_send.add(new BasicNameValuePair("fromn","from__n"));
                                params_req_send.add(new BasicNameValuePair("fromu","from__u"));
                                params_req_send.add(new BasicNameValuePair("to","to__"));
                                params_req_send.add(new BasicNameValuePair("ttle","title__"));
                                //JSONObject json1 = sr.getJSON(Config.ip+"/send",params_req_send);

                                Intent profactivity = new Intent(getContext(),MapsActivity.class);
                                edit.putString("phone_number", phone_number_txt);
                                startActivity(profactivity);
                                ((Activity)getContext()).finish();
                            }

                            Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else{
                    Toast.makeText(getContext(), "Phone number not valid !" ,Toast.LENGTH_LONG).show();
                }
            }
        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset = new Dialog(getContext());
                reset.setTitle("Reset Password");
                reset.setContentView(R.layout.reset_pass_init);
                cont = (Button) reset.findViewById(R.id.resbtn);
                cancel = (Button) reset.findViewById(R.id.cancelbtn);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reset.dismiss();
                    }
                });
                res_email = (EditText) reset.findViewById(R.id.email);

                cont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        email_res_txt = res_email.getText().toString();

                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("email", email_res_txt));

                        //  JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass", params);
                        JSONObject json = sr.getJSON(Config.ip+"/api/resetpass", params);

                        if (json != null) {
                            try {
                                String jsonstr = json.getString("response");
                                if (json.getBoolean("res")) {
                                    Log.e("JSON", jsonstr);
                                    Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();
                                    reset.setContentView(R.layout.reset_pass_code);
                                    cont_code = (Button) reset.findViewById(R.id.conbtn);
                                    code = (EditText) reset.findViewById(R.id.code);
                                    newpass = (EditText) reset.findViewById(R.id.npass);
                                    cancel1 = (Button) reset.findViewById(R.id.cancel);
                                    cancel1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            reset.dismiss();
                                        }
                                    });
                                    cont_code.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            code_txt = code.getText().toString();
                                            npass_txt = newpass.getText().toString();
                                            Log.e("Code", code_txt);
                                            Log.e("New pass", npass_txt);
                                            params = new ArrayList<NameValuePair>();
                                            params.add(new BasicNameValuePair("email", email_res_txt));
                                            params.add(new BasicNameValuePair("code", code_txt));
                                            params.add(new BasicNameValuePair("newpass", npass_txt));

                                            JSONObject json = sr.getJSON(Config.ip + "/api/resetpass/chg", params);
                                            //   JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass/chg", params);

                                            if (json != null) {
                                                try {

                                                    String jsonstr = json.getString("response");
                                                    if (json.getBoolean("res")) {
                                                        reset.dismiss();
                                                        Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                                    } else {
                                                        Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                        }
                                    });
                                } else {

                                    Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });


                reset.show();
            }
        });


        return view;
    }

}





