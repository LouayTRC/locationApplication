package com.example.locationapp.Client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.locationapp.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Config.RetrofitClient;
import models.Driver;
import models.Location;
import models.Requests.AvailabilityRequest;
import models.Requests.ReserveRequest;
import models.Reservation;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;
import services.DriverService;
import services.PictureService;
import services.PictureServiceImpl;
import services.ReservationService;

public class FirstReservation extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private TextView startDateDisplay, endDateDisplay, latitudeDisplay, longitudeDisplay;
    private MapView mapView;
    private MyLocationNewOverlay myLocationOverlay;
    private Marker currentLocationMarker;
    private boolean deliveryYes;
    private boolean driverYes;
    private LinearLayout locationView;
    private Spinner driverSpinner;
    private List<Driver> availableDrivers;
    private String startDate = "", endDate = "";
    private TextView carModel;
    private ImageView image;
    private GeoPoint startingLocation;
    private GeoPoint latestSelectedLocation;
    private User connectedUser;
    private String token;
    private LinearLayout deliveryLayout;  // Add deliveryLayout here to adjust dynamically

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        setContentView(R.layout.activity_first_reservation);

        startDateDisplay = findViewById(R.id.startDateInput);
        endDateDisplay = findViewById(R.id.endDateInput);
        latitudeDisplay = findViewById(R.id.latitudeDisplay);
        longitudeDisplay = findViewById(R.id.longitudeDisplay);
        locationView = findViewById(R.id.locationlayout);
        locationView.setVisibility(View.GONE);
        mapView = findViewById(R.id.mapView);
        driverSpinner = findViewById(R.id.driverSpinner);
        deliveryLayout = findViewById(R.id.deliveryLayout);  // Initialize deliveryLayout here

        getAvailableDrivers();
        getAndDisplayCar();

        Button zoomInButton = findViewById(R.id.zoomInButton);
        Button zoomOutButton = findViewById(R.id.zoomOutButton);

        setDateFromExtras();

        myLocationOverlay = new MyLocationNewOverlay(mapView);
        mapView.getOverlays().add(myLocationOverlay);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            myLocationOverlay.enableMyLocation();
        }

        RadioGroup deliveryGroup = findViewById(R.id.deliveryGroup);
        deliveryGroup.setOnCheckedChangeListener((group, checkedId) -> {
            deliveryYes = checkedId == R.id.deliveryYes;
            if (deliveryYes) {
                showMap();
            } else {
                hideMap();
            }
        });

        RadioGroup driverGroup = findViewById(R.id.driverGroup);
        driverGroup.setOnCheckedChangeListener((group, checkedId) -> {
            driverYes = checkedId == R.id.driverYes;

            if (driverYes) {
                // Show the driver select layout
                findViewById(R.id.driverSelectLayout).setVisibility(View.VISIBLE);

                // Dynamically adjust the deliveryLayout to come after driverSelectLayout
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) deliveryLayout.getLayoutParams();
                params.topToBottom = R.id.driverSelectLayout; // Set the delivery layout below driverSelectLayout
                deliveryLayout.setLayoutParams(params);

                // Populate the driver spinner with available drivers
                if (availableDrivers != null && !availableDrivers.isEmpty()) {
                    populateDriverSpinner();  // Populate the spinner here
                }
            } else {
                // Hide the driver select layout when driverYes is not selected
                findViewById(R.id.driverSelectLayout).setVisibility(View.GONE);

                // Adjust deliveryLayout's position to go back to its previous position
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) deliveryLayout.getLayoutParams();
                params.topToBottom = R.id.driverLayout; // Move back deliveryLayout under driverLayout
                deliveryLayout.setLayoutParams(params);
            }
        });

        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GeoPoint newLocation = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());
                updateLocation(newLocation);
            }
            return true;
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

        zoomInButton.setOnClickListener(v -> zoomIn());
        zoomOutButton.setOnClickListener(v -> zoomOut());

        Button submitButton = findViewById(R.id.reserveButton);
        submitButton.setOnClickListener(v -> reserveCar());
    }

    private void getAndDisplayCar() {
        carModel = findViewById(R.id.carName);
        Intent intent = getIntent();
        carModel.setText(intent.getStringExtra("model"));
        connectedUser = intent.getParcelableExtra("user");
        token = intent.getStringExtra("token");

        image = findViewById(R.id.carImage);
        String encodedImage = intent.getStringExtra("picture");
        if (encodedImage != null) {
            setImageFromEncodedString(encodedImage);
        } else {
            image.setImageResource(R.drawable.car1);
        }
    }

    public void getAvailableDrivers() {
        Intent intent = getIntent();
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");

        AvailabilityRequest request = new AvailabilityRequest(startDate, endDate);
        DriverService driverService = RetrofitClient.getRetrofitInstance().create(DriverService.class);
        Call<List<Driver>> driversCall = driverService.getAvailableDrivers(request);

        driversCall.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    availableDrivers = response.body();
                    // Populate the spinner after retrieving the drivers
                    if (driverYes) {
                        populateDriverSpinner();
                    }
                } else {
                    Toast.makeText(FirstReservation.this, "Error retrieving available drivers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Toast.makeText(FirstReservation.this, "Unable to load drivers. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateDriverSpinner() {
        List<String> driverNamesWithRegions = new ArrayList<>();
        for (Driver driver : availableDrivers) {
            driverNamesWithRegions.add(driver.user.name + " (" + driver.region + ")"+ "      "+"priceByDay="+driver.priceByDay);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, driverNamesWithRegions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        driverSpinner.setAdapter(adapter);
    }

    private void setImageFromEncodedString(String encodedImage) {
        try {
            PictureService pictureService = new PictureServiceImpl();
            Bitmap decodedBitmap = pictureService.decompressBase64ToImage(encodedImage);
            image.setImageBitmap(decodedBitmap);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error decoding image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDateFromExtras() {
        startDateDisplay.setText(startDate != null ? startDate : "");
        endDateDisplay.setText(endDate != null ? endDate : "");
    }

    private void showMap() {
        locationView.setVisibility(LinearLayout.VISIBLE);
        if (myLocationOverlay.getMyLocation() != null) {
            GeoPoint geoPoint = new GeoPoint(myLocationOverlay.getMyLocation());
            if (startingLocation == null) {
                startingLocation = geoPoint;
                latestSelectedLocation = geoPoint;
            }
            mapView.getController().setZoom(15.0);
            mapView.getController().setCenter(geoPoint);
        }
    }

    private void hideMap() {
        locationView.setVisibility(LinearLayout.GONE);
    }

    private void updateLocationDisplays(GeoPoint location) {
        latitudeDisplay.setText(String.valueOf(location.getLatitude()));
        longitudeDisplay.setText(String.valueOf(location.getLongitude()));

        if (currentLocationMarker == null) {
            currentLocationMarker = new Marker(mapView);
            currentLocationMarker.setTitle("Current Location");
            mapView.getOverlays().add(currentLocationMarker);
        }
        currentLocationMarker.setPosition(location);
        mapView.invalidate();
    }

    private void updateLocation(GeoPoint newLocation) {
        latestSelectedLocation = newLocation;
        updateLocationDisplays(newLocation);
    }

    private void zoomIn() {
        mapView.getController().zoomIn();
    }

    private void zoomOut() {
        mapView.getController().zoomOut();
    }

    private void reserveCar() {
        String startDate = startDateDisplay.getText().toString();
        String endDate = endDateDisplay.getText().toString();
        Double deliveryLongitude = null;
        Double deliveryLatitude = null;
        Location location = null;

        // Check if delivery is enabled to pass location or null values
        if (this.deliveryYes && latestSelectedLocation != null) {
            deliveryLongitude = latestSelectedLocation.getLongitude();
            deliveryLatitude = latestSelectedLocation.getLatitude();

            if (!Double.isNaN(deliveryLongitude) && !Double.isNaN(deliveryLatitude)) {
                location = new Location(deliveryLatitude, deliveryLongitude);
            } else {
                location = null; // Set to a default location if needed
            }
        }

        Intent oldIntent = getIntent();
        User user = oldIntent.getParcelableExtra("user");
        Log.d("reservation", user.toString());
        String carId = oldIntent.getStringExtra("carId");

        // Determine the driver ID to use in the reservation
        String driverId = null;
        if (driverYes) {
            int selectedPosition = driverSpinner.getSelectedItemPosition();
            if (selectedPosition != AdapterView.INVALID_POSITION && selectedPosition < availableDrivers.size()) {
                driverId = availableDrivers.get(selectedPosition).user._id;
            }
        }

        // Create Reservation object
        ReserveRequest reservation = new ReserveRequest(carId, user._id, startDate, endDate,location, driverId);

        ReservationService reservationService = RetrofitClient.getRetrofitInstance().create(ReservationService.class);
        Log.d("request reservation:", reservation.toString());

        Call<Reservation> reservationCall = reservationService.reserver(reservation);
        reservationCall.enqueue(new Callback<Reservation>() {
            @Override
            public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Reservation res = response.body();
                    // Log the successful reservation
                    Log.d("ReservationLog", res.toString());
                    Toast.makeText(FirstReservation.this, "Reservation added successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Log the error response
                    Log.e("ReservationLog", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(FirstReservation.this, "Error adding Reservation: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reservation> call, Throwable t) {
                // Log the failure message
                Log.e("ReservationLog", "Failure: " + t.getMessage());
                Toast.makeText(FirstReservation.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


