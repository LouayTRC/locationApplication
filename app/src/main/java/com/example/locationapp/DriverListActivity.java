package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import models.Driver;

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
        drivers.add(new Driver("John Doe", "123456789"));
        drivers.add(new Driver("Jane Smith", "987654321"));

        // Set up the adapter and handle item click
        driverAdapter = new DriverAdapter(drivers, driver -> {
            Intent intent = new Intent(DriverListActivity.this, ChatActivity.class);
            intent.putExtra("driver_name", driver.getName());
            intent.putExtra("driver_phone", driver.getPhoneNumber());
            startActivity(intent);
        });

        recyclerViewDrivers.setAdapter(driverAdapter);
    }
}
