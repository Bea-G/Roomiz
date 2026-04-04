package com.example.roomiz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivChatProfile;  // Profile image
    public TextView tvFullName;  // Full name
    public TextView tvLastMessage;  // Last message


    public ChatItemViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize the views
        ivChatProfile = itemView.findViewById(R.id.ivChatProfile);
        tvFullName = itemView.findViewById(R.id.tvFullName);
        tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
    }
}
