package com.example.locationapp.Client;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.CarDetails;
import com.example.locationapp.CarListAdapter;
import com.example.locationapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import models.Marque;
import models.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.MarqueService;
import services.CategoryService;

public class CarResearch extends AppCompatActivity {

    private Spinner marqueSpinner, categorySpinner;
    private ListView carListView;
    private List<Car> carList;
    private List<Car> filteredCars;
    private CarListAdapter carAdapter;
    private TextInputEditText textInputMinPrice, textInputMaxPrice;
    private List<Marque> marques;
    private List<Category> categories;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_research);

        marqueSpinner = findViewById(R.id.spinnerMarque);
        categorySpinner = findViewById(R.id.spinnerCategory);
        carListView = findViewById(R.id.listview);
        carList = new ArrayList<>();
        filteredCars = new ArrayList<>();

        fetchMarquesAndCategories();
        fetchCars();

        textInputMinPrice = findViewById(R.id.minPrice);
        textInputMaxPrice = findViewById(R.id.maxPrice);

        textInputMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCars(); // Call the filter method on text change
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        textInputMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCars(); // Call the filter method on text change
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        marqueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCars();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCars();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        carListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = filteredCars.get(position);
                Intent intent = new Intent(CarResearch.this, CarDetails.class);
                intent.putExtra("model", selectedCar.model);
                intent.putExtra("price", selectedCar.price);
                intent.putExtra("features", selectedCar.features);
                intent.putExtra("description", selectedCar.description);
                intent.putExtra("picture", selectedCar.picture);
                intent.putExtra("_id", selectedCar._id);
                startActivity(intent);
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
                    Toast.makeText(CarResearch.this, "Error fetching marques", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marque>> call, Throwable t) {
                Toast.makeText(CarResearch.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CarResearch.this, "Error fetching categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CarResearch.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void fetchCars() {
        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);
        Call<List<Car>> call = carService.getCars();

        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList = response.body();
                    filteredCars.clear();
                    filteredCars.addAll(carList);
                    updateListView();
                } else {
                    Toast.makeText(CarResearch.this, "Error fetching cars", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarResearch.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterCars() {
        String selectedMarque = marqueSpinner.getSelectedItem() != null ? marqueSpinner.getSelectedItem().toString() : null;
        String selectedCategory = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : null;

        String minPriceStr = textInputMinPrice.getText().toString();
        String maxPriceStr = textInputMaxPrice.getText().toString();

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;

        try {
            if (!minPriceStr.isEmpty()) {
                minPrice = Double.parseDouble(minPriceStr);
            }
            if (!maxPriceStr.isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceStr);
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return; // Exit the filter method
        }

        filteredCars.clear();

        for (Car car : carList) {
            boolean matchesMarque = selectedMarque == null || selectedMarque.equals("Select a Marque") ||
                    (car.marque != null && car.marque.name.equals(selectedMarque));
            boolean matchesCategory = selectedCategory == null || selectedCategory.equals("Select a Category") ||
                    (car.category != null && car.category.name.equals(selectedCategory));
            boolean matchesPrice = car.price >= minPrice && car.price <= maxPrice;

            if (matchesMarque && matchesCategory && matchesPrice) {
                filteredCars.add(car);
            }
        }

        updateListView();
    }

    private void updateListView() {
        if (carAdapter == null) {
            carAdapter =  new CarListAdapter(this, (ArrayList<Car>) filteredCars);
            carListView.setAdapter(carAdapter);
        } else {
            carAdapter.notifyDataSetChanged();
        }
    }

}
