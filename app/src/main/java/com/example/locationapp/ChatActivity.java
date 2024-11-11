package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Discussion;
import models.Message;
import models.Requests.SendMessageRequest;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ChatService;

public class ChatActivity extends AppCompatActivity {
    private User connectedUser;
    private String discussionId;
    private Discussion discussion;

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private View buttonSend;
    private List<Message> messages = new ArrayList<>(); // Initialize messages list here
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        connectedUser = intent.getParcelableExtra("user");
        discussionId = intent.getStringExtra("discussionId");

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        chatAdapter = new ChatAdapter(messages, connectedUser); // Pass the messages list to adapter
        recyclerViewMessages.setAdapter(chatAdapter);

        fetchDiscussion(); // Fetch discussion initially

        buttonSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!messageText.isEmpty()) {
                sendMessage(messageText, connectedUser._id, discussionId);
                editTextMessage.setText("");
            }
        });
    }

    private void fetchDiscussion() {
        ChatService chatService = RetrofitClient.getRetrofitInstance().create(ChatService.class);
        chatService.getDiscussionById(discussionId).enqueue(new Callback<Discussion>() {
            @Override
            public void onResponse(Call<Discussion> call, Response<Discussion> response) {
                if (response.isSuccessful() && response.body() != null) {
                    discussion = response.body();
                    messages.clear();
                    messages.addAll(discussion.messages); // Populate messages list
                    chatAdapter.notifyDataSetChanged(); // Notify adapter to refresh

                    TextView textViewDriverName = findViewById(R.id.textViewDriverName);
                    if (discussion.user1._id.equals(connectedUser._id)) {
                        textViewDriverName.setText(discussion.user2.name);
                    } else {
                        textViewDriverName.setText(discussion.user1.name);
                    }
                } else {
                    Log.d("Error: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<Discussion> call, Throwable t) {
                Log.d("Failure: ", t.getMessage());
            }
        });
    }

    private void sendMessage(String message, String senderId, String discussionId) {
        SendMessageRequest messageRequest = new SendMessageRequest(senderId, message);
        ChatService chatService = RetrofitClient.getRetrofitInstance().create(ChatService.class);
        chatService.sendMessage(discussionId, messageRequest).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Message newMessage = response.body();
                    Log.d("newMsg",newMessage.toString());
                    messages.add(newMessage); // Add new message to the list
                    chatAdapter.notifyItemInserted(messages.size() - 1); // Notify adapter
                    recyclerViewMessages.scrollToPosition(messages.size() - 1); // Scroll to new message
                } else {
                    Log.d("Error: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.d("Failure: ", t.getMessage());
            }
        });
    }
}
