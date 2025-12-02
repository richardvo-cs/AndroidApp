package com.example.fitnessprojectneal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvSummary;
    private Button btnProfile;
    private Button btnQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvSummary = findViewById(R.id.tvSummary);
        btnProfile = findViewById(R.id.btnProfile);
        btnQuests = findViewById(R.id.btnQuests);

        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, ProfileActivity.class)));

        btnQuests.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, QuestsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSummary();
    }

    private void updateSummary() {
        if (GameState.player == null) return;
        String text = "Welcome, " + GameState.player.name +
                "\nLevel: " + GameState.player.level +
                "\nCompleted Quests: " + GameState.getCompletedQuestCount();
        tvSummary.setText(text);
    }
}
