package com.example.roomiz;

import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalChatActivity extends AppCompatActivity {
    private final List<ChatMessage> messages = new ArrayList<>();
    private PersonalMessageAdapter adapter;
    private RecyclerView messagesView;
    private EditText messageInput;
    private FirebaseFirestore firestore;
    private String conversationId;
    private ListenerRegistration messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_chat);
        new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView()).setAppearanceLightStatusBars(true);

        String chatName = getIntent().getStringExtra("chat_name");
        if (chatName == null || chatName.trim().isEmpty()) chatName = "Dana Levy";
        conversationId = "me_" + chatName.toLowerCase().replaceAll("[^a-z0-9]+", "_");

        ((TextView) findViewById(R.id.tvChatName)).setText(chatName);
        ((de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.ivChatAvatar)).setImageResource(imageFor(chatName));
        ((ImageButton) findViewById(R.id.btnChatBack)).setOnClickListener(view -> finish());

        messagesView = findViewById(R.id.rvMessages);
        messagesView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonalMessageAdapter(messages);
        messagesView.setAdapter(adapter);

        messageInput = findViewById(R.id.etMessage);
        MaterialButton sendButton = findViewById(R.id.btnSend);
        sendButton.setOnClickListener(view -> sendMessage());
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

    private void listenForMessages() {
        messageListener = firestore.collection("conversations").document(conversationId)
                .collection("messages").orderBy("timestamp")
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;
                    messages.clear();
                    for (DocumentSnapshot document : snapshots.getDocuments()) {
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
        message.put("senderId", "me");
        message.put("timestamp", FieldValue.serverTimestamp());
        firestore.collection("conversations").document(conversationId).collection("messages").add(message);
        messageInput.setText("");
    }

    private int imageFor(String name) {
        String lowerName = name.toLowerCase();
        if (lowerName.contains("mika")) return R.drawable.mika_dan;
        if (lowerName.contains("daniel")) return R.drawable.daniel_levy;
        if (lowerName.contains("gaya")) return R.drawable.gaya_refael;
        if (lowerName.contains("alon")) return R.drawable.alon_ron;
        if (lowerName.contains("ori")) return R.drawable.ori_keidar;
        if (lowerName.contains("tom")) return R.drawable.tom_sasson;
        if (lowerName.contains("yuval")) return R.drawable.yuval_matalon;
        return R.drawable.dana_levy;
    }

    @Override
    protected void onDestroy() {
        if (messageListener != null) messageListener.remove();
        super.onDestroy();
    }
}