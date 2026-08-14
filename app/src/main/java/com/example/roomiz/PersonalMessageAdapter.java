package com.example.roomiz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PersonalMessageAdapter extends RecyclerView.Adapter<PersonalMessageAdapter.MessageViewHolder> {
    private static final int SENT = 1;
    private static final int RECEIVED = 2;
    private final List<ChatMessage> messages;

    public PersonalMessageAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return "me".equals(messages.get(position).getSenderId()) ? SENT : RECEIVED;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = viewType == SENT ? R.layout.item_message_sent : R.layout.item_message_received;
        return new MessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.message.setText(messages.get(position).getText());
    }

    @Override
    public int getItemCount() { return messages.size(); }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView message;
        MessageViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tvMessage);
        }
    }
}