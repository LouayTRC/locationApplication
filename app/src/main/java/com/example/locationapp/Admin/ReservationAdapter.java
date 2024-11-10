package com.example.locationapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.locationapp.R;

import java.util.List;

import Config.RetrofitClient;
import models.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.PictureService;
import services.PictureServiceImpl;
import services.ReservationService;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;

    // Updated constructor to accept a Context parameter
    public ReservationAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
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
        Log.d("Reservation", reservation.toString());

        // Display car information
        if (reservation.car != null ) {
            Log.d("Car Info", "Model: " + reservation.car.model);
            holder.tvCarName.setText(reservation.car.model);

            PictureService pictureService = new PictureServiceImpl();
            Bitmap pic = pictureService.decompressBase64ToImage(reservation.car.picture);
            holder.ivCarImage.setImageBitmap(pic);
        } else {
            holder.tvCarName.setText("Car information unavailable");
        }

        // Display client information
        if (reservation.client != null && reservation.client.user != null) {
            holder.tvClientName.setText(reservation.client.user.name);
        } else {
            holder.tvClientName.setText("Client information unavailable");
        }

        // Set click listeners for btnCheck and btnCancel
        holder.ivCheck.setOnClickListener(v -> {
            updateReservationStatus(reservation._id, 1); // Update car status to 1
        });

        holder.ivCancel.setOnClickListener(v -> {
            updateReservationStatus(reservation._id, -1);
        });

        holder.btnDetails.setOnClickListener(v -> {
            // Navigate to the ReservationDetails activity and pass the reservationId as an extra
            Intent intent = new Intent(context, ReservationDetails.class);
            Log.d("id",reservation._id);
            intent.putExtra("reservationId", reservation._id);
            context.startActivity(intent);
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
        ImageView ivCarImage;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvClientName = itemView.findViewById(R.id.tvClientName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            ivCancel = itemView.findViewById(R.id.ivCancel);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            ivCarImage = itemView.findViewById(R.id.ivCarImage);
        }
    }

    private void updateReservationStatus(String reservationId, Integer status) {
        // Use the existing reservationService instance
        ReservationService reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);
        Call<Reservation> call = reservationService.updateReservationStatus(reservationId, status);
        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {
                if (response.isSuccessful()) {
                    // Successfully updated the car's status
                    Toast.makeText(context, "Reservation status updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(context, "Failed to update car status. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Toast.makeText(context, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }

        });
    }


}
