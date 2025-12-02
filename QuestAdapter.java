package com.example.fitnessprojectneal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {

    public interface OnQuestClickedListener {
        void onQuestClicked(Quest quest);
    }

    private final List<Quest> quests = new ArrayList<>();
    private final OnQuestClickedListener listener;
    private final DateFormat dateFormat;
    private final Context context;

    public QuestAdapter(Context context, OnQuestClickedListener listener) {
        this.context = context;
        this.listener = listener;
        this.dateFormat = android.text.format.DateFormat.getDateFormat(context);
    }

    public void setQuests(List<Quest> newQuests) {
        quests.clear();
        quests.addAll(newQuests);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quest, parent, false);
        return new QuestViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.bind(quest, listener, dateFormat);
    }

    @Override
    public int getItemCount() {
        return quests.size();
    }

    static class QuestViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvInfo, tvStatus;
        Button btnProgress;

        public QuestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvQuestTitle);
            tvInfo = itemView.findViewById(R.id.tvQuestInfo);
            tvStatus = itemView.findViewById(R.id.tvQuestStatus);
            btnProgress = itemView.findViewById(R.id.btnQuestProgress);
        }

        public void bind(Quest quest,
                         OnQuestClickedListener listener,
                         DateFormat dateFormat) {

            tvTitle.setText(quest.title);

            String dueString = (quest.dueDate != null)
                    ? dateFormat.format(quest.dueDate)
                    : "No due date";

            String info = quest.difficulty.name() +
                    " • " + quest.type.name() +
                    " • Due: " + dueString +
                    " • XP: " + quest.baseXp;

            tvInfo.setText(info);

            String status;
            Date now = new Date();

            if (quest.isFinished) {
                status = quest.isSuccess ? "Completed ✔" : "Failed ✖";
            } else if (quest.dueDate != null && quest.dueDate.before(now)) {
                status = "Past due";
            } else {
                status = "In progress (" + quest.progress + "%)";
            }

            tvStatus.setText(status);

            btnProgress.setOnClickListener(v -> {
                if (listener != null) listener.onQuestClicked(quest);
            });
        }
    }
}
