package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Config.RetrofitClient;
import models.Location;
import models.Requests.ReserveRequest;
import models.Requests.SignupRequest;
import models.Reservation;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import services.AuthService;
import services.ReservationService;

public class Signup extends AppCompatActivity {
    private EditText cinEditText, nameEditText, emailEditText, phoneEditText,passwordEditText,roleEditText;
    private Button confirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize EditTexts
        cinEditText = findViewById(R.id.cin);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText=findViewById(R.id.pwdInput);
        roleEditText=findViewById(R.id.roleInput);


        // Initialize the button
        confirmButton = findViewById(R.id.confirmButton); // Make sure you have a button in your layout

        // Set up button click listener
        confirmButton.setOnClickListener(v -> signup());
    }

    public void signup(){
        String cin = cinEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String pwd = passwordEditText.getText().toString().trim();
        String role = roleEditText.getText().toString().trim();

        SignupRequest signupRequest = new SignupRequest(cin, phone, email, pwd,name,role);

        AuthService authService=RetrofitClient.getRetrofitInstance().create(AuthService.class);
        if (validateInputs()){
            Call<Void> signupCall = authService.signup(signupRequest);
            signupCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Log the successful reservation
                        if (role.equals("CLIENT")) {
                            Toast.makeText(Signup.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(Signup.this, "Account created successfully! \n you must wait for access", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                    } else {
                        // Log the error response
                        Log.e("SignupLog", "Error: " + response.code() + " - " + response.message());
                        Toast.makeText(Signup.this, "Error signup: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Log the failure message
                    Log.e("SignupLog", "Failure: " + t.getMessage());
                    Toast.makeText(Signup.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInputs() {
        // Check if model is empty
        if (nameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if year is valid
        String phone = phoneEditText.getText().toString().trim();
        if (phone.length()!=8) {
            Toast.makeText(this, "Phone is required and have 8 characters .", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if price is valid
        String pwd = passwordEditText.getText().toString().trim();
        if (pwd.isEmpty()) {
            Toast.makeText(this, "Password is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if features and description are empty
        if (emailEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (cinEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Cin is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (roleEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Role is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    /*private void handleReservation() {
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
        //User user = new User(cin, name, email, phone);


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
                    Toast.makeText(Signup.this, "Reservation added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Log the error response
                    Log.e("ReservationLog", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(Signup.this, "Error adding Reservation: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                // Log the failure message
                Log.e("ReservationLog", "Failure: " + t.getMessage());
                Toast.makeText(Signup.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }*/
}
