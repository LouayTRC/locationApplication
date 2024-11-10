package com.example.locationapp.Client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.locationapp.DiscussionListActivity;
import com.example.locationapp.R;

public class ClientHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goToCarList(View view) {
        Intent intent = new Intent(this, CarResearch.class);
        startActivity(intent);
    }

    public void goToDiscussions(View view) {
        // Update this to start DiscussionListActivity instead of ChatActivity
        Intent intent = new Intent(this, DiscussionListActivity.class);
        startActivity(intent);
    }
}
