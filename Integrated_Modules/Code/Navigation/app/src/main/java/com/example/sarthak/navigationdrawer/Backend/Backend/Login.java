package com.example.sarthak.navigationdrawer.Backend.Backend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sarthak.navigationdrawer.GCM.GCM;
import com.example.sarthak.navigationdrawer.MapsActivity;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends Fragment {
    EditText phone_number, password, res_email, code, newpass;
    Button login, cont, cancel, cancel1, cont_code;
    TextView forgot_password;
    String phone_number_txt, password_txt, email_res_txt, code_txt, npass_txt,shared_location;
    List<NameValuePair> params, params_history, params_req_send;
    SharedPreferences pref;
    ServerRequest sr;
    Dialog reset;
    SharedPreferences gcm_pref;
    private String regId;
    private SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, container, false);

        sr = new ServerRequest(getContext());
        phone_number = (EditText) view.findViewById(R.id.login_phone_number);
        password = (EditText) view.findViewById(R.id.login_password);
        login = (Button) view.findViewById(R.id.login_login);
        forgot_password = (TextView) view.findViewById(R.id.login_forgot_password);
        pref = this.getActivity().getSharedPreferences("AppPref", Context.MODE_PRIVATE);
        gcm_pref=this.getActivity().getSharedPreferences(GCM.class.getSimpleName(),Context.MODE_PRIVATE);
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Logging you in please Wait!");
        pDialog.setCancelable(false);
        //reset = new

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //call pdialog

                phone_number_txt = phone_number.getText().toString();
                password_txt = password.getText().toString();

                if (phone_number_txt.isEmpty() || password_txt.isEmpty()) {
                    Toast.makeText(getContext(), "Fill all details", Toast.LENGTH_SHORT).show();
                } else {


                        params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair(Config.phone_number, phone_number_txt));
                        params.add(new BasicNameValuePair(Config.password, password_txt));


                        regId = gcm_pref.getString("registration_id","");
                        if (regId == null) {
                            Toast.makeText(getContext(), "Could not create Registration id for GCM", Toast.LENGTH_SHORT).show();
                        } else if(regId.isEmpty()){
                            Toast.makeText(getContext(), "Could not create Registration id for GCM", Toast.LENGTH_SHORT).show();
                        } else{
                            Log.d("Adding gcm id to params",""+regId);
                            params.add(new BasicNameValuePair(Config.gcmId, regId));
                        }

                        Boolean isInternetPresent = sr.isConnectingToInternet(); // true or false
                        if (isInternetPresent) {
                            //ServerRequest sr = new ServerRequest();
                            JSONObject json = sr.getJSON(Config.ip + "/login", params);
                            if (json != null) {
                                try {
                                    String jsonstr = json.getString("response");
                                    if (json.getBoolean("res")) {
                                        String token = json.getString("token");
                                        String grav = json.getString("grav");
                                        String email = json.getString("email");
                                        String user_name = json.getString("user_name");
                                        shared_location = json.getString("shared_location");
                                        SharedPreferences.Editor edit = pref.edit();
                                        //Storing Data using SharedPreferences
                                        edit.putString("token", token);
                                        edit.putString("grav", grav);


                                        /*History h = new History();
                                        h.date = "19/2/96";
                                        h.source = "Gandhinagar";
                                        h.destination = "Ahmedabad";

                                        Gson gson = new Gson();
                                        String s = gson.toJson(h);
*/
                                        params_history = new ArrayList<NameValuePair>();
                                        params_history.add(new BasicNameValuePair(Config.phone_number, phone_number_txt));
                                        //params_history.add(new BasicNameValuePair(Config.history, s));
                                        //JSONObject json1 = sr.getJSON(Register.IP+"/reportAdd",params_history);


                                        params_req_send = new ArrayList<NameValuePair>();
                                        params_req_send.add(new BasicNameValuePair("fromn", "from__n"));
                                        params_req_send.add(new BasicNameValuePair("fromu", "from__u"));
                                        params_req_send.add(new BasicNameValuePair("to", "to__"));
                                        params_req_send.add(new BasicNameValuePair("title", "title__"));
                                        //JSONObject json1 = sr.getJSON(Config.ip+"/send",params_req_send);

                                        Intent profactivity = new Intent(getContext(), MapsActivity.class);
                                        edit.putString("phone_number", phone_number_txt);
                                        edit.putString("email", email);
                                        edit.putString("user_name",user_name);
                                        edit.putString("shared_location",shared_location);
                                        Log.d("chalo",shared_location);
                                        edit.commit();
                                        startActivity(profactivity);
                                        ((Activity) getContext()).finish();
                                    }

                                    Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "No Internet connection !", Toast.LENGTH_LONG).show();
                        }

                    }
                }

        });


        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                        .title("Forgot Password")
                        .customView(R.layout.reset_pass_init, true)
                        .positiveText("Continue")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //add to database and dismiss dialog
                                email_res_txt = res_email.getText().toString();

                                params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("email", email_res_txt));

                                //  JSONObject json = sr.getJSON("http://192.168.56.1:8080/api/resetpass", params);
                                JSONObject json = sr.getJSON(Config.ip + "/api/resetpass", params);

                                if (json != null) {
                                    try {
                                        String jsonstr = json.getString("response");
                                        if (json.getBoolean("res")) {
                                            Log.e("JSON", jsonstr);
                                            Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();
                                            MaterialDialog reset = new MaterialDialog.Builder(getContext())
                                                    .title("New password")
                                                    .customView(R.layout.reset_pass_code, true)
                                                    .positiveText("Reset")
                                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                                            code = (EditText) dialog.getCustomView().findViewById(R.id.code);
                                                            newpass = (EditText) dialog.getCustomView().findViewById(R.id.npass);

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
                                                                        dialog.dismiss();
                                                                        Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                                                    } else {
                                                                        Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                                                    }
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }

                                                        }
                                                    })
                                                    .negativeText("Cancel")
                                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                        @Override
                                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                            dialog.dismiss();
                                                        }
                                                    }).show();
                                            //reset.setContentView(R.layout.reset_pass_code);



                                        } else {

                                            Toast.makeText(getContext(), jsonstr, Toast.LENGTH_LONG).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        })
                        .negativeText("Cancel")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

                res_email = (EditText) dialog.getCustomView().findViewById(R.id.email);
            }
        });


        return view;
    }

}





