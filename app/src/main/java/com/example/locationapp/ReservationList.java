package com.example.locationapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import models.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ReservationService;

public class ReservationList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationList;
    private ReservationService reservationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reservations_activity);

        recyclerView = findViewById(R.id.rvReservationList);
        reservationList = new ArrayList<>();

        // Configure RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationAdapter = new ReservationAdapter(reservationList);
        recyclerView.setAdapter(reservationAdapter);

        // Fetch reservations from the API
        fetchReservations();
    }

    private void fetchReservations() {
        reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);
        Call<List<Reservation>> call = reservationService.getReservations();

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reservation>> call, @NonNull Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reservationList.clear();

                    reservationList.addAll(response.body());
                    reservationAdapter.notifyDataSetChanged();
                } else {
                    // Log response code and message for debugging
                    Toast.makeText(ReservationList.this, "Failed to fetch reservations: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reservation>> call, @NonNull Throwable t) {
                Toast.makeText(ReservationList.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateCarStatus(Car car) {
        // Use the existing reservationService instance
        Call<Car> call = reservationService.updateCarStatus(car._id, car.status);
        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    // Successfully updated the car's status
                    Toast.makeText(ReservationList.this, "Car status updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle error response
                    Toast.makeText(ReservationList.this, "Failed to update car status. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                // Handle request failure
                Toast.makeText(ReservationList.this, "Network error. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
