package com.example.locationapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Requests.SignupRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.AuthService;
import services.RoleService;
import models.Role;

public class Signup extends AppCompatActivity {
    private EditText cinEditText, nameEditText, emailEditText, phoneEditText, passwordEditText;
    private Spinner roleSpinner;
    private Button confirmButton;
    private TextView roleHint;
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
        confirmButton = findViewById(R.id.confirmButton);
        TextView roleSelectedText = findViewById(R.id.roleSelectedText);  // Le TextView qui affiche le rôle sélectionné

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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Si rien n'est sélectionné, vous pouvez définir un texte par défaut
                roleSelectedText.setText("Ton rôle est :");
            }
        });
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

        SignupRequest signupRequest = new SignupRequest(cin, phone, email, pwd, name, role);

        AuthService authService = RetrofitClient.getRetrofitInstance().create(AuthService.class);
        if (validateInputs()) {
            Call<Void> signupCall = authService.signup(signupRequest);
            signupCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        if (role.equals("CLIENT")) {
                            Toast.makeText(Signup.this, "Compte créé avec succès!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(Signup.this, "Compte créé avec succès! Vous devez attendre l'accès.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Log.e("SignupLog", "Erreur : " + response.code() + " - " + response.message());
                        Toast.makeText(Signup.this, "Erreur lors de l'inscription : " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("SignupLog", "Échec : " + t.getMessage());
                    Toast.makeText(Signup.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInputs() {
        // Validation des champs comme dans le code original
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

        if (cinEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "CIN requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (roleSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "Rôle requis.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
