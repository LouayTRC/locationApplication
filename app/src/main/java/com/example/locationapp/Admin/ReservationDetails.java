package com.example.locationapp.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.locationapp.R;

import java.text.SimpleDateFormat;

import Config.RetrofitClient;
import models.Reservation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ReservationService;

public class ReservationDetails extends AppCompatActivity {

    private TextView reservedByTextView, carModelTextView, reservationDatesTextView, driverOptionTextView,
            livraisonOptionTextView, livraisonDistanceTextView, statusTextView, totalPaymentTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reservation_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize XML components
        reservedByTextView = findViewById(R.id.reservedByTextView);
        carModelTextView = findViewById(R.id.carModelTextView);
        reservationDatesTextView = findViewById(R.id.reservationDatesTextView);
        driverOptionTextView = findViewById(R.id.driverOptionTextView);
        livraisonOptionTextView = findViewById(R.id.livraisonOptionTextView);
        livraisonDistanceTextView = findViewById(R.id.livraisonDistanceTextView);
        statusTextView = findViewById(R.id.statusTextView);
        totalPaymentTextView = findViewById(R.id.totalPaymentTextView);




        // Get the reservation ID from the Intent extras
        String reservationId = getIntent().getStringExtra("reservationId");
        Log.d("id2",reservationId);
        fetchReservationDetails(reservationId);

    }

    private void fetchReservationDetails(String reservationId) {
        // Initialize the Retrofit service
        ReservationService reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);

        // Make the API call to fetch reservation details
        Call<Reservation> call = reservationService.getReservationById(reservationId);
        call.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUIWithReservationData(response.body());
                } else {
                    Toast.makeText(ReservationDetails.this, "Failed to load reservation details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                Log.e("ReservationDetails", "Error fetching reservation data", t);
                Toast.makeText(ReservationDetails.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUIWithReservationData(Reservation reservation) {
        String displayedstatus;
        if (reservation.status==0){
            displayedstatus="Pending";
        } else if (reservation.status==1) {
            displayedstatus="Accepted";
        }
        else {
            displayedstatus="Refused";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedStartDate = dateFormat.format(reservation.dateStart);
        String formattedEndDate = dateFormat.format(reservation.dateEnd);
        double prixSupplementaire = reservation.distanceEnKm * 10;



        double prixDSupplementaire=reservation.diffDays*50;
        reservedByTextView.setText(reservation.client.user.name);
        carModelTextView.setText(reservation.car.model);
        reservationDatesTextView.setText(formattedStartDate + " - " + formattedEndDate);
        driverOptionTextView.setText(reservation.driver!=null ? "Yes" : "No");



        livraisonOptionTextView.setText(reservation.locationLat != null && reservation.locationLong != null ? "Yes" : "No");
        TextView livraisonPriceTextView = findViewById(R.id.livraisonPriceTextView);
        TextView driverPriceTextView=findViewById(R.id.driverPriceTextView);

        if (reservation.locationLat != null && reservation.locationLong != null ) {

            livraisonPriceTextView.setVisibility(View.VISIBLE); // Afficher le prix
            String prixFormate = String.format("%.2f TND", prixSupplementaire);
            livraisonPriceTextView.setText(prixFormate);
        } else {
            livraisonPriceTextView.setVisibility(View.GONE); // Masquer si ce n'est pas "Yes"
        }
        if(reservation.driver!=null){
            driverPriceTextView.setVisibility(View.VISIBLE);
            driverPriceTextView.setText(String.valueOf(prixDSupplementaire + " TND"));
        }
        else {driverPriceTextView.setVisibility(View.GONE);}



        livraisonDistanceTextView.setText(reservation.distanceEnKm!=0 ? String.valueOf(reservation.distanceEnKm)+ " KM" : "No Delivery requested");
        statusTextView.setText(displayedstatus);
        totalPaymentTextView.setText(String.valueOf(reservation.total + " TND"));

    }
}
