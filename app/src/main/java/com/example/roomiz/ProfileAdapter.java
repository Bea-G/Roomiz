package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    public interface OnLikeActionListener {
        void onLike(int position, View itemView);
        void onUnlike(int position, View itemView);
    }

    private final List<Profile> profiles;
    private final OnLikeActionListener listener;

    public ProfileAdapter(List<Profile> profiles, OnLikeActionListener listener) {
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
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onLike(pos, holder.itemView);
            }
        });

        holder.btnUnlike.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onUnlike(pos, holder.itemView);
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