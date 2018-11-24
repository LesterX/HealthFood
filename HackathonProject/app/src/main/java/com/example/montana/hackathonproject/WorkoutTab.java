package com.example.montana.hackathonproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WorkoutTab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button pointsBtn = (Button) findViewById(R.id.pointsButton);
        pointsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText hours = (EditText) findViewById(R.id.hoursEditText);
                TextView totalpoints=(TextView)findViewById(R.id.totalPointsTextView);

                int num = Integer.parseInt(hours.getText().toString());
                int pointsgained= (num*5) -2;
                totalpoints.setText(pointsgained + "");


            }
        });

        Button addprogressBtn = (Button) findViewById(R.id.addPointsButton);
        addprogressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView totalpoints=(TextView)findViewById(R.id.totalPointsTextView);
                int num = Integer.parseInt(totalpoints.getText().toString());
                Intent myIntent = new Intent(WorkoutTab.this, Profilestats.class);
                myIntent.putExtra("inttoadd", num);
                startActivity(myIntent);

            }
        });
    }

}
