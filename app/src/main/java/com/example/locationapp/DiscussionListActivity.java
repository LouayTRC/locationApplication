package com.example.locationapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
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
    private List<String> discussions; // Replace with actual model in a real implementation

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_list);

        recyclerViewDiscussions = findViewById(R.id.recyclerViewDiscussions);
        fabAddDiscussion = findViewById(R.id.fabAddDiscussion);

        discussions = new ArrayList<>(); // Replace with data from your database or server
        discussions.add("Driver 1");
        discussions.add("Driver 2");

        discussionAdapter = new DiscussionAdapter(discussions, new DiscussionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String driverName) {
                // Open chat activity with selected driver
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
                Intent intent = new Intent(DiscussionListActivity.this, DriverListActivity.class);
                startActivity(intent);
            }
        });
    }
}
