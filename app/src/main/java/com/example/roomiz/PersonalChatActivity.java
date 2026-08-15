package com.example.roomiz;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalChatActivity extends AppCompatActivity {
    private final List<ChatMessage> messages = new ArrayList<>();  // Messages in this chat.
    private PersonalMessageAdapter adapter;  // Message list adapter.
    private RecyclerView messagesView;  // Message list view.
    private EditText messageInput;  // Message field.
    private FirebaseFirestore firestore;  // Chat database.
    private String userId;  // Current user id.
    private String profileId;  // Other profile id.
    private ListenerRegistration messageListener;  // Live message listener.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        userId = FirebaseAuth.getInstance().getUid();
        profileId = getIntent().getStringExtra("profile_id");
        if (userId == null || profileId == null) {
            Toast.makeText(this, R.string.messages_load_failed, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvChatName)).setText(getIntent().getStringExtra("chat_name"));
        ProfileImageLoader.load(findViewById(R.id.ivChatAvatar), getIntent().getStringExtra("image_name"));

        // Return to the chat list by finishing the Activity lifecycle.
        findViewById(R.id.btnChatBack).setOnClickListener(view -> finish());

        messagesView = findViewById(R.id.rvMessages);
        messagesView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonalMessageAdapter(messages, userId);
        messagesView.setAdapter(adapter);

        messageInput = findViewById(R.id.etMessage);
        MaterialButton sendButton = findViewById(R.id.btnSend);
        sendButton.setOnClickListener(view -> sendMessage());  // When message sent
        messageInput.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });

        firestore = FirebaseFirestore.getInstance();
        listenForMessages();
    }

    // Keep messages updated live.
    private void listenForMessages() {
        messageListener = firestore.collection("users").document(userId).collection("conversations")
                .document(profileId).collection("messages").orderBy("timestamp")
                .addSnapshotListener((snapshots, error) -> {  // Watch current conversation.
                    if (error != null || snapshots == null) {
                        Toast.makeText(this, R.string.messages_load_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    messages.clear();
                    for (DocumentSnapshot document : snapshots) {
                        String text = document.getString("text");
                        String senderId = document.getString("senderId");
                        Timestamp timestamp = document.getTimestamp("timestamp");
                        if (text != null && senderId != null) messages.add(new ChatMessage(text, senderId, timestamp));
                    }
                    adapter.notifyDataSetChanged();
                    if (!messages.isEmpty()) messagesView.scrollToPosition(messages.size() - 1);
                });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (text.isEmpty()) return;
        Map<String, Object> message = new HashMap<>();
        message.put("text", text);
        message.put("senderId", userId);
        message.put("timestamp", FieldValue.serverTimestamp());
        firestore.collection("users").document(userId).collection("conversations")
                .document(profileId).collection("messages").add(message)
                .addOnFailureListener(error -> Toast.makeText(this, R.string.message_send_failed, Toast.LENGTH_SHORT).show());
        messageInput.setText("");
    }

    @Override
    protected void onDestroy() {
        if (messageListener != null) messageListener.remove();
        super.onDestroy();
    }
}