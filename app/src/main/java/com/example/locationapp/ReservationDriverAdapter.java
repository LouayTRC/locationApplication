package com.example.locationapp;

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

import java.util.List;

import Config.RetrofitClient;
import models.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.PictureService;
import services.PictureServiceImpl;
import services.ReservationService;

public class ReservationDriverAdapter extends RecyclerView.Adapter<ReservationDriverAdapter.ReservationViewHolder> {

    private Context context;
    private List<Reservation> reservationList;

    public ReservationDriverAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservationList = reservations;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_rersevation_driver_adapter, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        Log.d("Reservation", reservation.toString());

        // Display car information
        if (reservation.car != null) {
            holder.tvCarName.setText(reservation.car.model);

            PictureService pictureService = new PictureServiceImpl();
            Bitmap pic = pictureService.decompressBase64ToImage(reservation.car.picture);
            holder.ivCarImage.setImageBitmap(pic);
        } else {
            holder.tvCarName.setText("Car information unavailable");
        }

        // Display client information
        if (reservation.client != null && reservation.client.user != null) {
            holder.tvClientName.setText(reservation.client.user.getName());
        } else {
            holder.tvClientName.setText("Client information unavailable");
        }

        // Set click listeners for buttons

        holder.btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReservationDetails.class);
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
        ImageView ivCheck, ivMsg;
        Button btnDetails;
        ImageView ivCarImage;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarNameD);
            tvClientName = itemView.findViewById(R.id.tvClientNameD);
            ivCheck = itemView.findViewById(R.id.ivCheckD);
            ivMsg = itemView.findViewById(R.id.ivMsgD);
            btnDetails = itemView.findViewById(R.id.btnDetailsD);
            ivCarImage = itemView.findViewById(R.id.ivCarImageD);
        }
    }

}
