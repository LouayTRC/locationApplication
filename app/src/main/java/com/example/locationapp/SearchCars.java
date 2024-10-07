package com.example.locationapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Category;
import models.Marque;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CategoryService;
import services.MarqueService;

public class SearchCars extends AppCompatActivity {

    private SearchView searchView;
    private Spinner filterSpinner1, filterSpinner2;
    private List<Category> categoryList;
    private List<Marque> MarqueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cars);

        searchView = findViewById(R.id.searchView);
        filterSpinner1 = findViewById(R.id.filterSpinner1); // Spinner for categories
        filterSpinner2 = findViewById(R.id.filterSpinner2); // Spinner for car types

        // Initialize lists
        categoryList = new ArrayList<>();
        MarqueList = new ArrayList<>();

        // Fetch data for categories and car types
        fetchCategories();
        fetchMarques();
    }

    private void fetchCategories() {
        CategoryService categoryService = RetrofitClient.getRetrofitInstance().create(CategoryService.class);
        Call<List<Category>> call = categoryService.getCategorys();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.addAll(response.body());
                    setupCategorySpinner();
                } else {
                    Toast.makeText(SearchCars.this, "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(SearchCars.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMarques() {
        MarqueService MarqueService = RetrofitClient.getRetrofitInstance().create(MarqueService.class);
        Call<List<Marque>> call = MarqueService.getMarques();

        call.enqueue(new Callback<List<Marque>>() {
            @Override
            public void onResponse(Call<List<Marque>> call, Response<List<Marque>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MarqueList.addAll(response.body());
                    setupMarqueSpinner();
                } else {
                    Toast.makeText(SearchCars.this, "Failed to fetch car types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Marque>> call, Throwable t) {
                Toast.makeText(SearchCars.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategorySpinner() {
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categoryList) {
            categoryNames.add(category.name); // Assuming Category has a method getName()
        }

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryNames);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner1.setAdapter(categoryAdapter);
    }

    private void setupMarqueSpinner() {
        List<String> MarqueNames = new ArrayList<>();
        for (Marque Marque : MarqueList) {
            MarqueNames.add(Marque.name); // Assuming Marque has a method getName()
        }

        ArrayAdapter<String> MarqueAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MarqueNames);
        MarqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner2.setAdapter(MarqueAdapter);
    }
}
