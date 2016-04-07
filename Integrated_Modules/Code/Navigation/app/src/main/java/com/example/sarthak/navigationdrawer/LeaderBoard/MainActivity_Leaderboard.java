package com.example.sarthak.navigationdrawer.LeaderBoard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.sarthak.navigationdrawer.R;

import java.util.ArrayList;

public class MainActivity_Leaderboard extends AppCompatActivity {
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity__leaderboard);

        users=new ArrayList<>();
        getUsers();

        adapter = new RecycleViewAdapter(MainActivity_Leaderboard.this, users);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_leaderboard);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity_Leaderboard.this);
        recyclerView.setLayoutManager(linearLayoutManager);

    }
    private void getUsers(){

        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));
        users.add(new User("sda",1,3));

    }
}
