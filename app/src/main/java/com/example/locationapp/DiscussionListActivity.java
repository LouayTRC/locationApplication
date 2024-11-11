package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

import Config.RetrofitClient;
import models.Discussion;
import models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import services.ChatService;

public class DiscussionListActivity extends AppCompatActivity {
    private User connectedUser;
    private String token;
    List<User> contacts = new ArrayList<>();

    private RecyclerView recyclerViewDiscussions;
    private FloatingActionButton fabAddDiscussion;
    private DiscussionAdapter discussionAdapter;
    private List<Discussion> discussions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_list);

        Intent intent = getIntent();
        connectedUser = intent.getParcelableExtra("user");
        token = intent.getStringExtra("token");

        recyclerViewDiscussions = findViewById(R.id.recyclerViewDiscussions);
        fabAddDiscussion = findViewById(R.id.fabAddDiscussion);

        discussions = new ArrayList<>();
        discussionAdapter = new DiscussionAdapter(connectedUser,discussions, discussion -> {
            // Open chat activity with the selected discussion
            Intent chatIntent = new Intent(DiscussionListActivity.this, ChatActivity.class);
            chatIntent.putExtra("discussionId", discussion._id);
            chatIntent.putExtra("user",connectedUser);
            startActivity(chatIntent);
        });

        recyclerViewDiscussions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDiscussions.setAdapter(discussionAdapter);

        fetchDiscussions();

        fabAddDiscussion.setOnClickListener(view -> showDriverSelectionDialog());
    }

    private void fetchDiscussions() {
        ChatService chatService = RetrofitClient.getRetrofitInstance().create(ChatService.class);
        chatService.getUserDiscussions(connectedUser._id).enqueue(new Callback<List<Discussion>>() {
            @Override
            public void onResponse(Call<List<Discussion>> call, Response<List<Discussion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    discussions.clear();
                    discussions.addAll(response.body());
                    discussionAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Error: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Discussion>> call, Throwable t) {
                Log.d("Failure: ", t.getMessage());
            }
        });
    }

    private void showDriverSelectionDialog() {
        ChatService chatService = RetrofitClient.getRetrofitInstance().create(ChatService.class);
        chatService.getContacts(connectedUser._id).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contacts = response.body();

                    // Check for null values in the contacts list
                    List<String> driverNames = new ArrayList<>();
                    for (User contact : contacts) {
                        if (contact != null && contact.name != null) {
                            driverNames.add(contact.name);
                        }
                    }

                    if (!driverNames.isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DiscussionListActivity.this);
                        builder.setTitle("Select From Your Contacts")
                                .setItems(driverNames.toArray(new String[0]), (dialog, which) -> {
                                    // Pass the correct contactId (contact._id)
                                    String selectedContactId = contacts.get(which)._id;
                                    addNewDiscussion(selectedContactId);  // Use the contact's _id
                                })
                                .create()
                                .show();
                    } else {
                        Toast.makeText(DiscussionListActivity.this, "No valid contacts found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("Error: ", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.d("Failure: ", t.getMessage());
            }
        });
    }

    private void addNewDiscussion(String contactId) {
        // First, check if the discussion with the contact already exists
        boolean discussionExists = false;
        for (Discussion discussion : discussions) {
            // Compare user IDs (assuming the discussion has user1 and user2 as references to the users)
            if ((discussion.user1._id.equals(contactId) && discussion.user2._id.equals(connectedUser._id)) ||
                    (discussion.user2._id.equals(contactId) && discussion.user1._id.equals(connectedUser._id))) {
                discussionExists = true;
                break;
            }
        }

        if (discussionExists) {
            // If the discussion already exists, don't create a new one
            Log.d("Discussion Exists", "Discussion already exists with this contact.");
            return; // Exit early
        }

        // If no discussion exists, create a new one
        ChatService chatService = RetrofitClient.getRetrofitInstance().create(ChatService.class);
        chatService.createDiscussion(connectedUser._id, contactId).enqueue(new Callback<Discussion>() {
            @Override
            public void onResponse(Call<Discussion> call, Response<Discussion> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Discussion newDiscussion = response.body();
                    discussions.add(newDiscussion);
                    discussionAdapter.notifyItemInserted(discussions.size() - 1);
                    recyclerViewDiscussions.scrollToPosition(discussions.size() - 1);
                } else {
                    Log.d("Error: ", "Failed to create discussion.");
                }
            }

            @Override
            public void onFailure(Call<Discussion> call, Throwable t) {
                Log.d("Failure: ", t.getMessage());
            }
        });
    }




}
