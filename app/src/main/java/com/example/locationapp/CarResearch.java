package com.example.locationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Config.RetrofitClient;
import models.Car;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.CarService;

public class CarResearch extends AppCompatActivity {

    private Spinner spinnerModel, spinnerYear;
    private ListView listView;
    private List<Car> carList;
    private List<Car> filteredList; // Liste pour les voitures filtrées
    private ListAdapter listAdapter; // Adapter pour le ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_research);

        // Initialisation des spinners et du ListView
        spinnerModel = findViewById(R.id.spinner9);
        spinnerYear = findViewById(R.id.spinner10);
        listView = findViewById(R.id.listview); // Assurez-vous que votre ListView a cet ID dans le XML
        carList = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Récupérer les voitures
        fetchCars();

        // Configurer l'écouteur de sélection pour le spinner des modèles
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = (String) parent.getItemAtPosition(position);
                filterCarsByModel(selectedModel);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Ne rien faire
            }
        });
    }

    private void fetchCars() {
        CarService carService = RetrofitClient.getRetrofitInstance().create(CarService.class);
        Call<List<Car>> call = carService.getCars();

        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    carList = response.body();
                    // Remplir les spinners avec les données
                    populateSpinners();
                    filteredList.clear(); // Vider la liste filtrée avant d'ajouter les nouvelles données
                    filteredList.addAll(carList); // Initialiser la liste filtrée avec toutes les voitures
                    listAdapter = new ListAdapter(CarResearch.this, (ArrayList<Car>) filteredList);
                    listView.setAdapter(listAdapter); // Définir l'adaptateur sur le ListView
                } else {
                    Toast.makeText(CarResearch.this, "Erreur lors de la récupération des voitures", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(CarResearch.this, "Erreur: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSpinners() {
        // Remplissage fictif (vous pouvez le remplacer par des données de la base)
        Set<String> modelSet = new HashSet<>();
        Set<Integer> yearSet = new HashSet<>();

        for (Car car : carList) {
            if (car.model != null) modelSet.add(car.model);
            if (car.year != null) yearSet.add(car.year);
        }

        // Convertir les ensembles en listes
        List<String> modelList = new ArrayList<>(modelSet);
        List<Integer> yearList = new ArrayList<>(yearSet);

        // Remplir le spinner des modèles
        ArrayAdapter<String> modelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, modelList);
        modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(modelAdapter);

        // Remplir le spinner des années
        ArrayAdapter<Integer> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    private void filterCarsByModel(String model) {
        filteredList.clear(); // Vider la liste filtrée avant de l'utiliser

        for (Car car : carList) {
            if (car.model != null && car.model.equals(model)) {
                filteredList.add(car); // Ajouter la voiture correspondante à la liste filtrée
            }
        }

        // Mettre à jour le ListView avec les résultats filtrés
        updateListView();
    }

    private void updateListView() {
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged(); // Informer l'adaptateur des changements de données
        } else {
            listAdapter = new ListAdapter(this, (ArrayList<Car>) filteredList);
            listView.setAdapter(listAdapter); // Définir l'adaptateur sur le ListView
        }
    }
}
