package com.example.locationapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.locationapp.ChatActivity;
import com.example.locationapp.R;

import models.User;

public class ClientHome extends AppCompatActivity {
    User connectedUser;
    String token ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_client_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent=getIntent();
        connectedUser=intent.getParcelableExtra("user");
        Log.d("clientHome",connectedUser.toString());
        token=intent.getStringExtra("token");

    }

    public void goToCarList(View view){
        Intent intent=new Intent(this, CarResearch.class);
        intent.putExtra("user",connectedUser);
        intent.putExtra("token",token);
        startActivity(intent);
    }

    public void goToMyReservations(View view){
        Intent intent=new Intent(this, ClientReservations.class);
        intent.putExtra("user",connectedUser);
        intent.putExtra("token",token);
        startActivity(intent);
    }


    public void goToDiscussions(View view){
        Intent intent=new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}