package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    public interface OnActionListener {
        // Interface for the action listeners
        void onLike(int position, View itemView);  // When the like button is pressed
        void onUnlike(int position, View itemView);  // When the unlike button is pressed
        void onChat(Profile profile);  // When the chat button is pressed
    }

    private final List<Profile> profiles;  // List of profiles
    private final OnActionListener listener;  // Listener for the action listeners

    public ProfileAdapter(List<Profile> profiles, OnActionListener listener) {
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
        // Bind the profile data to the view holder

        Profile profile = profiles.get(position);

        holder.ivProfileImage.setImageResource(profile.getImageResId());
        holder.tvMatchPercentage.setText("⚡ " + profile.getMatchPercentage() + "%");
        holder.tvProfileName.setText(profile.getName());
        holder.tvAgeCity.setText(profile.getAge() + ", " + profile.getCity());
        holder.tvAbout.setText(profile.getAbout());
        holder.tvTagOne.setText(profile.getTagOne());
        holder.tvTagTwo.setText(profile.getTagTwo());

        // Set the click listeners for the buttons
        holder.btnLike.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();  // Get the position of the item
            if (pos != RecyclerView.NO_POSITION) { // Check if the position is valid
                listener.onLike(pos, holder.itemView);  // Call the onLike method of the listener
            }
        });

        holder.btnUnlike.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();  // Get the position of the item
            if (pos != RecyclerView.NO_POSITION) {  // Check if the position is valid
                listener.onUnlike(pos, holder.itemView);  // Call the onUnlike method of the listener
            }
        });

        holder.btnChat.setOnClickListener(v -> {
            listener.onChat(profile);  // Call the onChat method of the listener
        });
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void removeCard(int position) {
        // Remove the card at the given position
        if (position >= 0 && position < profiles.size()) {
            profiles.remove(position);  // Remove the profile from the list
            notifyItemRemoved(position);  // Notify the adapter that the item has been removed
        }
    }
}