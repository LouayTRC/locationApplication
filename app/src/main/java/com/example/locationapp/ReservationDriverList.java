package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapp.Admin.ReservationDetails;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ReservationService;

public class ReservationDriverList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReservationDriverAdapter reservationDriverAdapter;
    private List<Reservation> reservationList;
    private ReservationService reservationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_driver_list);

        recyclerView = findViewById(R.id.rvReservationListD);
        reservationList = new ArrayList<>();

        // Configure RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reservationDriverAdapter = new ReservationDriverAdapter(this, reservationList);
        recyclerView.setAdapter(reservationDriverAdapter);

        // Fetch reservations with driver
        fetchReservations();
    }

    public void goToReservationDetails(View view) {
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
                    for (Reservation reservation : response.body()) {
                        if (reservation.driver!=null) {
                            reservationList.add(reservation);
                        }
                    }
                    reservationDriverAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ReservationDriverList.this, "Failed to fetch reservations: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Reservation>> call, @NonNull Throwable t) {
                Toast.makeText(ReservationDriverList.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
