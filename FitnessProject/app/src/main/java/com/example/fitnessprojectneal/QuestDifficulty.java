package com.example.fitnessprojectneal;

public enum QuestDifficulty {
    NORMAL,
    HARD,
    LEGENDARY;

    public int getXpMultiplier() {
        switch (this) {
            case HARD: return 2;
            case LEGENDARY: return 3;
            default: return 1;
        }
    }
}
