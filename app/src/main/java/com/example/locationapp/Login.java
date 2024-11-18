package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.locationapp.Admin.AddCar;

import Config.RetrofitClient;
import models.Car;
import models.Requests.LoginRequest;
import models.Requests.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.AuthService;

public class Login extends AppCompatActivity {
    EditText emailInput,passwordInput;
    Button loginBtn,signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        emailInput=findViewById(R.id.email);
        passwordInput=findViewById(R.id.pwd);
        loginBtn=findViewById(R.id.loginButton);
        signupBtn=findViewById(R.id.signupButton);


        loginBtn.setOnClickListener(event->{
            String email=emailInput.getText().toString().trim();
            String pwd=passwordInput.getText().toString().trim();

            LoginRequest loginRequest=new LoginRequest(email,pwd);

            AuthService authService= RetrofitClient.getRetrofitInstance().create(AuthService.class);

            authService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        // Show a success message
                        Intent intent=new Intent(Login.this, MainActivity.class);
                        intent.putExtra("user",response.body().user);
                        intent.putExtra("token",response.body().token);
                        setResult(RESULT_OK,intent);
                        finish(); // Optionally close the activity or redirect
                    } else {
                        Toast.makeText(Login.this, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(Login.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        signupBtn.setOnClickListener(e->{
            Intent intent=new Intent(Login.this, Signup.class);
            startActivity(intent);
        });
    }
}