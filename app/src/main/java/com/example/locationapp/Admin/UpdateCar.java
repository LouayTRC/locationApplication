package com.example.locationapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.R;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import models.Category;
import models.Marque;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.CategoryService;
import services.MarqueService;

public class UpdateCar extends AppCompatActivity {

    private Car car;
    EditText modelInput, yearInput, priceInput, featuresInput, descriptionInput;
    Spinner marqueSpinner, categorySpinner;
    private List<Marque> marques;
    private List<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        modelInput = findViewById(R.id.model_name_input2);
        yearInput = findViewById(R.id.year_of_manufacture_input2);
        priceInput = findViewById(R.id.price_input2);
        featuresInput = findViewById(R.id.features_input2);
        descriptionInput = findViewById(R.id.description_input2);

        marqueSpinner = findViewById(R.id.spinner_marque2);
        categorySpinner = findViewById(R.id.spinner_category2);

        fetchMarquesAndCategories();

        // Get car id from intent
        Intent intent = getIntent();
        String id = intent.getStringExtra("car_id");

        Button updateBtn=findViewById(R.id.update_button);

        updateBtn.setOnClickListener(event-> updateCar());

        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);

        retrofit2.Call<Car> getCarCall = carService.getCarById(id);
        getCarCall.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(retrofit2.Call<Car> call, Response<Car> response) {
                if (response.isSuccessful() && response.body() != null) {
                    car = response.body();
                    Log.d("RetrofitResponse", "Response Body: " + response.body().toString());
                    displayCar(car);
                } else {
                    Toast.makeText(UpdateCar.this, "Error fetching car data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Car> call, Throwable t) {
                Toast.makeText(UpdateCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMarquesAndCategories() {
        MarqueService marqueService = RetrofitClient.getRetrofitInstance().create(MarqueService.class);
        CategoryService categoryService = RetrofitClient.getRetrofitInstance().create(CategoryService.class);

        // Fetch marques
        Call<List<Marque>> marqueCall = marqueService.getMarques();
        marqueCall.enqueue(new Callback<List<Marque>>() {
            @Override
            public void onResponse(Call<List<Marque>> call, Response<List<Marque>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    marques = response.body();
                    populateMarqueSpinner();
                } else {
                    Toast.makeText(UpdateCar.this, "Error fetching marques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marque>> call, Throwable t) {
                Toast.makeText(UpdateCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch categories
        Call<List<Category>> categoryCall = categoryService.getCategorys();
        categoryCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categories = response.body();
                    populateCategorySpinner();
                } else {
                    Toast.makeText(UpdateCar.this, "Error fetching categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(UpdateCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateMarqueSpinner() {
        if (marques != null && marques.size() > 0) {
            List<String> marqueNames = new ArrayList<>();
            marqueNames.add("Select a Marque"); // Default option for the spinner
            for (Marque marque : marques) {
                marqueNames.add(marque.name); // Add each marque name
            }

            ArrayAdapter<String> marqueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marqueNames);
            marqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            marqueSpinner.setAdapter(marqueAdapter);
        } else {
            // Handle the case where marques list is empty
            Toast.makeText(this, "No marques available", Toast.LENGTH_SHORT).show();
        }
    }

    private void populateCategorySpinner() {
        if (categories != null && categories.size() > 0) {
            List<String> categoryNames = new ArrayList<>();
            categoryNames.add("Select a Category"); // Default option for the spinner
            for (Category category : categories) {
                categoryNames.add(category.name); // Add each category name
            }

            ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
            categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoryAdapter);
        } else {
            // Handle the case where categories list is empty
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
        }
    }


    private void displayCar(Car car) {
        modelInput.setText(car.model);
        yearInput.setText(String.valueOf(car.year));
        priceInput.setText(String.valueOf(car.price));
        featuresInput.setText(car.features);
        descriptionInput.setText(car.description);

        // Select marque
        int marquePosition = getMarquePosition(car.marque._id);
        marqueSpinner.setSelection(marquePosition);

        // Select category
        int categoryPosition = getCategoryPosition(car.category._id);
        categorySpinner.setSelection(categoryPosition);
    }

    private int getMarquePosition(String marqueId) {
        for (int i = 0; i < marques.size(); i++) {
            if (marques.get(i)._id.equals(marqueId)) {
                return i + 1;
            }
        }
        return 0;
    }

    private int getCategoryPosition(String categoryId) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i)._id.equals(categoryId)) {
                return i + 1;
            }
        }
        return 0;
    }

    private boolean validateInputs() {
        if (marqueSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a marque.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (categorySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a category.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate model input
        if (modelInput.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Model name is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate year input
        String yearString = yearInput.getText().toString().trim();
        if (yearString.isEmpty()) {
            Toast.makeText(this, "Year is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        int year;
        try {
            year = Integer.parseInt(yearString);
            if (year < 1886 || year > 2024) {
                Toast.makeText(this, "Please enter a valid year.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Year must be a number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validate price input
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

        // Validate features and description inputs
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

    private void updateCar() {
        if (validateInputs()) {
            // Get selected marque and category
            Marque selectedMarque = marques.get(marqueSpinner.getSelectedItemPosition() - 1);
            Category selectedCategory = categories.get(categorySpinner.getSelectedItemPosition() - 1);

            // Update car object with new data
            car.model = modelInput.getText().toString().trim();
            car.year = Integer.parseInt(yearInput.getText().toString().trim());
            car.price = Double.parseDouble(priceInput.getText().toString().trim());
            car.features = featuresInput.getText().toString().trim();
            car.description = descriptionInput.getText().toString().trim();
            car.marque._id = selectedMarque._id;
            car.category._id = selectedCategory._id;

            // Call CarService to update car in the backend
            CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);
            retrofit2.Call<Car> updateCarCall = carService.updateCar(car._id,car);

            updateCarCall.enqueue(new Callback<Car>() {
                @Override
                public void onResponse(retrofit2.Call<Car> call, Response<Car> response) {
                    if (response.isSuccessful()) {
                        // Send back the result of the update
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("updatedCar", response.body());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(UpdateCar.this, "Error updating car", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Car> call, Throwable t) {
                    Toast.makeText(UpdateCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
