package com.example.locationapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private View buttonSend;
    private List<String> messages = new ArrayList<>();
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize the TextView to show the driver name
        TextView textViewDriverName = findViewById(R.id.textViewDriverName);
        String driverName = getIntent().getStringExtra("driverName");
        if (driverName != null) {
            textViewDriverName.setText(driverName); // Display the driver name
        } else {
            textViewDriverName.setText("Unknown Driver");
        }

        // Initialize RecyclerView, EditText, and Send Button
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messages);
        recyclerViewMessages.setAdapter(chatAdapter);

        // Handle sending messages
        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                editTextMessage.setText("");
            }
        });
    }

    private void sendMessage(String message) {
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        recyclerViewMessages.scrollToPosition(messages.size() - 1);
    }
}
