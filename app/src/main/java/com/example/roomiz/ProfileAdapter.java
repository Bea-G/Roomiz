package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    public interface OnLikeClickListener {
        void onLikeClick(int position, View itemView);
    }

    private final List<Profile> profiles;
    private final OnLikeClickListener listener;

    public ProfileAdapter(List<Profile> profiles, OnLikeClickListener listener) {
        this.profiles = profiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_card, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);

        holder.ivProfileImage.setImageResource(profile.getImageResId());
        holder.tvMatchPercentage.setText("⚡ " + profile.getMatchPercentage() + "%");
        holder.tvProfileName.setText(profile.getName());
        holder.tvAgeCity.setText(profile.getAge() + ", " + profile.getCity());
        holder.tvAbout.setText(profile.getAbout());
        holder.tvTagOne.setText(profile.getTagOne());
        holder.tvTagTwo.setText(profile.getTagTwo());

        holder.btnLike.setOnClickListener(v -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onLikeClick(adapterPosition, holder.itemView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void removeCard(int position) {
        if (position >= 0 && position < profiles.size()) {
            profiles.remove(position);
            notifyItemRemoved(position);
        }
    }
}