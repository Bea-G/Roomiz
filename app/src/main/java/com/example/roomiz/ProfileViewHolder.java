package com.example.roomiz;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileViewHolder extends RecyclerView.ViewHolder {

    public ImageView ivProfileImage;
    public TextView tvMatchPercentage;
    public TextView tvProfileName;
    public TextView tvAgeCity;
    public TextView tvAbout;
    public TextView tvTagOne;
    public TextView tvTagTwo;
    public ImageButton btnLike;

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
    }
}
