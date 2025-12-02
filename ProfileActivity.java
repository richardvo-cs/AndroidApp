package com.example.fitnessprojectneal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvLevel, tvXP, tvQuest;
    private ImageView ivAvatar;
    private Button btnEditProfile, btnInventory, btnMessages, btnQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvLevel = findViewById(R.id.tvLevel);
        tvXP = findViewById(R.id.tvXP);
        tvQuest = findViewById(R.id.tvQuest);
        ivAvatar = findViewById(R.id.ivAvatar);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnInventory = findViewById(R.id.btnInventory);
        btnMessages = findViewById(R.id.btnMessages);
        btnQuests = findViewById(R.id.btnQuests);

        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileEditActivity.class));
        });

        btnInventory.setOnClickListener(v -> {
            startActivity(new Intent(this, InventoryActivity.class));
        });

        // TODO: Messages & Quests Activity navigation

        updateUI();
    }

    private void updateUI() {
        Player player = GameState.player;
        tvName.setText("Name: " + player.name);
        tvLevel.setText("Level: " + player.level);
        tvXP.setText("XP: " + player.currentXp + "/" + player.xpToNextLevel);
        tvQuest.setText("Quest: " + player.currentQuestTitle + " (Due: " + player.currentQuestDueDate + ")");
        // TODO: Set avatar image based on player.avatarId
    }
}
