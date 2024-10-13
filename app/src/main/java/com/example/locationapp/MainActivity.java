package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Optional: Ensure button visibility
        Button reservationsButton = findViewById(R.id.ButtonReservations);
        reservationsButton.setVisibility(View.VISIBLE);
    }

    public void goToCarList(View view) {
        Intent intent = new Intent(MainActivity.this, CarList.class);
        startActivity(intent);
    }

    public void goToCarResearch(View view) {
        Intent intent = new Intent(MainActivity.this, CarResearch.class);
        startActivity(intent);
    }

    public void goToReservations(View view) {
        Intent intent = new Intent(MainActivity.this, ReservationList.class);
        startActivity(intent);
    }
}
