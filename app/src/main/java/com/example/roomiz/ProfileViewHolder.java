package com.example.roomiz;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivProfileImage;  // Profile image
    public TextView tvMatchPercentage;  // Match percentage
    public TextView tvProfileName;  // Profile name
    public TextView tvAgeCity;  // Age and city
    public TextView tvAbout;  // About
    public TextView tvTagOne;  // Tag one
    public TextView tvTagTwo;  // Tag two
    public ImageButton btnLike;  // Like button
    public ImageButton btnUnlike;  // Unlike button
    public ImageButton btnChat;  // Chat button

    public ProfileViewHolder(@NonNull View itemView) {
        super(itemView);

        ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
        tvMatchPercentage = itemView.findViewById(R.id.tvMatchPercentage);
        tvProfileName = itemView.findViewById(R.id.tvProfileName);
        tvAgeCity = itemView.findViewById(R.id.tvAgeCity);
        tvAbout = itemView.findViewById(R.id.tvAbout);
        tvTagOne = itemView.findViewById(R.id.tvTagOne);
        tvTagTwo = itemView.findViewById(R.id.tvTagTwo);
        btnLike = itemView.findViewById(R.id.btnLike);
        btnUnlike = itemView.findViewById(R.id.btnUnlike);
        btnChat = itemView.findViewById(R.id.btnChat);
    }
}
