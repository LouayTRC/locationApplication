package com.example.locationapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import models.Car;
import services.PictureService;
import services.PictureServiceImpl;

public class CarListAdapter extends ArrayAdapter<Car> {
    private PictureService pictureService;

    public CarListAdapter(@NonNull Context context, ArrayList<Car> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
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
        TextView listPrice = convertView.findViewById(R.id.listPrice);

        // Decode the Base64 string into a Bitmap using PictureService
        String base64Image = car.picture; // Assuming this is a Base64-encoded string
        Bitmap decodedImage = pictureService.decompressBase64ToImage(base64Image);

        if (decodedImage != null) {
            Glide.with(getContext())
                    .asBitmap() // Specify that you want to load a Bitmap
                    .load(decodedImage)
                    .into(listImage);
        } else {
            // If decoding fails, log the error
            Log.e("ListAdapter", "Failed to decode image for car: " + car.model + ", Base64 string: " + base64Image);
            // Set a default image
            listImage.setImageResource(R.drawable.car1); // Replace with your default image resource
        }

        listName.setText(car.model);
        listPrice.setText(String.valueOf(car.price));

        return convertView;
    }
}
