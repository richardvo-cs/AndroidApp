package com.example.fitnessprojectneal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class GameState {

    public static Player player = new Player();

    // All quests in memory
    public static final List<Quest> quests = new ArrayList<>();

    // Failure XP multiplier (for next success after a failure)
    public static int failureXpMultiplier = 1;

    // 1 / COSMETIC_ROLL_DENOMINATOR chance to get a cosmetic
    private static final int COSMETIC_ROLL_DENOMINATOR = 5;

    private static final String[] ENCOURAGING_PHRASES = new String[]{
            "Every step counts. You’ve got this!",
            "Progress, not perfection. Keep going!",
            "You’re building a stronger you every day.",
            "Setbacks are just setups for comebacks.",
            "You showed up. That matters more than you think."
    };

    // Simple date format for displaying quest due dates
    private static final SimpleDateFormat QUEST_DATE_FORMAT =
            new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static int getCompletedQuestCount() {
        int count = 0;
        for (Quest q : quests) {
            if (q.isFinished && q.isSuccess) {
                count++;
            }
        }
        return count;
    }

    public static List<Quest> getSortedQuests() {
        List<Quest> copy = new ArrayList<>(quests);
        Collections.sort(copy, new Comparator<Quest>() {
            @Override
            public int compare(Quest o1, Quest o2) {
                if (o1.dueDate == null && o2.dueDate == null) return 0;
                if (o1.dueDate == null) return 1;
                if (o2.dueDate == null) return -1;
                return o1.dueDate.compareTo(o2.dueDate);
            }
        });
        return copy;
    }

    public static void addQuest(Quest quest) {
        quests.add(quest);
    }

    public static QuestCompletionResult completeQuest(Quest quest, boolean success) {
        quest.isFinished = true;
        quest.isSuccess = success;

        if (success) {
            int difficultyMultiplier = quest.difficulty.getXpMultiplier();
            int xpMultiplier = Math.max(difficultyMultiplier, failureXpMultiplier);
            int xpGained = quest.baseXp * xpMultiplier;

            applyXpToPlayer(player, xpGained);

            // reset failure multiplier on success
            failureXpMultiplier = 1;

            CosmeticItem reward = rollCosmeticReward();
            return new QuestCompletionResult(true, xpGained, reward, null);
        } else {
            // Quest failure -> set failure multiplier for next task
            failureXpMultiplier = QuestDifficulty.HARD.getXpMultiplier();
            String phrase = getRandomEncouragement();
            return new QuestCompletionResult(false, 0, null, phrase);
        }
    }

    private static void applyXpToPlayer(Player p, int xpGained) {
        p.currentXp += xpGained;
        while (p.currentXp >= p.xpToNextLevel) {
            p.currentXp -= p.xpToNextLevel;
            p.level++;
            // simple scaling, adjust if you already use something else
            p.xpToNextLevel = (int) (p.xpToNextLevel * 1.2f);
        }
    }

    private static CosmeticItem rollCosmeticReward() {
        Random r = new Random();
        if (r.nextInt(COSMETIC_ROLL_DENOMINATOR) == 0) {
            // simple placeholder cosmetic
            CosmeticItem item = new CosmeticItem("reward_hat_" + System.currentTimeMillis(), "Head");
            if (player.inventory != null) {
                player.inventory.add(item);
            }
            return item;
        }
        return null;
    }

    private static String getRandomEncouragement() {
        Random r = new Random();
        return ENCOURAGING_PHRASES[r.nextInt(ENCOURAGING_PHRASES.length)];
    }

    /**
     * Update player.currentQuestTitle and player.currentQuestDueDate
     * to the first unfinished quest (sorted by due date),
     * or "None" if there are no active quests.
     */
    public static void updateCurrentQuest() {
        List<Quest> sorted = getSortedQuests();
        for (Quest q : sorted) {
            if (!q.isFinished) {
                player.currentQuestTitle = q.title;
                if (q.dueDate != null) {
                    player.currentQuestDueDate = QUEST_DATE_FORMAT.format(q.dueDate);
                } else {
                    player.currentQuestDueDate = "N/A";
                }
                return;
            }
        }

        // No unfinished quests
        player.currentQuestTitle = "None";
        player.currentQuestDueDate = "";
    }

    // Small helper type to describe quest completion result
    public static class QuestCompletionResult {
        public final boolean success;
        public final int xpGained;
        public final CosmeticItem cosmeticReward; // null if none
        public final String encouragementText;    // non-null only when failure

        public QuestCompletionResult(boolean success,
                                     int xpGained,
                                     CosmeticItem cosmeticReward,
                                     String encouragementText) {
            this.success = success;
            this.xpGained = xpGained;
            this.cosmeticReward = cosmeticReward;
            this.encouragementText = encouragementText;
        }
    }
}
