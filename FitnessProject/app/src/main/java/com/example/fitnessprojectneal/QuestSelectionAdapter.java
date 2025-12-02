package com.example.fitnessprojectneal;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuestSelectionAdapter extends RecyclerView.Adapter<QuestSelectionAdapter.QuestViewHolder> {

    private final List<Quest> quests;

    public QuestSelectionAdapter(List<Quest> quests) {
        this.quests = quests;
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quest_selection, parent, false);
        return new QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.bind(quest);
        holder.itemView.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("SELECTED_QUEST", quest);
            ((Activity) v.getContext()).setResult(Activity.RESULT_OK, resultIntent);
            ((Activity) v.getContext()).finish();
        });
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    static class QuestViewHolder extends RecyclerView.ViewHolder {
        TextView questTitle;
        TextView questDetails;

        public QuestViewHolder(@NonNull View itemView) {
            super(itemView);
            questTitle = itemView.findViewById(R.id.text_view_quest_title);
            questDetails = itemView.findViewById(R.id.text_view_quest_details);
        }

        void bind(Quest quest) {
            questTitle.setText(quest.title);
            questDetails.setText("Difficulty: " + quest.difficulty + ", XP: " + quest.baseXp);
        }
    }
}
