package com.example.roomiz;

import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String searchName = null;  // Name of the profile to search for

        if (getArguments() != null) {  // Check if arguments are passed to the ChatsFragment
            searchName = getArguments().getString("search_name");  // Get the name of the profile
        }

        RecyclerView rvChatItems = view.findViewById(R.id.rvChatItems);
        rvChatItems.setHasFixedSize(false);

        rvChatItems.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        ChatItemAdapter cia = new ChatItemAdapter(item -> {
            Intent intent = new Intent(requireContext(), PersonalChatActivity.class);
            intent.putExtra("chat_name", item.getFullName());
            startActivity(intent);
        });
        rvChatItems.setAdapter(cia);

        EditText etSearch = view.findViewById(R.id.etSearch);

        // Search functionality
        etSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // When the text in the EditText changes
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the chat items based on the search query
                cia.filter(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // If a name was passed to the ChatsFragment, set it to the EditText
        if (searchName != null) {
            etSearch.setText(searchName);
            etSearch.setSelection(searchName.length()); // cursor at end
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }
}