package com.example.locationapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Requests.SignupClientRequest;
import models.Requests.SignupDriverRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.AuthService;
import services.RoleService;
import models.Role;

public class Signup extends AppCompatActivity {
    private EditText cinEditText, nameEditText, emailEditText, phoneEditText, passwordEditText;
    private Spinner roleSpinner,genderSpinner;
    private Button confirmButton;
    private TextView roleHint, roleSelectedText;
    private LinearLayout regionLayout, driverPriceLayout,genderLayout;
    private EditText regionInput, priceByDayInput;
    private RelativeLayout buttonLayout; // Add a container for the button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialisation des vues
        cinEditText = findViewById(R.id.cin);
        nameEditText = findViewById(R.id.name);
        emailEditText = findViewById(R.id.email);
        phoneEditText = findViewById(R.id.phone);
        passwordEditText = findViewById(R.id.pwdInput);
        roleSpinner = findViewById(R.id.roleSpinner);
        roleHint = findViewById(R.id.roleHint);
        roleSelectedText = findViewById(R.id.roleSelectedText);
        confirmButton = findViewById(R.id.confirmButton);
        genderSpinner=findViewById(R.id.genderSpinner);

        // Initialize the driver info layouts
        regionLayout = findViewById(R.id.regionLayout);
        genderLayout = findViewById(R.id.genderLayout);
        driverPriceLayout = findViewById(R.id.driverPriceLayout);
        regionInput = findViewById(R.id.regionInput);
        priceByDayInput = findViewById(R.id.priceInput);

        buttonLayout = findViewById(R.id.buttonLayout); // Add a reference to the layout holding the button

        // Charger les rôles dans le Spinner
        loadRoles();

        // Configurer l'action du bouton
        confirmButton.setOnClickListener(v -> signup());
        roleHint.setOnClickListener(v -> {
            // S'assurer que le Spinner est activé ou visible pour l'utilisateur
            roleSpinner.performClick();
        });

        // Ajouter un écouteur sur le Spinner pour afficher le rôle sélectionné
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtenez l'élément sélectionné et mettez-le dans le TextView
                String selectedRole = parentView.getItemAtPosition(position).toString();
                roleSelectedText.setText("Ton rôle est : " + selectedRole);

                // Show or hide driver-specific fields and adjust button position based on selected role
                if ("DRIVER".equals(selectedRole)) {
                    regionLayout.setVisibility(View.VISIBLE);
                    driverPriceLayout.setVisibility(View.VISIBLE);
                    genderLayout.setVisibility(View.VISIBLE);
                    // Move the button below the driverPriceLayout
                    moveButtonUnder(driverPriceLayout);
                    displayGenders();
                } else {
                    regionLayout.setVisibility(View.GONE);
                    driverPriceLayout.setVisibility(View.GONE);
                    genderSpinner.setVisibility(View.GONE);
                    // Move the button back under the roleLayout
                    moveButtonUnder(regionLayout);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Default action when nothing is selected
                roleSelectedText.setText("Ton rôle est :");
                regionLayout.setVisibility(View.GONE);
                driverPriceLayout.setVisibility(View.GONE);
                moveButtonUnder(regionLayout);
            }
        });
    }
    public void displayGenders(){
        List<String> genderNames=new ArrayList<>();
        genderNames.add("Male");
        genderNames.add("Female");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Signup.this, android.R.layout.simple_spinner_item, genderNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }
    private void moveButtonUnder(View layoutBelow) {
        // Remove the button from its current position
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) confirmButton.getLayoutParams();
        // Adjust the position based on the selected role
        layoutParams.topMargin = 20; // Optional: Adjust the margin for better spacing
        layoutParams.addRule(RelativeLayout.BELOW, layoutBelow.getId()); // Set the button below the selected layout
        confirmButton.setLayoutParams(layoutParams);
    }

    private void loadRoles() {
        RoleService roleService = RetrofitClient.getRetrofitInstance().create(RoleService.class);
        Call<List<Role>> call = roleService.getRoles();

        call.enqueue(new Callback<List<Role>>() {
            @Override
            public void onResponse(Call<List<Role>> call, Response<List<Role>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Role> roles = response.body();
                    List<String> roleNames = new ArrayList<>();

                    for (Role role : roles) {
                        roleNames.add(role.getName()); // Supposons que `getName()` obtient le nom du rôle
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Signup.this, android.R.layout.simple_spinner_item, roleNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    roleSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(Signup.this, "Erreur lors du chargement des rôles.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Role>> call, Throwable t) {
                Toast.makeText(Signup.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signup() {
        String cin = cinEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String pwd = passwordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString(); // Obtenir le rôle sélectionné dans le Spinner
        String genre= genderSpinner.getSelectedItem().toString();
        // If role is DRIVER, get additional info
        String region = regionInput.getText().toString().trim();
        String priceByDay = priceByDayInput.getText().toString().trim();

        // Only proceed if inputs are valid
        if (validateInputs()) {
            SignupClientRequest signupRequest = new SignupClientRequest(cin, phone, email, pwd, name, role);
            AuthService authService = RetrofitClient.getRetrofitInstance().create(AuthService.class);
            if ("DRIVER".equals(role)) {
                // Safely parse priceByDay to avoid NumberFormatException
                double price = 0;
                try {
                    price = Double.parseDouble(priceByDay);
                } catch (NumberFormatException e) {
                    Toast.makeText(Signup.this, "Le prix par jour doit être un nombre valide.", Toast.LENGTH_SHORT).show();
                    return; // Stop the process if price is invalid
                }

                SignupDriverRequest signupDriverRequest = new SignupDriverRequest(signupRequest, region, price,genre);
                Call<Void> signupCall = authService.signupD(signupDriverRequest);
                signupCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Signup.this, "Compte créé avec succès! Vous devez attendre l'accès.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Signup.this, "Erreur lors de l'inscription : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Signup.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Call<Void> signupCall = authService.signupC(signupRequest);
                signupCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(Signup.this, "Compte créé avec succès!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Signup.this, "Erreur lors de l'inscription : " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(Signup.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private boolean validateInputs() {
        if (nameEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Nom requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String phone = phoneEditText.getText().toString().trim();
        if (phone.length() != 8) {
            Toast.makeText(this, "Le téléphone doit contenir 8 caractères.", Toast.LENGTH_SHORT).show();
            return false;
        }

        String pwd = passwordEditText.getText().toString().trim();
        if (pwd.isEmpty()) {
            Toast.makeText(this, "Mot de passe requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (emailEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Email requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
