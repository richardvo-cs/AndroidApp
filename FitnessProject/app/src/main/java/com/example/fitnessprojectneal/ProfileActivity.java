package com.example.fitnessprojectneal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvLevel, tvXP, tvQuest, tvCompletedQuests;
    private ImageView ivAvatar;
    private Button btnEditProfile, btnInventory, btnMessages, btnQuests, btnShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvLevel = findViewById(R.id.tvLevel);
        tvXP = findViewById(R.id.tvXP);
        tvQuest = findViewById(R.id.tvQuest);
        tvCompletedQuests = findViewById(R.id.tvCompletedQuests);
        ivAvatar = findViewById(R.id.ivAvatar);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnInventory = findViewById(R.id.btnInventory);
        btnMessages = findViewById(R.id.btnMessages);
        btnQuests = findViewById(R.id.btnQuests);
        btnShare = findViewById(R.id.btnShare);

        btnEditProfile.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, ProfileEditActivity.class)));

        btnInventory.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, InventoryActivity.class)));

        btnQuests.setOnClickListener(v ->
                startActivity(new Intent(ProfileActivity.this, QuestsActivity.class)));

        btnShare.setOnClickListener(v -> shareProfile());

        btnMessages.setOnClickListener(v -> 
                startActivity(new Intent(ProfileActivity.this, ConversationsActivity.class)));

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Player p = GameState.player;
        if (p == null) return;

        tvName.setText("Name: " + p.name);
        tvLevel.setText("Level: " + p.level);
        tvXP.setText("XP: " + p.currentXp + "/" + p.xpToNextLevel);

        tvCompletedQuests.setText("Completed quests: " + GameState.getCompletedQuestCount());

        String questTitle = (p.currentQuestTitle == null || p.currentQuestTitle.isEmpty())
                ? "None"
                : p.currentQuestTitle;
        String questDue = (p.currentQuestDueDate == null || p.currentQuestDueDate.isEmpty())
                ? "N/A"
                : p.currentQuestDueDate;

        tvQuest.setText("Current Quest: " + questTitle + " (Due: " + questDue + ")");

        // TODO: update avatar based on equipped cosmetics if desired
    }

    private void shareProfile() {
        Player p = GameState.player;

        String shareText =
                "My Fitness RPG profile:\n\n" +
                        "Name: " + p.name + "\n" +
                        "Level: " + p.level + "\n" +
                        "XP: " + p.currentXp + "/" + p.xpToNextLevel + "\n" +
                        "Completed quests: " + GameState.getCompletedQuestCount() + "\n" +
                        "Current quest: " + p.currentQuestTitle + "\n\n" +
                        "#FitnessRPG";

        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        startActivity(Intent.createChooser(sendIntent, "Share Profile"));
    }
}
