package com.example.roomiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView rvProfileCards;
    private ProfileAdapter adapter;
    private List<Profile> profiles;

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

        rvProfileCards = view.findViewById(R.id.rvProfileCards);
        profiles = ProfileRepository.getProfiles();

        adapter = new ProfileAdapter(profiles, (position, itemView) -> animateAndRemoveCard(position, itemView));

        rvProfileCards.setLayoutManager(new StackLayoutManager());
        rvProfileCards.setAdapter(adapter);
    }

    private void animateAndRemoveCard(int position, View itemView) {
        itemView.animate()
                .translationX(itemView.getWidth())
                .rotation(12f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> adapter.removeCard(position))
                .start();
    }
}