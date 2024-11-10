package com.example.locationapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locationapp.ClientReservationsAdapter;
import com.example.locationapp.R;

import java.util.List;

import Config.RetrofitClient;
import models.Reservation;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ReservationService;

public class ClientReservations extends AppCompatActivity {

    private User connectedUser;
    private String token;
    private RecyclerView recyclerView;
    private ClientReservationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_reservations);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainReservations), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        connectedUser = intent.getParcelableExtra("user");
        token = intent.getStringExtra("token");

        recyclerView = findViewById(R.id.rvReservationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchReservations();
    }

    private void fetchReservations() {
        ReservationService reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);

        reservationService.getReservationsByUserId(connectedUser._id).enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reservation> reservations = response.body();
                    adapter = new ClientReservationsAdapter(ClientReservations.this, reservations);
                    recyclerView.setAdapter(adapter);
                    Log.d("Reservations", reservations.toString());
                } else {
                    Log.e("ReservationsError", "Failed to retrieve reservations: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("ReservationsFailure", "Network error: " + t.getMessage());
            }
        });
    }
}
