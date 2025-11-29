package com.example.tripplannerpalestine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btnViewTrips = findViewById(R.id.btnViewTrips);
        Button btnStartPlanning = findViewById(R.id.btnStartPlanning);

        btnViewTrips.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, MainActivity.class))
        );

        btnStartPlanning.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AddEditTripActivity.class))
        );
    }
}
