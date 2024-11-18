package com.example.locationapp;

import android.content.Context;
import android.content.Intent;
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
import models.Driver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.DriverService;

public class DriversAdminAdapter extends RecyclerView.Adapter<DriversAdminAdapter.DriverViewHolder> {

    private Context context;
    private List<Driver> driverList;

    public DriversAdminAdapter(Context context, List<Driver> driverList) {
        this.context = context;
        this.driverList = driverList;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_drivers_admin_adapter, parent, false); // Ensure the layout file is correct
        return new DriverViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = driverList.get(position);

        // Display driver information
        holder.tvDriverName.setText(driver.user.name);

        // Display additional information, such as region if needed
        holder.tvRegionAdmin.setText(driver.region);

        if (driver.user.status == 1) {
            holder.tvStatus.setText("Actif");
        } else {
            holder.tvStatus.setText("Blocked");
        }

        // Set click listeners for buttons
        holder.ivCheck.setOnClickListener(v -> {
            updateStatus(driver.user._id, 1);
            driver.user.status=1;
        });

        holder.ivCancel.setOnClickListener(v -> {
            updateStatus(driver.user._id, 0);
            driver.user.status=0;
        });


    }

    private void updateStatus(String driverId, int status) {
        DriverService driverService= RetrofitClient.getRetrofitInstance().create(DriverService.class);
        Call<Void> call = driverService.updateStatus(driverId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    String statusMessage = (status == 1) ? "Driver marked as available." : "Driver marked as unavailable.";

                    Toast.makeText(context, statusMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to update status.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return driverList.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {

        TextView tvDriverName, tvRegionAdmin,tvStatus;
        ImageView ivCheck, ivCancel;


        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDriverName = itemView.findViewById(R.id.driverName);
            tvRegionAdmin = itemView.findViewById(R.id.regionName);
            ivCheck = itemView.findViewById(R.id.ivCheck);
            ivCancel = itemView.findViewById(R.id.ivCancel);
            tvStatus=itemView.findViewById(R.id.status);
        }
    }
}
