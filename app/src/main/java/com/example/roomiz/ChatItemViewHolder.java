package com.example.roomiz;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivChatProfile;
    public TextView tvFullName;
    public TextView tvLastMessage;

    public ChatItemViewHolder(@NonNull View itemView) {
        super(itemView);
        ivChatProfile = itemView.findViewById(R.id.ivChatProfile);
        tvFullName = itemView.findViewById(R.id.tvFullName);
        tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
    }
}
