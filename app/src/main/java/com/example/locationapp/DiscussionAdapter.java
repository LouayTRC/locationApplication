package com.example.locationapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.DiscussionViewHolder> {

    private List<String> discussions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String driverName);
    }

    public DiscussionAdapter(List<String> discussions, OnItemClickListener listener) {
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
        String driverName = discussions.get(position);
        holder.bind(driverName, listener);
    }

    @Override
    public int getItemCount() {
        return discussions.size();
    }

    public static class DiscussionViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDriverName;

        public DiscussionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDriverName = itemView.findViewById(R.id.textViewDriverName);
        }

        public void bind(String driverName, OnItemClickListener listener) {
            textViewDriverName.setText(driverName);
            itemView.setOnClickListener(v -> listener.onItemClick(driverName));
        }
    }
}
