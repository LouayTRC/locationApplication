package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

public class DriverListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDrivers;
    private DriverAdapter driverAdapter;
    private List<Driver> drivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_list);

        recyclerViewDrivers = findViewById(R.id.recyclerViewDrivers);
        recyclerViewDrivers.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of drivers (replace with your actual data source)
        drivers = new ArrayList<>();

        // Create Retrofit instance
        DriverService driverService = RetrofitClient.getRetrofitInstance().create(DriverService.class);

        // Execute the getAllDrivers API call
        driverService.getAllDrivers().enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    drivers = response.body();
                    // Handle the list of drivers here, e.g., display them in a RecyclerView
                    Log.d("DriverList", drivers.toString());
                } else {
                    Log.e("DriverListError", "Failed to retrieve drivers: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Log.e("DriverListFailure", "Network error: " + t.getMessage());
            }
        });

        // Set up the adapter and handle item click
        driverAdapter = new DriverAdapter(drivers, driver -> {
            Intent intent = new Intent(DriverListActivity.this, ChatActivity.class);

            startActivity(intent);
        });

        recyclerViewDrivers.setAdapter(driverAdapter);
    }
}
