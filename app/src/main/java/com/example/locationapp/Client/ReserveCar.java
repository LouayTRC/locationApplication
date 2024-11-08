package com.example.locationapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.R;

import Config.RetrofitClient;
import models.Location;
import models.Requests.ReserveRequest;
import models.Reservation;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ReservationService;

public class ReserveCar extends AppCompatActivity {
    private EditText cinEditText, nameEditText, emailEditText, phoneEditText;
    private Button confirmButton;
    private Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_car);

        // Initialize EditTexts
        cinEditText = findViewById(R.id.cin);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);

        // Initialize the button
        confirmButton = findViewById(R.id.confirmButton); // Make sure you have a button in your layout

        // Set up button click listener
        confirmButton.setOnClickListener(v -> handleReservation());
    }

    private void handleReservation() {
        // Retrieve inputs
        String cin = cinEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        // Retrieve passed data
        Intent intent = getIntent();
        String dateStart = intent.getStringExtra("startDate");
        String dateEnd = intent.getStringExtra("endDate");
        boolean driverYes = intent.getBooleanExtra("driverYes", false);
        Double deliveryLongitude = intent.getDoubleExtra("deliveryLongitude", Double.NaN);
        Double deliveryLatitude = intent.getDoubleExtra("deliveryLatitude", Double.NaN);
        String carId=intent.getStringExtra("carId");

        // Create Location object
        Location location;
        if (!Double.isNaN(deliveryLongitude) && !Double.isNaN(deliveryLatitude)) {
            location = new Location(deliveryLatitude, deliveryLongitude);
        } else {
            location = null; // Or set to a default location if needed
        }

        // Create Client object
        User user = new User(cin, name, email, phone);


        // Create Reservation object
        ReserveRequest reservation = new ReserveRequest(carId, user, location, driverYes,dateStart,dateEnd);

        ReservationService reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);
        Log.d("request:",reservation.toString());

        Call<Reservation> reservationCall = reservationService.reserver(reservation);
        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation res = response.body();
                    // Log the successful reservation
                    Log.d("ReservationLog", res.toString());
                    Toast.makeText(ReserveCar.this, "Reservation added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Log the error response
                    Log.e("ReservationLog", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(ReserveCar.this, "Error adding Reservation: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                // Log the failure message
                Log.e("ReservationLog", "Failure: " + t.getMessage());
                Toast.makeText(ReserveCar.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
