package com.example.tripplannerpalestine;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView tvTitle, tvDestination, tvDates, tvDifficulty, tvServices, tvNotes;
    private Switch switchFavorite;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        tvTitle = findViewById(R.id.tvDetailTitle);
        tvDestination = findViewById(R.id.tvDetailDestination);
        tvDates = findViewById(R.id.tvDetailDates);
        tvDifficulty = findViewById(R.id.tvDetailDifficulty);
        tvServices = findViewById(R.id.tvDetailServices);
        tvNotes = findViewById(R.id.tvDetailNotes);
        switchFavorite = findViewById(R.id.switchDetailFavorite);
        Button btnEdit = findViewById(R.id.btnEditTrip);

        String tripId = getIntent().getStringExtra("trip_id");
        if (tripId != null) {
            trip = TripStorage.getTripById(this, tripId);
        }

        if (trip == null) {
            Toast.makeText(this, "Trip not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("Trip Details");
        bindData();

        switchFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            trip.setFavorite(isChecked);
            TripStorage.addOrUpdateTrip(TripDetailsActivity.this, trip);
        });

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(TripDetailsActivity.this, AddEditTripActivity.class);
            intent.putExtra("trip_id", trip.getId());
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data if edited
        if (trip != null) {
            trip = TripStorage.getTripById(this, trip.getId());
            bindData();
        }
    }

    private void bindData() {
        if (trip == null) return;

        tvTitle.setText(trip.getTitle());
        tvDestination.setText(trip.getDestination());
        String dates = trip.getStartDate() + " - " + trip.getEndDate();
        tvDates.setText(dates);
        tvDifficulty.setText("Difficulty: " + trip.getDifficulty());

        StringBuilder services = new StringBuilder();
        services.append(trip.isIncludeHotel() ? "Hotel included\n" : "");
        services.append(trip.isIncludeMeals() ? "Meals included\n" : "");
        services.append(trip.isHasGuide() ? "Local guide provided\n" : "");
        if (services.length() == 0) services.append("No extra services selected");
        tvServices.setText(services.toString());

        tvNotes.setText(trip.getNotes().isEmpty() ? "No notes" : trip.getNotes());
        switchFavorite.setOnCheckedChangeListener(null);
        switchFavorite.setChecked(trip.isFavorite());
        switchFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            trip.setFavorite(isChecked);
            TripStorage.addOrUpdateTrip(TripDetailsActivity.this, trip);
        });
    }
}
