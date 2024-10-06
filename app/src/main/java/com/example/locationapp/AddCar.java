package com.example.locationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;



import models.Car;

public class AddCar extends AppCompatActivity {
    private EditText modelInput, yearInput, priceInput, featuresInput, descriptionInput, pictureInput;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        // Initialize the form fields
        modelInput = findViewById(R.id.model_name_input);
        yearInput = findViewById(R.id.year_of_manufacture_input);
        priceInput = findViewById(R.id.price_per_day_input);
        featuresInput = findViewById(R.id.transmission_type_input);  // Use corresponding IDs
        descriptionInput = findViewById(R.id.car_type_input);
        pictureInput = findViewById(R.id.insurance_options_input);  // Placeholder for picture, replace with actual implementation

        submitButton = findViewById(R.id.submit_button);

        // Set click listener for the submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCarData();
            }
        });
    }

    private void saveCarData() {
        // Get data from form fields
        String model = modelInput.getText().toString();
        Integer year = Integer.parseInt(yearInput.getText().toString());
        double price = Double.parseDouble(priceInput.getText().toString());
        String features = featuresInput.getText().toString();
        String description = descriptionInput.getText().toString();
        String picture = pictureInput.getText().toString();  // Update this with actual image path or URI

        // Create a new Car object with the form data
        Car newCar = new Car(model, year, price, features, description, picture);


    }

}
