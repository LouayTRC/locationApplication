package com.example.locationapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class AddCar extends AppCompatActivity {
    private static final int IMAGE_PICK_REQUEST = 1;
    private ImageView imagePreview;
    private String imageBase64; // Holds the base64 encoded image
    private EditText modelInput, yearInput, priceInput, featuresInput, descriptionInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        Button selectImageButton = findViewById(R.id.button_select_image);
        imagePreview = findViewById(R.id.image_preview); // Initialize the ImageView

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(); // Call method to select image
            }
        });

        // Initialize the form fields
        modelInput = findViewById(R.id.model_name_input);
        yearInput = findViewById(R.id.year_of_manufacture_input);
        priceInput = findViewById(R.id.price_input); // Updated ID
        featuresInput = findViewById(R.id.features_input);  // Updated ID
        descriptionInput = findViewById(R.id.description_input); // Updated ID

        submitButton = findViewById(R.id.submit_button);

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    saveCarData();
                }
            }
        });
    }

    private boolean validateInputs() {
        // Check if model is empty
        if (modelInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Model name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if year is valid
        String yearString = yearInput.getText().toString().trim();
        if (yearString.isEmpty()) {
            Toast.makeText(this, "Year is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        int year;
        try {
            year = Integer.parseInt(yearString);
            if (year < 1886 || year > 2024) { // The first car was invented in 1886
                Toast.makeText(this, "Please enter a valid year.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Year must be a number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if price is valid
        String priceString = priceInput.getText().toString().trim();
        if (priceString.isEmpty()) {
            Toast.makeText(this, "Price is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        double price;
        try {
            price = Double.parseDouble(priceString);
            if (price <= 0) {
                Toast.makeText(this, "Price must be greater than 0.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Price must be a number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if features and description are empty
        if (featuresInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Features are required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (descriptionInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Description is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if an image is selected
        if (imageBase64 == null || imageBase64.isEmpty()) {
            Toast.makeText(this, "Please select an image.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void sendNewCar(Car newCar) {
        // Create an instance of the Retrofit service
        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);

        // Make the network request to add the car
        carService.addCar(newCar).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Show a success message
                    Toast.makeText(AddCar.this, "Car added successfully!", Toast.LENGTH_SHORT).show();
                    finish(); // Optionally close the activity or clear fields
                } else {
                    // Show an error message if the response wasn't successful
                    Toast.makeText(AddCar.this, "Failed to add car.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle errors (like network failure)
                Toast.makeText(AddCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                // Convert the selected image to a Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                // Convert the Bitmap to a Base64 string
                imageBase64 = encodeImageToBase64(bitmap);

                // Set the Bitmap to the ImageView
                imagePreview.setImageBitmap(bitmap); // Display the selected image
                Toast.makeText(this, "Image selected and encoded!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace(); // Log the error for debugging
                Toast.makeText(this, "Failed to process the image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void saveCarData() {
        // Get data from form fields
        String model = modelInput.getText().toString();
        Integer year = Integer.parseInt(yearInput.getText().toString());
        double price = Double.parseDouble(priceInput.getText().toString());
        String features = featuresInput.getText().toString();
        String description = descriptionInput.getText().toString();

        // Use the encoded image for the picture field
        String picture = imageBase64;

        // Create a new Car object with the form data
        Car newCar = new Car(model, year, price, features, description, picture);

        sendNewCar(newCar);
    }
}
