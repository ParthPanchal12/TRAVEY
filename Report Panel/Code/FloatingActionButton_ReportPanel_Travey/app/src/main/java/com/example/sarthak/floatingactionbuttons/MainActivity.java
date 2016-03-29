package com.example.sarthak.floatingactionbuttons;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FloatingActionsMenu fab_menu_report_panel=(FloatingActionsMenu)findViewById(R.id.fab_report_panel);

        FloatingActionButton fab_accident=(FloatingActionButton)findViewById(R.id.fab_report_accident);
        fab_accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                new EnterReportParameters(MainActivity.this,"Accident").show();
            }
        });
        FloatingActionButton fab_roadblock=(FloatingActionButton)findViewById(R.id.fab_report_roadblock);
        fab_roadblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                new EnterReportParameters(MainActivity.this,"Roadblock").show();
            }
        });
        FloatingActionButton fab_event=(FloatingActionButton)findViewById(R.id.fab_report_event);
        fab_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu_report_panel.collapse();
                new EnterReportParameters(MainActivity.this,"Event").show();
            }
        });
    }
}
