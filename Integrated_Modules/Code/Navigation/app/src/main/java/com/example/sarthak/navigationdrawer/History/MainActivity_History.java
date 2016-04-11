package com.example.sarthak.navigationdrawer.History;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.sarthak.navigationdrawer.Backend.Backend.Config;
import com.example.sarthak.navigationdrawer.Backend.Backend.History;
import com.example.sarthak.navigationdrawer.Backend.Backend.ServerRequest;
import com.example.sarthak.navigationdrawer.LeaderBoard.User;
import com.example.sarthak.navigationdrawer.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity_History extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<History> histories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_history);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setTitle("History");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        histories = new ArrayList<>();
        getHistory();

        adapter = new RecycleViewAdapter(MainActivity_History.this, histories);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_history);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity_History.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    private void getHistory() {

        ArrayList<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Config.phone_number, "8758964908"));
        ServerRequest sr = new ServerRequest(MainActivity_History.this);
        Log.d("here", "params sent");
        //JSONObject json = sr.getJSON("http://127.0.0.1:8080/register",params);
        JSONObject json = sr.getJSON(Config.ip + "/history", params);
        Log.d("here", "json received");
        if (json != null) {
            try {
                Log.d("JsonLeaderBoard", "" + json);
                JSONArray historyArray = json.getJSONArray(Config.history);
                for (int i = 0; i < historyArray.length(); i++) {
                    Gson gson = new Gson();
                    History newHistory = gson.fromJson(historyArray.getString(i), new TypeToken<History>() {
                    }.getType());
                    histories.add(newHistory);
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
