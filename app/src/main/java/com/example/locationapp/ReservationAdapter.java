package com.example.locationapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import models.Reservation;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Reservation> reservationList;

    // ViewHolder class to hold views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCarName;
        public TextView tvClientName;
        public ImageView ivCheck;
        public ImageView ivCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            ivCancel = itemView.findViewById(R.id.ivCancel);
        }
    }

    // Constructor
    public ReservationAdapter(Context context, ArrayList<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reservation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        holder.tvCarName.setText(reservation.car.model); // Assuming car has a model property
        holder.tvClientName.setText(reservation.client.user.getName()); // Assuming client has a user property

        // You can set the check and cancel icons based on reservation status
        if (reservation.status == 1) {
            holder.ivCheck.setVisibility(View.VISIBLE);
            holder.ivCancel.setVisibility(View.GONE);
        } else {
            holder.ivCheck.setVisibility(View.GONE);
            holder.ivCancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }
}
