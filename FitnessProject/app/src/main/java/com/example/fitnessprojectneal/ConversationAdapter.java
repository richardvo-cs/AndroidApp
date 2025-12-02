package com.example.fitnessprojectneal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final List<Conversation> conversationList;
    private final Context context;

    public ConversationAdapter(Context context, List<Conversation> conversationList) {
        this.context = context;
        this.conversationList = conversationList;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, lastMessageText, timeText;
        ImageView unreadBadge;

        ConversationViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_view_conversation_name);
            lastMessageText = itemView.findViewById(R.id.text_view_last_message);
            timeText = itemView.findViewById(R.id.text_view_conversation_time);
            unreadBadge = itemView.findViewById(R.id.image_view_unread_badge);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Conversation conversation = conversationList.get(position);
                    Intent intent = new Intent(context, MessagesActivity.class);
                    intent.putExtra("CONVERSATION_NAME", conversation.getName());
                    context.startActivity(intent);
                }
            });
        }

        void bind(Conversation conversation) {
            nameText.setText(conversation.getName());
            lastMessageText.setText(conversation.getLastMessage());
            timeText.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(conversation.getTimestamp()));
            if (conversation.hasUnreadMessages()) {
                unreadBadge.setVisibility(View.VISIBLE);
            } else {
                unreadBadge.setVisibility(View.GONE);
            }
        }
    }
}
