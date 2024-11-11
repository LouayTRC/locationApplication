package com.example.locationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import models.Discussion;
import models.User;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {
    private final User connectedUser;
    private final List<Discussion> discussions;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Discussion discussion);
    }

    public DiscussionAdapter(User user, List<Discussion> discussions, OnItemClickListener listener) {
        this.connectedUser = user;
        this.discussions = discussions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiscussionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discussion, parent, false);
        return new DiscussionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussionViewHolder holder, int position) {
        Discussion discussion = discussions.get(position);
        holder.bind(discussion, connectedUser, listener);
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDriverName;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDriverName = itemView.findViewById(R.id.textViewDriverName);
        }

        public void bind(Discussion discussion, User connectedUser, OnItemClickListener listener) {
            // Determine the other participant in the discussion
            String otherParticipantName;
            if (discussion.user1._id.equals(connectedUser._id)) {
                otherParticipantName = discussion.user2.name;
            } else {
                otherParticipantName = discussion.user1.name;
            }

            textViewDriverName.setText(otherParticipantName);
            itemView.setOnClickListener(v -> listener.onItemClick(discussion));
        }
    }
}
