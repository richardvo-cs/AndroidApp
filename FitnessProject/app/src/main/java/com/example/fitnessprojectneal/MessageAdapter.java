package com.example.fitnessprojectneal;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    private static final int VIEW_TYPE_ATTACHED_QUEST = 3;

    private final List<Object> itemList;

    public MessageAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemList.get(position);
        if (item instanceof Message) {
            if (((Message) item).isSentByUser()) {
                return VIEW_TYPE_MESSAGE_SENT;
            } else {
                return VIEW_TYPE_MESSAGE_RECEIVED;
            }
        } else if (item instanceof Quest) {
            return VIEW_TYPE_ATTACHED_QUEST;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_attached_quest, parent, false);
            return new QuestViewHolder(view, this, itemList);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = itemList.get(position);
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind((Message) item);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind((Message) item);
                break;
            case VIEW_TYPE_ATTACHED_QUEST:
                ((QuestViewHolder) holder).bind((Quest) item);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_view_message_body);
            timeText = itemView.findViewById(R.id.text_view_message_time);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            timeText.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.getTimestamp()));
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_view_message_body);
            timeText = itemView.findViewById(R.id.text_view_message_time);
            nameText = itemView.findViewById(R.id.text_view_sender_name);
        }

        void bind(Message message) {
            messageText.setText(message.getText());
            timeText.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.getTimestamp()));
            // nameText.setText(message.getSender().getName());
        }
    }

    private static class QuestViewHolder extends RecyclerView.ViewHolder {
        TextView questTitle, questDetails;
        Button updateProgress, markComplete;
        private final MessageAdapter adapter;
        private final List<Object> itemList;

        QuestViewHolder(View itemView, MessageAdapter adapter, List<Object> itemList) {
            super(itemView);
            this.adapter = adapter;
            this.itemList = itemList;
            questTitle = itemView.findViewById(R.id.text_view_quest_title);
            questDetails = itemView.findViewById(R.id.text_view_quest_details);
            updateProgress = itemView.findViewById(R.id.button_update_progress);
            markComplete = itemView.findViewById(R.id.button_mark_complete);
        }

        void bind(Quest quest) {
            questTitle.setText(quest.title);
            questDetails.setText("Difficulty: " + quest.difficulty + ", XP: " + quest.baseXp + ", Progress: " + quest.progress + "%");

            updateProgress.setOnClickListener(v -> showUpdateProgressDialog(v.getContext(), quest, getAdapterPosition()));
            markComplete.setOnClickListener(v -> showMarkCompleteDialog(v.getContext(), quest, getAdapterPosition()));

            if (quest.isFinished) {
                markComplete.setEnabled(false);
                markComplete.setText("Completed");
                updateProgress.setEnabled(false);
            } else {
                markComplete.setEnabled(true);
                markComplete.setText("Mark Complete");
                updateProgress.setEnabled(true);
            }
        }

        private void showUpdateProgressDialog(Context context, Quest quest, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_update_quest_progress, null);
            EditText editTextProgress = dialogView.findViewById(R.id.edit_text_progress);

            builder.setView(dialogView)
                    .setPositiveButton("Update", (dialog, which) -> {
                        String progressString = editTextProgress.getText().toString();
                        if (!progressString.isEmpty()) {
                            int progress = Integer.parseInt(progressString);
                            if (progress >= 0 && progress <= 100) {
                                quest.progress = progress;
                                if (progress == 100) {
                                    quest.isFinished = true;
                                    quest.isSuccess = true;
                                    if (GameState.player != null) {
                                        GameState.player.currentXp += quest.baseXp;
                                    }
                                }
                                adapter.notifyItemChanged(position);
                            } else {
                                Toast.makeText(context, "Progress must be between 0 and 100", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

        private void showMarkCompleteDialog(Context context, Quest quest, int position) {
            new AlertDialog.Builder(context)
                    .setTitle("Mark Quest Complete?")
                    .setMessage("This will mark the quest as complete and award XP.")
                    .setPositiveButton("Complete", (dialog, which) -> {
                        quest.isFinished = true;
                        quest.isSuccess = true;
                        quest.progress = 100;
                        if (GameState.player != null) {
                            GameState.player.currentXp += quest.baseXp;
                        }
                        adapter.notifyItemChanged(position);
                        Toast.makeText(context, "Quest Complete!", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
}
