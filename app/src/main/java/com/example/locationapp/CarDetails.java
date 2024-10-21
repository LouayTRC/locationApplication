package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.locationapp.databinding.ActivityDetailedBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Config.RetrofitClient;
import models.Requests.AvailabilityRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.PictureServiceImpl;

public class CarDetails extends AppCompatActivity {

    private ActivityDetailedBinding binding;
    private PictureServiceImpl pictureService;
    private String startDateStr;
    private String endDateStr;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pictureService = new PictureServiceImpl();

        intent = getIntent();
        if (intent != null) {
            String carId = intent.getStringExtra("_id");
            binding.detailModel.setText(intent.getStringExtra("model"));
            binding.detailPrice.setText(String.valueOf(intent.getDoubleExtra("price", 0)));
            binding.detailDescription.setText(intent.getStringExtra("description"));
            binding.detailFeatures.setText(intent.getStringExtra("features"));

            String encodedImage = intent.getStringExtra("picture");
            if (encodedImage != null) {
                setImageFromEncodedString(encodedImage);
            } else {
                binding.detailImage.setImageResource(R.drawable.car1); // Default image
            }

            binding.startDate.setOnClickListener(v -> showDatePickerDialog(true));
            binding.endDate.setOnClickListener(v -> showDatePickerDialog(false));
            binding.reserveButton.setOnClickListener(v -> goToReserveCar(carId));
        }
    }

    private void setImageFromEncodedString(String encodedImage) {
        try {
            Bitmap decodedBitmap = pictureService.decompressBase64ToImage(encodedImage);
            binding.detailImage.setImageBitmap(decodedBitmap);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error decoding image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog(boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
            if (isStartDate) {
                startDateStr = selectedDate;
                binding.startDate.setText(selectedDate);
            } else {
                endDateStr = selectedDate;
                binding.endDate.setText(selectedDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void goToReserveCar(String carId) {
        if (startDateStr == null || endDateStr == null) {
            Toast.makeText(this, "Select dates", Toast.LENGTH_SHORT).show();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);

            if (startDate != null && startDate.after(endDate)) {
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
                            Intent newIntent = new Intent(CarDetails.this, FirstReservation.class);
                            newIntent.putExtra("startDate", startDateStr);  // Pass start date as extra
                            newIntent.putExtra("endDate", endDateStr);
                            newIntent.putExtra("carId",carId);
                            newIntent.putExtra("model",intent.getStringExtra("model"));
                            newIntent.putExtra("picture",intent.getStringExtra("picture"));
                            // Pass end date as extra
                            startActivity(newIntent);
                        } else {
                            String message = "Car is not available";
                            Toast.makeText(CarDetails.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CarDetails.this, "Error checking availability", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                    Toast.makeText(CarDetails.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Date parsing error", Toast.LENGTH_SHORT).show();
        }
    }



}
