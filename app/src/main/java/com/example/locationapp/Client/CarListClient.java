package com.example.locationapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.locationapp.Admin.CarListAdmin;
import com.example.locationapp.CarDetails;
import com.example.locationapp.CarListAdapter;
import com.example.locationapp.R;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class CarListClient extends AppCompatActivity {
    private List<Car> dataArrayList;
    private List<Car> filteredList;
    private ListView listView;
    private CarListAdapter carListAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list_client);

        listView = findViewById(R.id.listview2);
        searchView = findViewById(R.id.searchView2);
        dataArrayList = new ArrayList<>();
        filteredList = new ArrayList<>(); // Initialize filteredList here

        // Fetch cars using the CarService
        fetchCars();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Car selectedCar = filteredList.get(position);
            Intent intent = new Intent(CarListClient.this, CarDetails.class);
            intent.putExtra("model", selectedCar.model);
            intent.putExtra("price", selectedCar.price);
            intent.putExtra("features", selectedCar.features);
            intent.putExtra("description", selectedCar.description);
            intent.putExtra("picture", selectedCar.picture);
            intent.putExtra("_id", selectedCar._id);
            startActivity(intent);
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
                    carListAdapter = new CarListAdapter(CarListClient.this, (ArrayList<Car>) filteredList);
                    listView.setAdapter(carListAdapter);
                } else {
                    Toast.makeText(CarListClient.this, "Failed to fetch cars", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarListClient.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        carListAdapter.notifyDataSetChanged(); // Notify adapter about data changes
    }

}