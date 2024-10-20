package com.example.locationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import models.Reservation;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private List<Reservation> reservationList;

    public ReservationAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.tvCarName.setText(reservation.getCar().getModel());
        holder.tvClientName.setText("Name"); // Assurez-vous d'accéder au nom du client correctement

        // Handle status icon display
        if (reservation.getStatus() == 0) {
            holder.ivCheck.setVisibility(View.VISIBLE);
            holder.ivCancel.setVisibility(View.GONE);
        } else {
            holder.ivCheck.setVisibility(View.GONE);
            holder.ivCancel.setVisibility(View.VISIBLE);
        }

        // Handle "Details" button click
        holder.btnDetails.setOnClickListener(view -> {
            // Open reservation details
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder {

        TextView tvCarName, tvClientName;
        ImageView ivCheck, ivCancel;
        Button btnDetails;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            ivCancel = itemView.findViewById(R.id.ivCancel);
            btnDetails = itemView.findViewById(R.id.btnDetails);
        }
    }
}
