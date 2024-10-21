package com.example.locationapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        Log.d("reserb" , reservation.toString());
        // Log to check if Car and model are not null
        if (reservation.car != null) {
            Log.d("Car Info", "Model: " + reservation.car.model);
            holder.tvCarName.setText(reservation.car.model);
        } else {
            holder.tvCarName.setText("Car information unavailable");
        }

        if (reservation.client != null && reservation.client.user != null) {
            holder.tvClientName.setText(reservation.client.user.getName());
        } else {
            holder.tvClientName.setText("Client information unavailable");
        }
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
