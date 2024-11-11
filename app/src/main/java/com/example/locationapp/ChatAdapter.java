package com.example.locationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import models.Message;
import models.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messages;
    private User connectedUser;

    public ChatAdapter(List<Message> messages, User connectedUser) {
        this.messages = messages;
        this.connectedUser = connectedUser;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                viewType == 1 ? R.layout.item_message_sent : R.layout.item_message_received,
                parent,
                false
        );
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.bind(messages.get(position).description);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message sender is the connected user
        return messages.get(position).sender._id.equals(connectedUser._id) ? 1 : 0;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView messageTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }

        public void bind(String message) {
            messageTextView.setText(message);
        }
    }
}
