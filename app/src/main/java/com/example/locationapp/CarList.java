package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

    private ArrayList<Car> dataArrayList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_cars_activity);

        listView = findViewById(R.id.listview);
        dataArrayList = new ArrayList<>();

        // Fetch cars using the CarService
        fetchCars();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Car selectedCar = dataArrayList.get(position);
                Intent intent = new Intent(CarList.this, CarDetails.class);
                intent.putExtra("model", selectedCar.model);
                intent.putExtra("price", selectedCar.price);
                intent.putExtra("features", selectedCar.features);
                intent.putExtra("description", selectedCar.description);
                intent.putExtra("picture", selectedCar.picture);
                intent.putExtra("_id",selectedCar._id);
                startActivity(intent);
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
                    ListAdapter listAdapter = new ListAdapter(CarList.this, dataArrayList);
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
}
