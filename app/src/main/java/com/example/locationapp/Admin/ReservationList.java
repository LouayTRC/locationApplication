package com.example.locationapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapp.R;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
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
        reservationAdapter = new ReservationAdapter(this, reservationList); // Pass the context to the adapter
        recyclerView.setAdapter(reservationAdapter);

        // Fetch reservations from the API
        fetchReservations();
    }

    public void goToReservaionDetails(View view) {
        Intent intent = new Intent(this, ReservationDetails.class);
        startActivity(intent);
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
}
