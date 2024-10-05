package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.locationapp.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ListAdapter listAdapter;
    ArrayList<ListData> dataArrayList = new ArrayList<>();
    ListData listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageList = {R.drawable.car1, R.drawable.car2, R.drawable.car3, R.drawable.car4, R.drawable.car5, R.drawable.car6, R.drawable.car7};
        int[] featureList = {R.string.car1Features, R.string.car2Features,R.string.car3Features,R.string.car4Features,R.string.car5Features, R.string.car6Features, R.string.car7Features};
        int[] descList = {R.string.car1Desc, R.string.car2Desc, R.string.car3Desc,R.string.car4Desc,R.string.car5Desc, R.string.car6Desc, R.string.car7Desc};
        String[] nameList = {"Car 1", "Car 2", "Car 3", "Car 4", "Car 5","Car 6", "Car 7"};
        String[] priceList = {"80 dt", "90 dt", "100 dt","70 dt", "150 dt", "120 dt", "170 dt"};

        for (int i = 0; i < imageList.length; i++){
            listData = new ListData(nameList[i], priceList[i], featureList[i], descList[i], imageList[i]);
            dataArrayList.add(listData);
        }
        listAdapter = new ListAdapter(MainActivity.this, dataArrayList);
        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
                intent.putExtra("name", nameList[i]);
                intent.putExtra("price", priceList[i]);
                intent.putExtra("features", featureList[i]);
                intent.putExtra("desc", descList[i]);
                intent.putExtra("image", imageList[i]);
                startActivity(intent);
            }
        });
    }
}