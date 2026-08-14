package com.example.roomiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {
    private final List<ListenerRegistration> previewListeners = new ArrayList<>();

    public ChatsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String searchName = getArguments() == null ? null : getArguments().getString("search_name");

        RecyclerView chatList = view.findViewById(R.id.rvChatItems);
        chatList.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        ChatItemAdapter adapter = new ChatItemAdapter(item -> {
            Intent intent = new Intent(requireContext(), PersonalChatActivity.class);
            intent.putExtra("chat_name", item.getFullName());
            startActivity(intent);
        });
        chatList.setAdapter(adapter);
        listenForLastMessages(adapter);

        EditText searchInput = view.findViewById(R.id.etSearch);
        searchInput.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adapter.filter(s.toString()); }
            @Override public void afterTextChanged(android.text.Editable s) { }
        });
        if (searchName != null) {
            searchInput.setText(searchName);
            searchInput.setSelection(searchName.length());
        }
    }

    private void listenForLastMessages(ChatItemAdapter adapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        for (ChatItem item : adapter.getAllChatItems()) {
            ListenerRegistration listener = firestore.collection("conversations")
                    .document(ChatUtils.conversationId(item.getFullName()))
                    .collection("messages")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener((snapshots, error) -> {
                        if (error != null || snapshots == null || snapshots.isEmpty()) return;
                        DocumentSnapshot lastMessage = snapshots.getDocuments().get(0);
                        String text = lastMessage.getString("text");
                        String senderId = lastMessage.getString("senderId");
                        if (text == null || senderId == null) return;
                        String prefix = "me".equals(senderId) ? "Me" : ChatUtils.firstName(item.getFullName());
                        adapter.updateLastMessage(item.getFullName(), prefix + ": " + text);
                    });
            previewListeners.add(listener);
        }
    }

    @Override
    public void onDestroyView() {
        for (ListenerRegistration listener : previewListeners) listener.remove();
        previewListeners.clear();
        super.onDestroyView();
    }
}