package com.example.fitnessprojectneal;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Quest implements Serializable {
    public String id;
    public String title;
    public Date dueDate;
    public QuestDifficulty difficulty;
    public QuestType type;

    public int baseXp;
    public int progress;        // simple 0–100 progress
    public boolean isFinished;
    public boolean isSuccess;   // only meaningful when isFinished = true

    public Quest(String title,
                 Date dueDate,
                 QuestDifficulty difficulty,
                 QuestType type,
                 int baseXp) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.dueDate = dueDate;
        this.difficulty = difficulty;
        this.type = type;
        this.baseXp = baseXp;
        this.progress = 0;
        this.isFinished = false;
        this.isSuccess = false;
    }
}
