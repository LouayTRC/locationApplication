package com.example.locationapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.locationapp.Admin.AdminHome;
import com.example.locationapp.Client.CarListClient;
import com.example.locationapp.Client.ClientHome;

import models.Car;
import models.User;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_LOGIN=1;
    Button loginBtn,accountBtn,logoutBtn;
    User connectedUser;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginBtn=findViewById(R.id.loginBtn);
        logoutBtn=findViewById(R.id.logoutBtn);
        accountBtn=findViewById(R.id.accountBtn);

    }

    public void goToLogin(View view) {
        Intent intent=new Intent(MainActivity.this, Login.class);
        startActivityForResult(intent,REQUEST_CODE_LOGIN);

    }

    public void goToCarList(View view) {
        Intent intent=new Intent(MainActivity.this, CarListClient.class);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK) {

            connectedUser=data.getParcelableExtra("user");
            Log.d("user",connectedUser.toString());
            token=data.getStringExtra("token");

            loginBtn.setVisibility(View.GONE);
            accountBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.VISIBLE);

            accountBtn.setOnClickListener(e->{
                if (connectedUser.role.name.equals("CLIENT")){
                    Intent intent1=new Intent(MainActivity.this, ClientHome.class);
                    intent1.putExtra("user",connectedUser);
                    intent1.putExtra("token",token);
                    startActivity(intent1);
                }
                else if (connectedUser.role.name.equals("DRIVER")){
                    Intent intent1=new Intent(MainActivity.this, DriverHome.class);
                    intent1.putExtra("user",connectedUser);
                    intent1.putExtra("token",token);
                    startActivity(intent1);
                }
                else{
                    Intent intent1=new Intent(MainActivity.this, AdminHome.class);
                    intent1.putExtra("user",connectedUser);
                    intent1.putExtra("token",token);
                    startActivity(intent1);
                }
            });

            logoutBtn.setOnClickListener(e->{
                connectedUser=null;
                token=null;
                logoutBtn.setVisibility(View.GONE);
                accountBtn.setVisibility(View.GONE);
                loginBtn.setVisibility(View.VISIBLE);
            });
        }
    }

}
