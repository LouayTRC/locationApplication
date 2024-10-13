package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.locationapp.databinding.ActivityDetailedBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Config.RetrofitClient;
import models.Requests.AvailabilityRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class CarDetails extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (intent != null) {
            String carId = intent.getStringExtra("_id");
            binding.detailModel.setText(intent.getStringExtra("model"));
            binding.detailPrice.setText(String.valueOf(intent.getDoubleExtra("price", 0)));
            binding.detailDescription.setText(intent.getStringExtra("description"));
            binding.detailFeatures.setText(intent.getStringExtra("features"));
            binding.detailImage.setImageResource(intent.getIntExtra("image", R.drawable.car1));
            binding.checkAvailabilityButton.setOnClickListener(v -> checkAvailability(carId));
        }

    }


    private void checkAvailability(String carId) {
        String startDateStr = binding.dateDebut.getText().toString().trim();
        String endDateStr = binding.dateFin.getText().toString().trim();

        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.makeText(this, "Select dates", Toast.LENGTH_SHORT).show();
            return;
        }


        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            assert startDate != null;
            if (startDate.compareTo(endDate) > 0) {
                Toast.makeText(this, "Start date cannot be after End date", Toast.LENGTH_SHORT).show();
                return;
            }


            AvailabilityRequest request = new AvailabilityRequest(startDateStr, endDateStr);


            CarService service = RetrofitClient.getRetrofitInstance().create(CarService.class);
            Call<Boolean> call = service.verifyCarAvailability(carId, request);


            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Boolean isAvailable = response.body();
                        if (isAvailable) {
                            Toast.makeText(CarDetails.this, "Car is available !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CarDetails.this, "Car is not available.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CarDetails.this, "Failed to check availability", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(CarDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (ParseException e) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd", Toast.LENGTH_SHORT).show();
        }
    }


    public void goToReserveCar(View view) {
        Intent intent = new Intent(CarDetails.this, FirstReservation.class);
        startActivity(intent);
    }

}
