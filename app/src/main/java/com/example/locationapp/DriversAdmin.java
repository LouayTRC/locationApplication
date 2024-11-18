package com.example.locationapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Driver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.DriverService;

public class DriversAdmin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DriversAdminAdapter driverAdapter;  // Correct adapter class
    private List<Driver> driverList;
    private DriverService driverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drivers_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.driversList);
        driverList = new ArrayList<>();

        // Configure RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        driverAdapter = new DriversAdminAdapter(this, driverList); // Use DriversAdminAdapter
        recyclerView.setAdapter(driverAdapter);

        // Fetch drivers from the API
        fetchDrivers();
    }

    private void fetchDrivers() {
        driverService = RetrofitClient.getRetrofitInstance().create(DriverService.class);
        Call<List<Driver>> call = driverService.getAllDrivers();

        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(@NonNull Call<List<Driver>> call, @NonNull Response<List<Driver>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    driverList.clear();
                    driverList.addAll(response.body());
                    driverAdapter.notifyDataSetChanged();
                } else {
                    // Log response code and message for debugging
                    Toast.makeText(DriversAdmin.this, "Failed to fetch drivers: " + response.code() + " " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Driver>> call, @NonNull Throwable t) {
                Toast.makeText(DriversAdmin.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
