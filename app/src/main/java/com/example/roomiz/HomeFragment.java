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

public class HomeFragment extends Fragment {

    private RecyclerView rvProfileCards;
    private ProfileAdapter pa;
    private List<Profile> profiles;
    View endCard;

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

        endCard = view.findViewById(R.id.endCard);

        rvProfileCards = view.findViewById(R.id.rvProfileCards);
        profiles = ProfileRepository.getProfiles();

        pa = new ProfileAdapter(profiles, new ProfileAdapter.OnActionListener() {
            @Override
            public void onLike(int position, View itemView) {
                animateRightAndRemove(position, itemView);
            }

            @Override
            public void onUnlike(int position, View itemView) {
                animateLeftAndRemove(position, itemView);
            }

            @Override
            public void onChat(Profile profile) {
                openChatsFragment(profile.getName());
            }
        });

        rvProfileCards.setLayoutManager(new StackLayoutManager());
        rvProfileCards.setAdapter(pa);
    }

    private void openChatsFragment(String name) {
        ChatsFragment fragment = new ChatsFragment();

        Bundle bundle = new Bundle();
        bundle.putString("search_name", name);
        fragment.setArguments(bundle);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flHome, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void animateRightAndRemove(int position, View itemView) {
        String name = profiles.get(position).getName();
        showMatchToast("It's a match with " + name + "!🎉");

        itemView.animate()
                .translationX(itemView.getWidth())
                .rotation(12f)
                .alpha(0f)
                .setDuration(300)
                .withEndAction(() -> {
                    pa.removeCard(position);

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

                    checkIfFinished();
                })
                .start();
    }

    private void checkIfFinished() {
        if (pa.getItemCount() == 0) {
            rvProfileCards.setVisibility(View.GONE);
            endCard.setVisibility(View.VISIBLE);
        }
    }

    private void showMatchToast(String message) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View layout = inflater.inflate(R.layout.match_toast, null);

        TextView tv = layout.findViewById(R.id.tvMatchToast);
        tv.setText(message);

        Toast toast = new Toast(requireContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}