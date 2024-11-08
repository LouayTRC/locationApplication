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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.locationapp.R;
import com.example.locationapp.UpdateCar;

import java.util.ArrayList;
import java.util.List;

import models.Car;
import services.PictureService;
import services.PictureServiceImpl;

public class CarListAdapterAdmin extends ArrayAdapter<Car> {
    private Context context;
    private OnCarUpdatedListener listener;
    private List<Car> carList;
    private PictureService pictureService;

    public CarListAdapterAdmin(@NonNull Context context, ArrayList<Car> dataArrayList, OnCarUpdatedListener listener) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.listener = listener;
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

        if (base64Image != null) {
            Glide.with(getContext())
                    .load(pictureService.decompressBase64ToImage(base64Image)) // Decompress image
                    .into(listImage);
        }

        return convertView;
    }

    // Method to update car and notify listener
    public void updateCar(int position, Car updatedCar) {
        if (listener != null) {
            listener.onUpdateCar(position, updatedCar); // Notify the listener (CarListAdmin)
        }
    }

    // Interface for communicating car updates
    public interface OnCarUpdatedListener {
        void onUpdateCar(int position, Car car);
    }
}
