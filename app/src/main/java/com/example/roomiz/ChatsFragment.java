package com.example.roomiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    // One listener for each chat preview.
    private final List<ListenerRegistration> previewListeners = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView chatList = view.findViewById(R.id.rvChatItems);
        chatList.setLayoutManager(new LinearLayoutManager(requireContext()));
        ChatItemAdapter adapter = new ChatItemAdapter(item -> openChat(item));
        chatList.setAdapter(adapter);
        loadProfiles(adapter);

        EditText searchInput = view.findViewById(R.id.etSearch);
        // Filter chats live while end-user types.
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adapter.filter(s.toString()); }
            @Override public void afterTextChanged(android.text.Editable s) { }
        });
    }

    private void loadProfiles(ChatItemAdapter adapter) {
        ProfileRepository.getProfilesFromFirestore(new ProfileRepository.ProfilesCallback() {
            @Override
            public void onProfilesLoaded(List<Profile> profiles) {
                List<ChatItem> items = new ArrayList<>();
                for (Profile profile : profiles) items.add(new ChatItem(profile.getId(), profile.getName(), profile.getImageName()));
                adapter.setChatItems(items);
                listenForLastMessages(adapter);
            }

            @Override
            public void onError(Exception exception) {
                Toast.makeText(requireContext(), R.string.profiles_load_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenForLastMessages(ChatItemAdapter adapter) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId == null) return;
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        for (ChatItem item : adapter.getAllChatItems()) {
            ListenerRegistration listener = firestore.collection("users").document(userId)
                    .collection("conversations").document(item.getProfileId()).collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING).limit(1)
                    .addSnapshotListener((snapshots, error) -> {  // Watch the latest message
                        if (error != null || snapshots == null || snapshots.isEmpty()) return;
                        DocumentSnapshot message = snapshots.getDocuments().get(0);
                        String text = message.getString("text");
                        String senderId = message.getString("senderId");
                        if (text == null || senderId == null) return;
                        String prefix = userId.equals(senderId) ? "Me" : ChatUtils.firstName(item.getFullName());
                        adapter.updateLastMessage(item.getProfileId(), prefix + ": " + text);
                    });
            previewListeners.add(listener);
        }
    }

    private void openChat(ChatItem item) {
        Intent intent = new Intent(requireContext(), PersonalChatActivity.class);
        intent.putExtra("profile_id", item.getProfileId());
        intent.putExtra("chat_name", item.getFullName());
        intent.putExtra("image_name", item.getImageName());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        for (ListenerRegistration listener : previewListeners) listener.remove();
        previewListeners.clear();
        super.onDestroyView();
    }
}