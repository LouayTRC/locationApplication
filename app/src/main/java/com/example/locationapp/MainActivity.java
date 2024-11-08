package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.locationapp.Admin.AdminHome;
import com.example.locationapp.Client.ClientHome;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void goToAdminHome(View view) {
        Intent intent = new Intent(MainActivity.this, AdminHome.class);
        startActivity(intent);
    }

    public void goToClientHome(View view) {
        Intent intent = new Intent(MainActivity.this, ClientHome.class);
        startActivity(intent);
    }

    public void goToDriverHome(View view) {
        Intent intent = new Intent(this, DriverHome.class);
        startActivity(intent);
    }
    public void goToReservationDriverList(View view) {
        Intent intent = new Intent(this, ReservationDriverList.class);
        startActivity(intent);
    }


}
