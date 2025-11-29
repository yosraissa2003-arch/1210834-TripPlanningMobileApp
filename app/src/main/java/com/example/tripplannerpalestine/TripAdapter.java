package com.example.tripplannerpalestine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    public interface OnTripClickListener {
        void onTripClick(Trip trip);
    }

    private Context context;
    private List<Trip> trips = new ArrayList<>();
    private OnTripClickListener listener;

    public TripAdapter(Context context, List<Trip> trips, OnTripClickListener listener) {
        this.context = context;
        this.trips = trips;
        this.listener = listener;
    }

    public void updateList(List<Trip> newTrips) {
        this.trips = newTrips;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.tvTitle.setText(trip.getTitle());
        holder.tvDestination.setText(trip.getDestination());
        String dates = trip.getStartDate() + " - " + trip.getEndDate();
        holder.tvDates.setText(dates);
        holder.tvDifficulty.setText(trip.getDifficulty());

        // نمنع إعادة تشغيل الـ listener القديم عند إعادة التدوير
        holder.switchFavorite.setOnCheckedChangeListener(null);
        holder.switchFavorite.setChecked(trip.isFavorite());
        holder.switchFavorite.setOnCheckedChangeListener((buttonView, isChecked) -> {
            trip.setFavorite(isChecked);
            TripStorage.updateFavorite(context, trip.getId(), isChecked);
        });

        // الضغط على الكارد كله → يفتح التفاصيل
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripClick(trip);
            }
        });

        // زر Details يعمل نفس وظيفة الضغط على الكارد
        holder.btnDetails.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripClick(trip);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips != null ? trips.size() : 0;
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDestination, tvDates, tvDifficulty;
        Switch switchFavorite;
        Button btnDetails;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDates = itemView.findViewById(R.id.tvDates);
            tvDifficulty = itemView.findViewById(R.id.tvDifficulty);
            switchFavorite = itemView.findViewById(R.id.switchFavorite);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
