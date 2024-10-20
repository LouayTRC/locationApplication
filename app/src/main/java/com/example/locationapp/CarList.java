package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class CarList extends AppCompatActivity {

    private List<Car> dataArrayList;
    private List<Car> filteredList;
    private ListView listView;
    private ListAdapter listAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cars_activity);

        listView = findViewById(R.id.listview);
        searchView = findViewById(R.id.searchView);
        dataArrayList = new ArrayList<>();
        filteredList = new ArrayList<>(); // Initialize filteredList here

        // Fetch cars using the CarService
        fetchCars();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = filteredList.get(position);
                Intent intent = new Intent(CarList.this, CarDetails.class);
                intent.putExtra("model", selectedCar.model);
                intent.putExtra("price", selectedCar.price);
                intent.putExtra("features", selectedCar.features);
                intent.putExtra("description", selectedCar.description);
                intent.putExtra("picture", selectedCar.picture);
                intent.putExtra("_id", selectedCar._id);
                startActivity(intent);
            }
        });

        // Set up search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void fetchCars() {
        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);
        Call<List<Car>> call = carService.getCars();

        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataArrayList.addAll(response.body());
                    filteredList.clear(); // Clear filtered list before adding data
                    filteredList.addAll(dataArrayList); // Initialize filtered list
                    listAdapter = new ListAdapter(CarList.this, (ArrayList<Car>) filteredList);
                    listView.setAdapter(listAdapter);
                } else {
                    Toast.makeText(CarList.this, "Failed to fetch cars", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarList.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToAddCar(View view){

        Intent intent = new Intent(CarList.this, AddCar.class);
        startActivity(intent);
    }

    private void filter(String text) {
        filteredList.clear();
        if (text.isEmpty()) {
            filteredList.addAll(dataArrayList);
        } else {
            for (Car car : dataArrayList) {
                if (car.model.toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(car);
                }
            }
        }
        listAdapter.notifyDataSetChanged(); // Notify adapter about data changes
    }
}
