package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DiscussionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDiscussions;
    private FloatingActionButton fabAddDiscussion;
    private DiscussionAdapter discussionAdapter;
    private List<String> discussions; // List of discussions (drivers)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_list);

        recyclerViewDiscussions = findViewById(R.id.recyclerViewDiscussions);
        fabAddDiscussion = findViewById(R.id.fabAddDiscussion);

        discussions = new ArrayList<>(); // Replace with data from your database or server
        discussions.add("Driver 1");
        discussions.add("Driver 2");

        // Set up the adapter
        discussionAdapter = new DiscussionAdapter(discussions, new DiscussionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String driverName) {
                // Open chat activity with the selected driver
                Intent intent = new Intent(DiscussionListActivity.this, ChatActivity.class);
                intent.putExtra("driverName", driverName); // Pass driver info to ChatActivity
                startActivity(intent);
            }
        });

        recyclerViewDiscussions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDiscussions.setAdapter(discussionAdapter);

        fabAddDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDriverSelectionDialog(); // Show the driver selection dialog
            }
        });
    }

    // Method to show the driver selection dialog
    private void showDriverSelectionDialog() {
        final String[] drivers = {"Driver 1", "Driver 2", "Driver 3", "Driver 4"}; // List of available drivers

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Driver")
                .setItems(drivers, (dialog, which) -> {
                    String selectedDriver = drivers[which];
                    addNewDiscussion(selectedDriver); // Add the new discussion with the selected driver
                })
                .create()
                .show();
    }

    // Method to add a new discussion to the list
    private void addNewDiscussion(String driverName) {
        discussions.add(driverName); // Add the selected driver to the discussion list
        discussionAdapter.notifyItemInserted(discussions.size() - 1); // Notify the adapter that a new item has been added
        recyclerViewDiscussions.scrollToPosition(discussions.size() - 1); // Scroll to the new item
    }
}
