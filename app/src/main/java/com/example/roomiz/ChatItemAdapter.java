package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemViewHolder>{
    public interface OnChatClickListener { void onChatClick(ChatItem item); }
    private final OnChatClickListener listener;

    List<ChatItem> chatItems;  // List of chat items
    List<ChatItem> allChatItems;  // List of all chat items (to be able to search for the chats)

    public ChatItemAdapter(OnChatClickListener listener){
        this.listener = listener;

        // Initialize the lists
        chatItems = new ArrayList<>();
        allChatItems = new ArrayList<>();
        allChatItems.add(new ChatItem(R.drawable.mika_dan, "Mika Dan", ""));
        allChatItems.add(new ChatItem(R.drawable.daniel_levy, "Daniel Levy", ""));
        allChatItems.add(new ChatItem(R.drawable.dana_levy, "Dana Levy \uD83C\uDFE0", ""));
        allChatItems.add(new ChatItem(R.drawable.alon_ron, "Alon Ron", ""));
        allChatItems.add(new ChatItem(R.drawable.gaya_refael, "Gaya Refael \uD83C\uDFE0", ""));
        allChatItems.add(new ChatItem(R.drawable.ori_keidar, "Ori Keidar", ""));
        allChatItems.add(new ChatItem(R.drawable.tom_sasson, "Tom Sasson", ""));
        allChatItems.add(new ChatItem(R.drawable.yuval_matalon, "Yuval Matalon", ""));

        chatItems.addAll(allChatItems);  // Show all chats by default
    }


    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        ChatItemViewHolder viewHolder = new ChatItemViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder holder, int position) {
        ChatItem chatItem = chatItems.get(position);
        holder.ivChatProfile.setImageResource(chatItem.getChatProfileImage());
        holder.tvFullName.setText(chatItem.getFullName());
        holder.tvLastMessage.setText(chatItem.getLastMessage());
        holder.itemView.setOnClickListener(view -> listener.onChatClick(chatItem));
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }



    public List<ChatItem> getAllChatItems() {
        return allChatItems;
    }
    public void updateLastMessage(String fullName, String preview) {
        for (ChatItem item : allChatItems) {
            if (item.getFullName().equals(fullName)) {
                item.setLastMessage(preview);
                int visiblePosition = chatItems.indexOf(item);
                if (visiblePosition >= 0) notifyItemChanged(visiblePosition);
                return;
            }
        }
    }
    // Filter the chat items based on the search query
    public void filter(String query) {
        chatItems.clear();  // Clear the list

        if (query == null || query.trim().isEmpty()) {
            // If the query is empty, show all chats
            chatItems.addAll(allChatItems);
        } else {
            // If the query is not empty, filter the chats
            String lowerQuery = query.toLowerCase();

            // Loop through all chat items and add the ones that match the query
            for (ChatItem item : allChatItems) {
                if (item.getFullName().toLowerCase().contains(lowerQuery)) {
                    chatItems.add(item);
                }
            }
        }
        notifyDataSetChanged();  // Update the RecyclerView
    }
}
