package com.example.locationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.util.GeoPoint;

public class FirstReservation extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private TextView startDateDisplay, endDateDisplay, latitudeDisplay, longitudeDisplay;
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private Marker currentLocationMarker; // Marker for current location
    private boolean deliveryYes; // Flag to check delivery option

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        setContentView(R.layout.activity_first_reservation);

        // Initialize TextViews
        startDateDisplay = findViewById(R.id.startDateInput);
        endDateDisplay = findViewById(R.id.endDateInput);
        latitudeDisplay = findViewById(R.id.latitudeDisplay);
        longitudeDisplay = findViewById(R.id.longitudeDisplay);
        mapView = findViewById(R.id.mapView);

        // Set date from extras
        setDateFromExtras();

        // Initialize MyLocationOverlay
        myLocationOverlay = new MyLocationNewOverlay(mapView);
        mapView.getOverlays().add(myLocationOverlay);

        // Request location permission if not granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            myLocationOverlay.enableMyLocation();
        }

        // Handle delivery options
        RadioGroup deliveryGroup = findViewById(R.id.deliveryGroup);
        deliveryGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.deliveryYes) {
                deliveryYes = true; // Set delivery flag
                showMap();
            } else {
                deliveryYes = false; // Clear delivery flag
                hideMap();
            }
        });

        // Reserve Button action
        Button reserveButton = findViewById(R.id.reserveButton);
        reserveButton.setOnClickListener(v -> goToReserveCar());

        // Button to center map on current location
        Button btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        btnCurrentLocation.setOnClickListener(v -> centerMapOnCurrentLocation());
    }

    private void setDateFromExtras() {
        Intent intent = getIntent();
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");

        startDateDisplay.setText(startDate != null ? startDate : "");
        endDateDisplay.setText(endDate != null ? endDate : "");
    }

    private void showMap() {
        findViewById(R.id.mapView).setVisibility(LinearLayout.VISIBLE);
        findViewById(R.id.longitudeLayout).setVisibility(LinearLayout.VISIBLE);
        findViewById(R.id.latitudeLayout).setVisibility(LinearLayout.VISIBLE);
        findViewById(R.id.btnCurrentLocation).setVisibility(LinearLayout.VISIBLE);
        if (myLocationOverlay.getMyLocation() != null) {
            updateLocationDisplays(myLocationOverlay.getMyLocation());
            GeoPoint geoPoint = new GeoPoint(myLocationOverlay.getMyLocation());
            mapView.getController().setZoom(15.0);
            mapView.getController().setCenter(geoPoint);
        }
    }

    private void hideMap() {
        findViewById(R.id.mapView).setVisibility(LinearLayout.GONE);
    }

    private void updateLocationDisplays(GeoPoint location) {
        latitudeDisplay.setText("Latitude: " + location.getLatitude());
        longitudeDisplay.setText("Longitude: " + location.getLongitude());

        if (currentLocationMarker == null) {
            // Initialize and add a marker for the current location
            currentLocationMarker = new Marker(mapView);
            currentLocationMarker.setTitle("Current Location");
            currentLocationMarker.setDraggable(true);
            mapView.getOverlays().add(currentLocationMarker);
        }

        // Update the marker position and the map center
        currentLocationMarker.setPosition(location);
        mapView.getController().setCenter(location);
        mapView.invalidate();

        // Listen for drag events
        currentLocationMarker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Handle marker drag start if needed
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Handle marker drag if needed
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Update the UI with the new position
                GeoPoint newPosition = marker.getPosition();
                latitudeDisplay.setText("Latitude: " + newPosition.getLatitude());
                longitudeDisplay.setText("Longitude: " + newPosition.getLongitude());
            }
        });
    }

    private void centerMapOnCurrentLocation() {
        if (myLocationOverlay.getMyLocation() != null) {
            GeoPoint location = myLocationOverlay.getMyLocation();
            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            mapView.getController().animateTo(geoPoint);
            updateLocationDisplays(location);
        } else {
            Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToReserveCar() {
        String startDate = startDateDisplay.getText().toString();
        String endDate = endDateDisplay.getText().toString();
        boolean driverYes = true; // Assume driver is required or retrieved from your logic
        Double deliveryLongitude = null;
        Double deliveryLatitude = null;

        // Check if delivery is enabled to pass location or null values
        if (deliveryYes) {
            GeoPoint myLocation = myLocationOverlay.getMyLocation();
            if (myLocation != null) {
                deliveryLongitude = myLocation.getLongitude();
                deliveryLatitude = myLocation.getLatitude();
            }
        }

        Intent intent = new Intent(this, ReserveCar.class);
        // Put the extras in the intent
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra("driverYes", driverYes); // Pass the driver boolean value
        // Pass the longitude and latitude values
        intent.putExtra("deliveryLongitude", deliveryLongitude);
        intent.putExtra("deliveryLatitude", deliveryLatitude);

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        myLocationOverlay.enableMyLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLocationOverlay.disableMyLocation();
        mapView.onDetach();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                myLocationOverlay.enableMyLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
