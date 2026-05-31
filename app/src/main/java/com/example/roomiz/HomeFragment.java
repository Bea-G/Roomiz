package com.example.roomiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;
import com.google.firebase.analytics.FirebaseAnalytics;

public class HomeFragment extends Fragment {

    private RecyclerView rvProfileCards;  // RecyclerView for the profile cards
    private ProfileAdapter pa;  // Adapter for the profile cards
    private List<Profile> profiles;  // List of profiles
    View endCard;  // View for the end card
    private FirebaseAnalytics firebaseAnalytics;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        firebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());

        // Initializing
        endCard = view.findViewById(R.id.endCard);

        rvProfileCards = view.findViewById(R.id.rvProfileCards);
        profiles = new ArrayList<>();

        pa = new ProfileAdapter(profiles, new ProfileAdapter.OnActionListener() {
            @Override
            public void onLike(int position, View itemView) {  // When the like button is pressed
                logProfileEvent("like", profiles.get(position));
                animateRightAndRemove(position, itemView);
            }

            @Override
            public void onUnlike(int position, View itemView) {  // When the unlike button is pressed
                logProfileEvent("unlike", profiles.get(position));
                animateLeftAndRemove(position, itemView);
            }

            @Override
            public void onChat(Profile profile) {  // When the chat button is pressed
                // Open the chats fragment
                logProfileEvent("chat_open", profile);
                openChatsFragment(profile.getName());
            }
        });

        rvProfileCards.setLayoutManager(new StackLayoutManager());  // Set the "Deck" layout manager
        rvProfileCards.setAdapter(pa);
        ProfileRepository.getProfilesFromFirestore(new ProfileRepository.ProfilesCallback() {
            @Override
            public void onProfilesLoaded(List<Profile> loadedProfiles) {
                profiles.clear();
                profiles.addAll(loadedProfiles);
                pa.notifyDataSetChanged();
                checkIfFinished();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(requireContext(), "Failed to load profiles", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openChatsFragment(String name) {
        ChatsFragment fragment = new ChatsFragment();

        // Pass the name of the profile to the chat fragment
        Bundle bundle = new Bundle();  // Create a new bundle
        bundle.putString("search_name", name);  // The name of the profile
        fragment.setArguments(bundle);  // Pass the bundle to the fragment

        // Replace the current fragment with the chat fragment
        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flHome, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void animateRightAndRemove(int position, View itemView) {
        String name = profiles.get(position).getName();  // Get the name of the profile
        showMatchToast("It's a match with " + name + "!🎉");

        itemView.animate()
                .translationX(itemView.getWidth())
                .rotation(12f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    pa.removeCard(position);

                    // Check if there are no more cards left and if so, show the end card
                    checkIfFinished();
                })
                .start();
    }

    private void animateLeftAndRemove(int position, View itemView) {
        showMatchToast("Maybe next time");

        itemView.animate()
                .translationX(-itemView.getWidth())
                .rotation(-12f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    pa.removeCard(position);

                    // Check if there are no more cards left and if so, show the end card
                    checkIfFinished();
                })
                .start();
    }

    // Check if RecyclerView of profile cards is empty and if so, show the end card
    private void checkIfFinished() {
        if (pa.getItemCount() == 0) {
            rvProfileCards.setVisibility(View.GONE);
            endCard.setVisibility(View.VISIBLE);
        }
    }

    // Show a custom "match" Toast
    private void showMatchToast(String message) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View layout = inflater.inflate(R.layout.match_toast, null);

        TextView tv = layout.findViewById(R.id.tvMatchToast);
        tv.setText(message);

        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);  // Set custom layout
        toast.show();
    }

    // Send Analytics Event
    private void logProfileEvent(String eventName, Profile profile) {
        Bundle bundle = new Bundle();
        bundle.putString("profile_name", profile.getName());
        bundle.putString("profile_city", profile.getCity());
        bundle.putInt("match_percentage", profile.getMatchPercentage());

        android.util.Log.d("FIREBASE_TEST", "Event sent: " + eventName + " for " + profile.getName());

        firebaseAnalytics.logEvent(eventName, bundle);
    }
}