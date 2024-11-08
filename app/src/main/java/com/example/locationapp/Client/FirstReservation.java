package com.example.locationapp.Client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.locationapp.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.osmdroid.util.GeoPoint;

import services.PictureService;
import services.PictureServiceImpl;

public class FirstReservation extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private TextView startDateDisplay, endDateDisplay, latitudeDisplay, longitudeDisplay;
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private Marker currentLocationMarker; // Marker for current location
    private boolean deliveryYes;
    private boolean driverYes;// Flag to check delivery option
    private LinearLayout locationView;

    private TextView carModel;
    private ImageView image;
    private GeoPoint startingLocation;
    private GeoPoint latestSelectedLocation;


    @SuppressLint("ClickableViewAccessibility")
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
        locationView=findViewById(R.id.locationlayout);
        locationView.setVisibility(View.GONE);
        mapView = findViewById(R.id.mapView);


        getAndDisplayCar();

        // Initialize Zoom Buttons
        Button zoomInButton = findViewById(R.id.zoomInButton);
        Button zoomOutButton = findViewById(R.id.zoomOutButton);

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
            deliveryYes = checkedId == R.id.deliveryYes; // Set delivery flag
            if (deliveryYes) {
                showMap();
            } else {
                hideMap();
            }
        });

        RadioGroup driverGroup = findViewById(R.id.driverGroup);
        driverGroup.setOnCheckedChangeListener((group, checkedId) -> {
            driverYes = checkedId == R.id.driverYes; // Set driver flag
        });

        // Set touch listener on the map to change location
        mapView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    GeoPoint newLocation = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                    updateLocation(newLocation);
                }
                return true; // Return true to indicate the touch event was handled
            }
        });

        Button updateLocationButton = findViewById(R.id.btnCurrentLocation);
        updateLocationButton.setOnClickListener(v -> {
            if (startingLocation != null) {
                updateLocation(startingLocation);
                Toast.makeText(this, "Returning to the starting location", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Starting location not set", Toast.LENGTH_SHORT).show();
            }
        });
        // Zoom in and out button listeners
        zoomInButton.setOnClickListener(v -> zoomIn());
        zoomOutButton.setOnClickListener(v -> zoomOut());

        Button submitButton = findViewById(R.id.reserveButton);
        submitButton.setOnClickListener(v -> {
            goToReserveCar();
        });
    }
    private void getAndDisplayCar(){
        carModel=findViewById(R.id.carName);
        Intent intent=getIntent();
        carModel.setText(intent.getStringExtra("model"));

        image=findViewById(R.id.carImage);
        String encodedImage = intent.getStringExtra("picture");
        if (encodedImage != null) {
            setImageFromEncodedString(encodedImage);
        } else {
            image.setImageResource(R.drawable.car1); // Default image
        }

    }
    private void setImageFromEncodedString(String encodedImage) {
        try {
            PictureService pictureService= new PictureServiceImpl();
            Bitmap decodedBitmap = pictureService.decompressBase64ToImage(encodedImage);
            image.setImageBitmap(decodedBitmap);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error decoding image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void setDateFromExtras() {
        Intent intent = getIntent();
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");

        startDateDisplay.setText(startDate != null ? startDate : "");
        endDateDisplay.setText(endDate != null ? endDate : "");
    }

    private void showMap() {
        locationView.setVisibility(LinearLayout.VISIBLE);

        if (myLocationOverlay.getMyLocation() != null) {
            updateLocationDisplays(myLocationOverlay.getMyLocation());
            GeoPoint geoPoint = new GeoPoint(myLocationOverlay.getMyLocation());
            if (startingLocation == null) {
                startingLocation = geoPoint;
                latestSelectedLocation=geoPoint;
            }
            mapView.getController().setZoom(15.0);
            mapView.getController().setCenter(geoPoint);
        }
    }

    private void hideMap() {
        locationView.setVisibility(LinearLayout.GONE);
    }

    private void updateLocationDisplays(GeoPoint location) {
        latitudeDisplay.setText(String .valueOf(location.getLatitude()));
        longitudeDisplay.setText(String .valueOf(location.getLongitude()));

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
    }

    private void updateLocation(GeoPoint newLocation) {
        if (currentLocationMarker != null) {
            currentLocationMarker.setPosition(newLocation);
            updateLocationDisplays(newLocation);
            mapView.invalidate();
            latestSelectedLocation = newLocation;
        }
    }

    // Method to zoom in
    private void zoomIn() {
        double currentZoom = mapView.getZoomLevelDouble();
        mapView.getController().setZoom(currentZoom + 1);
    }

    // Method to zoom out
    private void zoomOut() {
        double currentZoom = mapView.getZoomLevelDouble();
        mapView.getController().setZoom(currentZoom - 1);
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

    private void goToReserveCar() {
        String startDate = startDateDisplay.getText().toString();
        String endDate = endDateDisplay.getText().toString(); // Assume driver is required or retrieved from your logic
        Double deliveryLongitude = null;
        Double deliveryLatitude = null;

        // Check if delivery is enabled to pass location or null values
        if (this.deliveryYes) {
            deliveryLongitude = latestSelectedLocation.getLongitude();
            deliveryLatitude = latestSelectedLocation.getLatitude();
        }
        Intent oldIntent=getIntent();

        Intent intent = new Intent(this, ReserveCar.class);
        // Put the extras in the intent
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putExtra("driverYes", driverYes); // Pass the driver boolean value
        // Pass the longitude and latitude values
        intent.putExtra("deliveryLongitude", deliveryLongitude);
        intent.putExtra("deliveryLatitude", deliveryLatitude);
        intent.putExtra("carId",oldIntent.getStringExtra("carId"));
        startActivity(intent);
    }
}
