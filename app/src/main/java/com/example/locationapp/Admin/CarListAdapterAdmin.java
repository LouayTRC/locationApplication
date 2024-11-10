package com.example.locationapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.locationapp.R;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.PictureService;
import services.PictureServiceImpl;

public class CarListAdapterAdmin extends ArrayAdapter<Car> {
    private Context context;
    private List<Car> carList;
    private PictureService pictureService;

    public CarListAdapterAdmin(@NonNull Context context, ArrayList<Car> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.carList = dataArrayList;
        this.pictureService = new PictureServiceImpl(); // Initialize the PictureService
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Car car = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView listImage = convertView.findViewById(R.id.listImage);
        TextView listName = convertView.findViewById(R.id.listName);
        View buttonsLayout=convertView.findViewById(R.id.editCarLayout);
        Button updateBtn = convertView.findViewById(R.id.updateCarBtn);
        Button deleteBtn=convertView.findViewById(R.id.deleteBtn);
        TextView price=convertView.findViewById(R.id.listPrice);
        // Load the image using Glide
        String base64Image = car.picture; // Assuming the image is in Base64 format

        listName.setText(car.model);
        price.setVisibility(View.GONE);
        buttonsLayout.setVisibility(View.VISIBLE);

        updateBtn.setOnClickListener(v -> {
            // Open the UpdateCar activity
            Intent intent = new Intent(context, UpdateCar.class);
            intent.putExtra("car_id", car._id); // Pass car ID to UpdateCar activity
            ((CarListAdmin) context).startActivityForResult(intent, CarListAdmin.UPDATE_CAR_CODE);
        });
        deleteBtn.setOnClickListener(event-> deleteCar(car._id));

        if (base64Image != null) {
            Glide.with(getContext())
                    .load(pictureService.decompressBase64ToImage(base64Image)) // Decompress image
                    .into(listImage);
        }

        return convertView;
    }

    public void deleteCar(String carId) {
        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);
        Call<Void> call = carService.deleteCar(carId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Find the car in the list and remove it
                    for (int i = 0; i < carList.size(); i++) {
                        if (carList.get(i)._id.equals(carId)) {
                            carList.remove(i);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Car deleted successfully", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(context, "Failed to delete car", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
