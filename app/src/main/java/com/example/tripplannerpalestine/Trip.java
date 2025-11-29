package com.example.tripplannerpalestine;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Trip {

    private String id;
    private String title;
    private String destination;
    private String startDate;
    private String endDate;
    private String difficulty;
    private boolean includeHotel;
    private boolean includeMeals;
    private boolean hasGuide;
    private boolean favorite;
    private String notes;

    public Trip() {
        this.id = UUID.randomUUID().toString();
    }

    public Trip(String id, String title, String destination, String startDate,
                String endDate, String difficulty, boolean includeHotel,
                boolean includeMeals, boolean hasGuide, boolean favorite, String notes) {
        this.id = (id == null || id.isEmpty()) ? UUID.randomUUID().toString() : id;
        this.title = title;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.difficulty = difficulty;
        this.includeHotel = includeHotel;
        this.includeMeals = includeMeals;
        this.hasGuide = hasGuide;
        this.favorite = favorite;
        this.notes = notes;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDestination() { return destination; }

    public void setDestination(String destination) { this.destination = destination; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public boolean isIncludeHotel() { return includeHotel; }

    public void setIncludeHotel(boolean includeHotel) { this.includeHotel = includeHotel; }

    public boolean isIncludeMeals() { return includeMeals; }

    public void setIncludeMeals(boolean includeMeals) { this.includeMeals = includeMeals; }

    public boolean isHasGuide() { return hasGuide; }

    public void setHasGuide(boolean hasGuide) { this.hasGuide = hasGuide; }

    public boolean isFavorite() { return favorite; }

    public void setFavorite(boolean favorite) { this.favorite = favorite; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public JSONObject toJson() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("title", title);
        obj.put("destination", destination);
        obj.put("startDate", startDate);
        obj.put("endDate", endDate);
        obj.put("difficulty", difficulty);
        obj.put("includeHotel", includeHotel);
        obj.put("includeMeals", includeMeals);
        obj.put("hasGuide", hasGuide);
        obj.put("favorite", favorite);
        obj.put("notes", notes);
        return obj;
    }

    public static Trip fromJson(JSONObject obj) throws JSONException {
        Trip trip = new Trip();
        trip.id = obj.optString("id", UUID.randomUUID().toString());
        trip.title = obj.optString("title");
        trip.destination = obj.optString("destination");
        trip.startDate = obj.optString("startDate");
        trip.endDate = obj.optString("endDate");
        trip.difficulty = obj.optString("difficulty");
        trip.includeHotel = obj.optBoolean("includeHotel", false);
        trip.includeMeals = obj.optBoolean("includeMeals", false);
        trip.hasGuide = obj.optBoolean("hasGuide", false);
        trip.favorite = obj.optBoolean("favorite", false);
        trip.notes = obj.optString("notes");
        return trip;
    }
}
