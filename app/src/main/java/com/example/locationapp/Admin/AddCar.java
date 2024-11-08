package com.example.locationapp.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.R;

import java.io.IOException;
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
import services.PictureService;
import services.PictureServiceImpl;

public class AddCar extends AppCompatActivity {
    private EditText modelInput, yearInput, priceInput, featuresInput, descriptionInput;
    private Button submitButton;
    private Spinner categorySpinner;
    private Spinner marqueSpinner;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Bitmap selectedBitmap; // Keep track of selected image bitmap
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
        priceInput = findViewById(R.id.price_input);
        featuresInput = findViewById(R.id.features_input);
        descriptionInput = findViewById(R.id.description_input);

        Button selectImageButton = findViewById(R.id.button_select_image);
        selectImageButton.setOnClickListener(v -> openImagePicker());

        submitButton = findViewById(R.id.submit_button);
        // Set click listener for the submit button
        submitButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveCarData();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri); // Store selected bitmap
                ImageView imageView = findViewById(R.id.image_preview);
                imageView.setImageBitmap(selectedBitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        carService.addCar(newCar).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                if (response.isSuccessful()) {
                    // Show a success message
                    Toast.makeText(AddCar.this, "Car added successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent=getIntent();
                    intent.putExtra("car",response.body());
                    setResult(RESULT_OK,intent);
                    finish(); // Optionally close the activity or redirect
                } else {
                    Toast.makeText(AddCar.this, "Failed to add car.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(AddCar.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveCarData() {
        // Retrieve input values
        String model = modelInput.getText().toString();
        int year = Integer.parseInt(yearInput.getText().toString());
        double price = Double.parseDouble(priceInput.getText().toString());
        String description = descriptionInput.getText().toString();
        String features = featuresInput.getText().toString();
        int selectedMarqueIndex = marqueSpinner.getSelectedItemPosition();
        int selectedCategoryIndex = categorySpinner.getSelectedItemPosition();

        // Ensure a valid marque and category are selected
        if (selectedMarqueIndex == 0 || selectedCategoryIndex == 0) {
            Toast.makeText(this, "Please select a marque and a category.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get selected marque and category objects
        String selectedCategory = categorySpinner.getSelectedItem().toString();
        String selectedMarque = marqueSpinner.getSelectedItem().toString();

        // Convert selected image bitmap to Base64
        PictureService pictureService = new PictureServiceImpl();
        String imageBase64 = pictureService.compressImageToBase64(selectedBitmap); // Pass the bitmap to this method

        // Create the new car request
        AddCarRequest newCar = new AddCarRequest(model, year, price, description,features, selectedMarque, selectedCategory,imageBase64);
        Log.d("Request : ",newCar.toString());
        // Send the new car data
        sendNewCar(newCar);


    }
}
