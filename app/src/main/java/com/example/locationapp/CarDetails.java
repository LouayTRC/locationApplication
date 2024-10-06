package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.locationapp.databinding.ActivityDetailedBinding;

public class CarDetails extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("model");
            double price = intent.getDoubleExtra("price",0000);
            String features = intent.getStringExtra("features");
            String desc = intent.getStringExtra("description");
            int image = intent.getIntExtra("image", R.drawable.car1);

            binding.detailModel.setText(name);
            binding.detailPrice.setText(String.valueOf(price));
            binding.detailDescription.setText(desc);
            binding.detailFeatures.setText(features);
            binding.detailImage.setImageResource(image);
        }
    }
}