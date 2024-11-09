package com.example.locationapp;


import android.os.Bundle;
import android.view.View;
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
        setContentView(R.layout.activity_driver_list);  // Replace with your actual layout

        recyclerViewDrivers = findViewById(R.id.recyclerViewDrivers);
        recyclerViewDrivers.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of drivers (replace with your actual data source)
        drivers = new ArrayList<>();
        drivers.add(new Driver("John Doe", "123456789"));
        drivers.add(new Driver("Jane Smith", "987654321"));

        // Create the adapter and set it to the RecyclerView
        driverAdapter = new DriverAdapter(drivers, new DriverAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Driver driver) {
                // Handle the item click
                Toast.makeText(DriverListActivity.this, "Clicked: " + driver.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewDrivers.setAdapter(driverAdapter);
    }
}
