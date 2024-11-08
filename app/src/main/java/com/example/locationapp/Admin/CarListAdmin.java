package com.example.locationapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.locationapp.CarDetails;
import com.example.locationapp.R;
import com.example.locationapp.Admin.CarListAdapterAdmin;
import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class CarListAdmin extends AppCompatActivity implements CarListAdapterAdmin.OnCarUpdatedListener {

    public static final int ADDCAR_CODE = 1;
    public static final int UPDATE_CAR_CODE = 2;

    private List<Car> dataArrayList;
    private List<Car> filteredList;
    private ListView listView;
    private CarListAdapterAdmin carListAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_list_cars_activity);

        listView = findViewById(R.id.listview);
        searchView = findViewById(R.id.searchView);
        dataArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Fetch cars using CarService
        fetchCars();

        // ListView item click listener
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Car selectedCar = filteredList.get(position);
            Intent intent = new Intent(CarListAdmin.this, CarDetails.class);
            intent.putExtra("model", selectedCar.model);
            intent.putExtra("price", selectedCar.price);
            intent.putExtra("features", selectedCar.features);
            intent.putExtra("description", selectedCar.description);
            intent.putExtra("picture", selectedCar.picture);
            intent.putExtra("_id", selectedCar._id);
            startActivity(intent);
        });

        // Search functionality
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
                    filteredList.clear();
                    filteredList.addAll(dataArrayList);
                    carListAdapter = new CarListAdapterAdmin(CarListAdmin.this, (ArrayList<Car>) filteredList, CarListAdmin.this);
                    listView.setAdapter(carListAdapter);
                } else {
                    Toast.makeText(CarListAdmin.this, "Failed to fetch cars", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarListAdmin.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToAddCar(View view) {
        Intent intent = new Intent(CarListAdmin.this, AddCar.class);
        startActivityForResult(intent, ADDCAR_CODE);
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
        carListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDCAR_CODE && resultCode == RESULT_OK) {
            Car newCar = data.getParcelableExtra("car");
            carListAdapter.add(newCar);
        } else if (requestCode == UPDATE_CAR_CODE && resultCode == RESULT_OK) {
            Car updatedCar = data.getParcelableExtra("car");
            if (updatedCar != null) {
                updateCarInList(updatedCar);
            }
        }
    }

    private void updateCarInList(Car updatedCar) {
        for (int i = 0; i < filteredList.size(); i++) {
            if (filteredList.get(i)._id.equals(updatedCar._id)) {
                filteredList.set(i, updatedCar);
                break;
            }
        }
        carListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUpdateCar(int position, Car car) {
        updateCarInList(car);
    }
}
