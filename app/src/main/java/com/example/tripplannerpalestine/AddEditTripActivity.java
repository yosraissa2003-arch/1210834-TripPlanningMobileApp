package com.example.tripplannerpalestine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Calendar;

public class AddEditTripActivity extends AppCompatActivity {

    private EditText etTitle, etDestination, etNotes;
    private Button btnStartDate, btnEndDate, btnSave;
    private RadioGroup rgDifficulty;
    private RadioButton rbEasy, rbModerate, rbHard;
    private CheckBox cbHotel, cbMeals;
    private Switch switchGuide;

    private String selectedStartDate = "";
    private String selectedEndDate = "";
    private Trip existingTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_trip);

        etTitle = findViewById(R.id.etTitle);
        etDestination = findViewById(R.id.etDestination);
        etNotes = findViewById(R.id.etNotes);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnSave = findViewById(R.id.btnSaveTrip);
        rgDifficulty = findViewById(R.id.rgDifficulty);
        rbEasy = findViewById(R.id.rbEasy);
        rbModerate = findViewById(R.id.rbModerate);
        rbHard = findViewById(R.id.rbHard);
        cbHotel = findViewById(R.id.cbHotel);
        cbMeals = findViewById(R.id.cbMeals);
        switchGuide = findViewById(R.id.switchGuide);

        String tripId = getIntent().getStringExtra("trip_id");
        if (tripId != null) {
            existingTrip = TripStorage.getTripById(this, tripId);
            if (existingTrip != null) {
                setTitle("Edit Trip");
                fillData(existingTrip);
            }
        } else {
            setTitle("Add New Trip");
        }

        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));

        btnSave.setOnClickListener(v -> saveTrip());
    }

    private void fillData(Trip trip) {
        etTitle.setText(trip.getTitle());
        etDestination.setText(trip.getDestination());
        etNotes.setText(trip.getNotes());

        selectedStartDate = trip.getStartDate();
        selectedEndDate = trip.getEndDate();
        btnStartDate.setText(selectedStartDate);
        btnEndDate.setText(selectedEndDate);

        if ("Easy".equalsIgnoreCase(trip.getDifficulty())) {
            rbEasy.setChecked(true);
        } else if ("Moderate".equalsIgnoreCase(trip.getDifficulty())) {
            rbModerate.setChecked(true);
        } else {
            rbHard.setChecked(true);
        }

        cbHotel.setChecked(trip.isIncludeHotel());
        cbMeals.setChecked(trip.isIncludeMeals());
        switchGuide.setChecked(trip.isHasGuide());
    }

    private void showDatePicker(boolean isStart) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                (DatePicker view, int y, int m, int d) -> {
                    String date = (d < 10 ? "0" + d : d) + "/" +
                            ((m + 1) < 10 ? "0" + (m + 1) : (m + 1)) + "/" + y;
                    if (isStart) {
                        selectedStartDate = date;
                        btnStartDate.setText(date);
                    } else {
                        selectedEndDate = date;
                        btnEndDate.setText(date);
                    }
                }, year, month, day);
        dpd.show();
    }

    private void saveTrip() {
        String title = etTitle.getText().toString().trim();
        String destination = etDestination.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(destination)) {
            etDestination.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(selectedStartDate)) {
            Toast.makeText(this, "Select start date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(selectedEndDate)) {
            Toast.makeText(this, "Select end date", Toast.LENGTH_SHORT).show();
            return;
        }

        String difficulty = "Moderate";
        int selectedId = rgDifficulty.getCheckedRadioButtonId();
        if (selectedId == R.id.rbEasy) difficulty = "Easy";
        else if (selectedId == R.id.rbModerate) difficulty = "Moderate";
        else if (selectedId == R.id.rbHard) difficulty = "Hard";

        boolean includeHotel = cbHotel.isChecked();
        boolean includeMeals = cbMeals.isChecked();
        boolean hasGuide = switchGuide.isChecked();

        String id = existingTrip != null ? existingTrip.getId() : null;
        boolean favorite = existingTrip != null && existingTrip.isFavorite();

        Trip trip = new Trip(id, title, destination, selectedStartDate,
                selectedEndDate, difficulty, includeHotel, includeMeals,
                hasGuide, favorite, notes);

        TripStorage.addOrUpdateTrip(this, trip);
        Toast.makeText(this, "Trip saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
