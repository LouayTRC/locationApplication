package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.locationapp.databinding.ActivityDetailedBinding;

import Config.RetrofitClient;
import models.AvailabilityRequest;
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
        String startDate = binding.dateDebut.getText().toString().trim();
        String endDate = binding.dateFin.getText().toString().trim();


        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Select dates", Toast.LENGTH_SHORT).show();
        } else if (startDate.compareTo(endDate) > 0) {
            Toast.makeText(this, "Start date cannot be after end date", Toast.LENGTH_SHORT).show();

        } else {
            AvailabilityRequest request = new AvailabilityRequest(startDate, endDate);

            CarService service = RetrofitClient.getRetrofitInstance().create(CarService.class);
            Call<Boolean> call = service.verifyCarAvailability(carId, request);

            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Boolean isAvailable = response.body();
                        if (isAvailable) {
                            Toast.makeText(CarDetails.this, "Car is available!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CarDetails.this, "Car is not available.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CarDetails.this, "Failed to check availability", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(CarDetails.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
