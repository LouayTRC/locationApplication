package com.example.locationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import models.Category;
import models.Marque;
import models.Requests.AddCarRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.CategoryService;
import services.MarqueService;

public class AddCar extends AppCompatActivity {
    private EditText modelInput, yearInput, priceInput, featuresInput, descriptionInput;
    private Button submitButton;
    private Spinner categorySpinner;
    private Spinner marqueSpinner;

    private List<Marque> marques;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        marqueSpinner = findViewById(R.id.spinner_marque);
        categorySpinner = findViewById(R.id.spinner_category);

        fetchMarquesAndCategories();

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

    private void fetchMarquesAndCategories() {
        MarqueService marqueService = RetrofitClient.getRetrofitInstance().create(MarqueService.class);
        CategoryService categoryService = RetrofitClient.getRetrofitInstance().create(CategoryService.class);

        Call<List<Marque>> marqueCall = marqueService.getMarques();
        marqueCall.enqueue(new Callback<List<Marque>>() {
            @Override
            public void onResponse(Call<List<Marque>> call, Response<List<Marque>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    marques = response.body();
                    populateMarqueSpinner();
                } else {
                    Toast.makeText(AddCar.this, "Error fetching marques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marque>> call, Throwable t) {
                Toast.makeText(AddCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Call<List<Category>> categoryCall = categoryService.getCategorys();
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    populateCategorySpinner();
                } else {
                    Toast.makeText(AddCar.this, "Error fetching categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(AddCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateMarqueSpinner() {
        List<String> marqueNames = new ArrayList<>();
        marqueNames.add("Select a Marque");
        for (Marque marque : marques) {
            marqueNames.add(marque.name);
        }

        ArrayAdapter<String> marqueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marqueNames);
        marqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marqueSpinner.setAdapter(marqueAdapter);
    }

    private void populateCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        categoryNames.add("Select a Category");
        for (Category category : categories) {
            categoryNames.add(category.name);
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
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

        return true;
    }

    private void sendNewCar(AddCarRequest newCar) {
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

    private void saveCarData() {
        // Get data from form fields
        String model = modelInput.getText().toString();
        Integer year = Integer.parseInt(yearInput.getText().toString());
        double price = Double.parseDouble(priceInput.getText().toString());
        String features = featuresInput.getText().toString();
        String description = descriptionInput.getText().toString();

        // Get the selected category and marque names from the spinners
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        String selectedMarque = marqueSpinner.getSelectedItem().toString();

        // Create a new Car object with the form data and selected category and marque
        AddCarRequest newCar = new AddCarRequest(model, year, price, features, description, selectedMarque,selectedCategory);

        // Call the method to send the new car data to the server
        sendNewCar(newCar);
    }

}
