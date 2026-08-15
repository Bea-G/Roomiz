package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemViewHolder> {
    public interface OnChatClickListener {
        void onChatClick(ChatItem item);
    }

    private final List<ChatItem> chatItems = new ArrayList<>();
    private final List<ChatItem> allChatItems = new ArrayList<>();  // Full list used by search.
    private final OnChatClickListener listener;

    public ChatItemAdapter(OnChatClickListener listener) {
        this.listener = listener;
    }

    public void setChatItems(List<ChatItem> items) {
        allChatItems.clear();
        allChatItems.addAll(items);
        chatItems.clear();
        chatItems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
        ChatItem item = chatItems.get(position);
        ProfileImageLoader.load(holder.ivChatProfile, item.getImageName());
        holder.tvFullName.setText(item.getFullName());
        holder.tvLastMessage.setText(item.getLastMessage());
        // Open the selected chat.
        holder.itemView.setOnClickListener(view -> listener.onChatClick(item));
    }

    @Override
    public int getItemCount() { return chatItems.size(); }

    public List<ChatItem> getAllChatItems() { return allChatItems; }

    public void updateLastMessage(String profileId, String preview) {
        for (ChatItem item : allChatItems) {
            if (item.getProfileId().equals(profileId)) {
                item.setLastMessage(preview);
                int position = chatItems.indexOf(item);
                if (position >= 0) notifyItemChanged(position);
                return;
            }
        }
    }

    public void filter(String query) {
        chatItems.clear();
        String lowerQuery = query == null ? "" : query.trim().toLowerCase();
        for (ChatItem item : allChatItems) {
            if (lowerQuery.isEmpty() || item.getFullName().toLowerCase().contains(lowerQuery)) chatItems.add(item);
        }
        notifyDataSetChanged();
    }
}