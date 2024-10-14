package com.example.locationapp;

import models.Client; // Ensure this import is present
import models.Car; // Ensure this import is present
import models.Reservation; // Ensure this import is present
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList; // Make sure to import ArrayList
import java.util.Date;

public class ReservationList extends AppCompatActivity {

    private RecyclerView rvReservationList;
    private ReservationAdapter reservationAdapter;
    private ArrayList<Reservation> reservationList; // Change to ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_reservations_activity);

        rvReservationList = findViewById(R.id.rvReservationList);
        rvReservationList.setLayoutManager(new LinearLayoutManager(this));

        // Initialize reservation list (mock data or fetch from database)
        reservationList = new ArrayList<>(); // Initialize as ArrayList

        // Add some mock data for now
       /* reservationList.add(new Reservation("1", new Client("1", "Foulen Fouleni"),
                new Car("Car Name", 2024, 100.0, "Features", "Description", "picture.png", null, null),
                new Date(), new Date(), 1));

        reservationList.add(new Reservation("2", new Client("2", "John Doe"),
                new Car("Another Car", 2023, 150.0, "More Features", "Another Description", "another_picture.png", null, null),
                new Date(), new Date(), 1));

        // Pass context and reservationList to the adapter
        reservationAdapter = new ReservationAdapter(this, reservationList); // Pass 'this' as context
        rvReservationList.setAdapter(reservationAdapter);*/
    }
}
