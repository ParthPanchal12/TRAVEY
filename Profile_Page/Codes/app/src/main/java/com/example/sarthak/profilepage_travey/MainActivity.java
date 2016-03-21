package com.example.sarthak.profilepage_travey;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private Profile_ListViewAdapter adapter;
    private ArrayList<ProfileClass> profile;
    private Toolbar toolbar;
    private int userInfoIcons[] = {R.drawable.ic_call_black_24dp, R.drawable.ic_mail_black_24dp, R.drawable.ic_star_half_black_24dp, R.drawable.ic_location_on_black_24dp, R.drawable.ic_location_on_black_24dp};
    private String userInfoTitle[] = {"Phone", "Mail", "Rating", "Location Shared Status","Temp"};
    private String userInfoDescription[] = {"8758964908", "khandekarsarthak@gmail.com", "4.5", "Shared","Temp"};
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_ProfilePage);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_ProfilePage);
        collapsingToolbar.setTitle("Sarthak");
        setSupportActionBar(toolbar);
        ImageView header = (ImageView) findViewById(R.id.imageView_UserImage);
        listView = (ListView) findViewById(R.id.listView_userDetails);
        initialiseProfile();
        adapter = new Profile_ListViewAdapter(profile, MainActivity.this);
        listView.setAdapter(adapter);
    }

    private void initialiseProfile() {
        profile = new ArrayList<ProfileClass>();
        for (int i = 0; i < userInfoDescription.length; i++) {
            profile.add(new ProfileClass(userInfoIcons[i], userInfoTitle[i], userInfoDescription[i]));
        }
    }
}
