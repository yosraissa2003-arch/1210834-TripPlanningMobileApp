package com.example.tripplannerpalestine;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TripStorage {

    private static final String PREF_NAME = "trip_prefs";
    private static final String KEY_TRIPS = "trips_json";
    private static final String KEY_SEEDED = "sample_seeded";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static List<Trip> getTrips(Context context) {
        SharedPreferences prefs = getPrefs(context);
        String json = prefs.getString(KEY_TRIPS, "[]");
        List<Trip> trips = new ArrayList<>();

        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                trips.add(Trip.fromJson(obj));
            }
        } catch (JSONException e) {
            Log.e("TripStorage", "Error parsing trips JSON", e);
        }

        return trips;
    }

    public static void saveTrips(Context context, List<Trip> trips) {
        JSONArray array = new JSONArray();
        for (Trip t : trips) {
            try {
                array.put(t.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        getPrefs(context).edit().putString(KEY_TRIPS, array.toString()).apply();
    }

    public static void addOrUpdateTrip(Context context, Trip trip) {
        List<Trip> trips = getTrips(context);
        boolean found = false;
        for (int i = 0; i < trips.size(); i++) {
            if (trips.get(i).getId().equals(trip.getId())) {
                trips.set(i, trip);
                found = true;
                break;
            }
        }
        if (!found) {
            trips.add(trip);
        }
        saveTrips(context, trips);
    }

    public static Trip getTripById(Context context, String id) {
        List<Trip> trips = getTrips(context);
        for (Trip t : trips) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    public static void updateFavorite(Context context, String id, boolean favorite) {
        List<Trip> trips = getTrips(context);
        for (Trip t : trips) {
            if (t.getId().equals(id)) {
                t.setFavorite(favorite);
                break;
            }
        }
        saveTrips(context, trips);
    }

  
    public static void ensureSampleData(Context context) {
        SharedPreferences prefs = getPrefs(context);
        boolean seeded = prefs.getBoolean(KEY_SEEDED, false);
        if (seeded) return;

        List<Trip> sample = new ArrayList<>();

        sample.add(new Trip(null,
                "Jerusalem Old City Highlights",
                "Jerusalem",
                "11/12/2025",
                "12/12/2025",
                "Moderate",
                true,
                true,
                true,
                true,
                "Walking tour around Al-Aqsa, Old City markets, and historical gates."
        ));

        sample.add(new Trip(null,
                "Bethlehem & Hebron Heritage Day",
                "Bethlehem & Hebron",
                "15/12/2025",
                "15/12/2025",
                "Easy",
                true,
                false,
                true,
                false,
                "Visit Church of the Nativity, Ibrahimi Mosque, and local glass workshops."
        ));

        sample.add(new Trip(null,
                "Jericho & Wadi Qelt Adventure",
                "Jericho",
                "20/12/2025",
                "21/12/2025",
                "Moderate",
                false,
                false,
                false,
                false,
                "Cable car, Hisham’s Palace mosaics, and desert viewpoints."
        ));

        saveTrips(context, sample);
        prefs.edit().putBoolean(KEY_SEEDED, true).apply();
    }
}
