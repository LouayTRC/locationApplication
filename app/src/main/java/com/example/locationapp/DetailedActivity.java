package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.locationapp.databinding.ActivityDetailedBinding;

public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("name");
            String price = intent.getStringExtra("price");
            int features = intent.getIntExtra("features", R.string.car1Features);
            int desc = intent.getIntExtra("desc", R.string.car1Desc);
            int image = intent.getIntExtra("image", R.drawable.car1);

            binding.detailName.setText(name);
            binding.detailPrice.setText(price);
            binding.detailDesc.setText(desc);
            binding.detailFeatures.setText(features);
            binding.detailImage.setImageResource(image);
        }
    }
}