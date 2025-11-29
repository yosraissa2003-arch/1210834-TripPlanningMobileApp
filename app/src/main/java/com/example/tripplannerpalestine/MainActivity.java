package com.example.tripplannerpalestine;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TripAdapter.OnTripClickListener {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> allTrips = new ArrayList<>();
    private EditText etSearch;
    private static final String TAG = "MainActivity";
    private static final String KEY_SEARCH = "search_query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        TextView tvTitle = findViewById(R.id.tvAppTitle);
        tvTitle.setText("Explore Palestine – Trip Planner");

        etSearch = findViewById(R.id.etSearch);
        Button btnAddTrip = findViewById(R.id.btnAddTrip);

        recyclerView = findViewById(R.id.recyclerTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TripStorage.ensureSampleData(this);
        allTrips = TripStorage.getTrips(this);

        adapter = new TripAdapter(this, allTrips, this);
        recyclerView.setAdapter(adapter);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnAddTrip.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTripActivity.class);
            startActivity(intent);
        });

        if (savedInstanceState != null) {
            String q = savedInstanceState.getString(KEY_SEARCH, "");
            etSearch.setText(q);
            filterTrips(q);
        }
    }

    private void filterTrips(String query) {
        String q = query.toLowerCase().trim();
        List<Trip> filtered = new ArrayList<>();
        for (Trip t : allTrips) {
            if (t.getTitle().toLowerCase().contains(q) ||
                    t.getDestination().toLowerCase().contains(q)) {
                filtered.add(t);
            }
        }
        adapter.updateList(filtered);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");

        allTrips = TripStorage.getTrips(this);
        filterTrips(etSearch.getText().toString());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onTripClick(Trip trip) {
        Intent intent = new Intent(MainActivity.this, TripDetailsActivity.class);
        intent.putExtra("trip_id", trip.getId());
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            outState.putString(KEY_SEARCH, etSearch.getText().toString());
        }
    }
}
