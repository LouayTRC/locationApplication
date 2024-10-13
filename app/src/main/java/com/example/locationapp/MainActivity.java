package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Méthode pour aller à l'activité CarList
    public void goToCarList(View view) {
        Intent intent = new Intent(MainActivity.this, CarList.class);
        startActivity(intent);
    }

    // Méthode pour aller à l'activité CarResearch
    public void goToCarResearch(View view) {
        Intent intent = new Intent(MainActivity.this, CarResearch.class);
        startActivity(intent);
    }

}
