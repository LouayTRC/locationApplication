package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstReservation extends AppCompatActivity {

    private RadioGroup deliveryGroup;
    private LinearLayout regionLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_reservation);  // Adjust if your layout file name is different

        // Initialize the region layout and delivery group
        regionLayout = findViewById(R.id.regionLayout);
        deliveryGroup = findViewById(R.id.deliveryGroup);

        // Set a listener on the radio group
        deliveryGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Check which radio button was clicked
                if (checkedId == R.id.deliveryYes) {
                    // Show the region layout if "Yes" is selected
                    regionLayout.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.deliveryNo) {
                    // Hide the region layout if "No" is selected
                    regionLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    public void goToReserveCar(View view) {
        Intent intent = new Intent(FirstReservation.this, ReserveCar.class);

        // Ajouter les détails de la voiture dans l'intent
        intent.putExtra("carId", getIntent().getStringExtra("_id"));
        intent.putExtra("model", getIntent().getStringExtra("model"));
        intent.putExtra("price", getIntent().getDoubleExtra("price", 0));
        intent.putExtra("features", getIntent().getStringExtra("features"));
        intent.putExtra("description", getIntent().getStringExtra("description"));
        intent.putExtra("picture", getIntent().getStringExtra("picture"));
        intent.putExtra("category", getIntent().getStringExtra("category"));  // Transmettre la catégorie
        intent.putExtra("marque", getIntent().getStringExtra("marque"));      // Transmettre la marque

        // Lancer l'activité ReserveCar avec les données
        startActivity(intent);
    }
}