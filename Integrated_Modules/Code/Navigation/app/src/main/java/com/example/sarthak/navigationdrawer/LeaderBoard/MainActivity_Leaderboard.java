package com.example.sarthak.navigationdrawer.LeaderBoard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity_Leaderboard extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_leaderboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_LeaderBoard);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("LeaderBoard");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        users = new ArrayList<>();
        getUsers();

        Collections.sort(users);
        adapter = new RecycleViewAdapter(MainActivity_Leaderboard.this, users);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_leaderboard);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity_Leaderboard.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getUsers() {

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Config.phone_number, "1234567891"));
        ServerRequest sr = new ServerRequest(MainActivity_Leaderboard.this);
        Log.d("here", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONArray json = sr.getJSONArray(Config.ip + "/leaderBoard", params);
        Log.d("here", "json received");
        if (json != null) {
            try {
                Log.d("JsonLeaderBoard", "" + json);
                for (int i = 0; i < json.length(); i++) {
                    Gson gson = new Gson();
                    User newUser = gson.fromJson(json.getString(i), new TypeToken<User>() {
                    }.getType());

                    users.add(newUser);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
