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


    }

    public void goToCarList(View view) {
        Intent intent = new Intent(MainActivity.this, CarList.class);
        startActivity(intent);
    }

    public void goToCarResearch(View view) {
        Intent intent = new Intent(MainActivity.this, CarResearch.class);
        startActivity(intent);
    }

    public void goToReservationList(View view) {
        Intent intent = new Intent(this, ReservationList.class);
        startActivity(intent);
    }
    public void goToReservationDriverList(View view) {
        Intent intent = new Intent(this, ReservationDriverList.class);
        startActivity(intent);
    }


}
