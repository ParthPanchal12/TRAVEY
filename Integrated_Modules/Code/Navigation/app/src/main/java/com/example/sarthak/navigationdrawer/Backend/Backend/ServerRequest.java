package com.example.sarthak.navigationdrawer.Backend.Backend;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.JsonIOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ServerRequest {

    static InputStream is = null;
    static JSONObject jObj = null;
    static JSONArray jArray = null;
    static String json = "";
    private Context _context;
    SweetAlertDialog progressBar;

    public ServerRequest(Context context) {
        this._context = context;
        //Looper.prepare();
        /*if(!isConnectingToInternet()){
            getDialogCheckInternet();
        }*/
    }

    public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {


        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }

    public JSONArray getJSONArrayFromUrl(String url, List<NameValuePair> params) {


        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
            Log.e("JSON", json);
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        try {
            jArray = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jArray;

    }

    JSONObject jobj;

    public JSONObject getJSON(String url, List<NameValuePair> params) {

        Params param = new Params(url, params);
        Request myTask = new Request();
        try {
            jobj = myTask.execute(param).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return jobj;
    }

    JSONArray jarray;

    public JSONArray getJSONArray(String url, List<NameValuePair> params) {

        Params param = new Params(url, params);
        RequestArray myTask = new RequestArray();
        try {
            jarray = myTask.execute(param).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return jarray;
    }

    private static class Params {
        String url;
        List<NameValuePair> params;


        Params(String url, List<NameValuePair> params) {
            this.url = url;
            this.params = params;

        }
    }

    private class Request extends AsyncTask<Params, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(Params... args) {
            ServerRequest request = new ServerRequest(_context);

            Log.d("ip",Config.ip);
            JSONObject json = request.getJSONFromUrl(args[0].url, args[0].params);


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            super.onPostExecute(json);
        }


    }

    private class RequestArray extends AsyncTask<Params, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(Params... args) {

            ServerRequest request = new ServerRequest(_context);
            JSONArray json = request.getJSONArrayFromUrl(args[0].url, args[0].params);

            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {

            super.onPostExecute(json);

        }

    }


    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    private void getDialogCheckInternet() {

        boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this._context)
                .title("You are not connected to the internet")
                .customView(R.layout.conect_internet, wrapInScrollView)
                .positiveText("Retry")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //add to database and dismiss dialog
                        if (!isConnectingToInternet()) {
                            getDialogCheckInternet();
                        }
                    }
                })
                .show();
    }

}