package com.example.locationapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapp.Admin.ReservationDetails;
import com.example.locationapp.R;

import java.text.SimpleDateFormat;
import java.util.List;

import models.Reservation;
import services.PictureService;
import services.PictureServiceImpl;

public class ClientReservationsAdapter extends RecyclerView.Adapter<ClientReservationsAdapter.ReservationViewHolder> {
    private Context context;
    private List<Reservation> reservationList;

    public ClientReservationsAdapter(Context context, List<Reservation> reservationList) {
        this.context = context;
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_client_reservations_adapter, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);
        Log.d("Reservation", reservation.toString());

        if (reservation.car != null) {
            holder.tvCarName.setText(reservation.car.model);
            PictureService pictureService = new PictureServiceImpl();
            Bitmap pic = pictureService.decompressBase64ToImage(reservation.car.picture);
            holder.ivCarImage.setImageBitmap(pic);
        } else {
            holder.tvCarName.setText("Car information unavailable");
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedStartDate = dateFormat.format(reservation.dateStart);
        String formattedEndDate = dateFormat.format(reservation.dateEnd);
        holder.tvDates.setText(formattedStartDate + " - " + formattedEndDate);

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
        TextView tvCarName, tvDates;
        Button btnDetails;
        ImageView ivCarImage;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvDates = itemView.findViewById(R.id.tvDatesReservation);
            btnDetails = itemView.findViewById(R.id.btnDetails);
            ivCarImage = itemView.findViewById(R.id.ivCarImage);
        }
    }
}
