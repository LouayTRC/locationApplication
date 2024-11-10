package com.example.locationapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import models.Driver;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriverViewHolder> {

    private List<Driver> drivers;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Driver driver);
    }

    public DriverAdapter(List<Driver> drivers, OnItemClickListener onItemClickListener) {
        this.drivers = drivers;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public DriverViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_driver_conversation, parent, false);
        return new DriverViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriverViewHolder holder, int position) {
        Driver driver = drivers.get(position);
        holder.nameTextView.setText(driver.user.name); // Make sure Driver has a getName() method
        holder.phoneTextView.setText(driver.user.phone); // Make sure Driver has a getPhoneNumber() method

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(driver));
    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public static class DriverViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;

        public DriverViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewDriverName);
        }
    }
}
