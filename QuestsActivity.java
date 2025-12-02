package com.example.fitnessprojectneal;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuestsActivity extends AppCompatActivity
        implements QuestAdapter.OnQuestClickedListener {

    private RecyclerView rvQuests;
    private TextView tvEmpty;
    private QuestAdapter adapter;
    private FloatingActionButton fabAddQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quests);

        rvQuests = findViewById(R.id.rvQuests);
        tvEmpty = findViewById(R.id.tvEmptyQuests);
        fabAddQuest = findViewById(R.id.fabAddQuest);

        Button btnBack = findViewById(R.id.btnBackQuests);
        btnBack.setOnClickListener(v -> finish());

        rvQuests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new QuestAdapter(this, this);
        rvQuests.setAdapter(adapter);

        fabAddQuest.setOnClickListener(v -> showAddQuestDialog());
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<Quest> sorted = GameState.getSortedQuests();
        adapter.setQuests(sorted);

        if (sorted.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvQuests.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvQuests.setVisibility(View.VISIBLE);
        }
    }

    // ---- Add new quest view (modal) ----

    private void showAddQuestDialog() {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_quest, null, false);

        EditText etTitle = view.findViewById(R.id.etQuestTitle);
        DatePicker dpDueDate = view.findViewById(R.id.dpDueDate);
        RadioGroup rgDifficulty = view.findViewById(R.id.rgDifficulty);
        RadioGroup rgType = view.findViewById(R.id.rgType);
        EditText etBaseXp = view.findViewById(R.id.etBaseXp);

        // Clamp to future dates: minDate = today
        dpDueDate.setMinDate(System.currentTimeMillis());

        new AlertDialog.Builder(this)
                .setTitle("New Quest")
                .setView(view)
                .setPositiveButton("Create", (dialog, which) -> {

                    String title = etTitle.getText().toString().trim();
                    if (title.isEmpty()) {
                        Toast.makeText(this, "Title is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int day = dpDueDate.getDayOfMonth();
                    int month = dpDueDate.getMonth();
                    int year = dpDueDate.getYear();
                    Calendar cal = Calendar.getInstance();
                    cal.set(year, month, day, 23, 59, 59);
                    Date dueDate = cal.getTime();

                    // Difficulty mapping using if/else
                    int difficultyId = rgDifficulty.getCheckedRadioButtonId();
                    QuestDifficulty difficulty;
                    if (difficultyId == R.id.rbHard) {
                        difficulty = QuestDifficulty.HARD;
                    } else if (difficultyId == R.id.rbLegendary) {
                        difficulty = QuestDifficulty.LEGENDARY;
                    } else {
                        difficulty = QuestDifficulty.NORMAL;
                    }

                    // Type mapping using if/else
                    int typeId = rgType.getCheckedRadioButtonId();
                    QuestType type;
                    if (typeId == R.id.rbOther) {
                        type = QuestType.OTHER;
                    } else {
                        type = QuestType.STEP_GOAL;
                    }

                    int baseXp = 50;
                    try {
                        baseXp = Integer.parseInt(etBaseXp.getText().toString());
                    } catch (Exception ignored) {}

                    Quest quest = new Quest(title, dueDate, difficulty, type, baseXp);
                    GameState.addQuest(quest);
                    GameState.updateCurrentQuest();
                    refreshList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---- Update quest (progress / complete) ----

    @Override
    public void onQuestClicked(Quest quest) {
        showUpdateQuestDialog(quest);
    }

    private void showUpdateQuestDialog(Quest quest) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_update_quest, null, false);

        TextView tvInfo = view.findViewById(R.id.tvQuestUpdateInfo);
        SeekBar sbProgress = view.findViewById(R.id.sbQuestProgress);
        TextView tvProgressValue = view.findViewById(R.id.tvProgressValue);
        CheckBox cbComplete = view.findViewById(R.id.cbCompleteQuest);

        tvInfo.setText(quest.title);
        sbProgress.setProgress(quest.progress);
        tvProgressValue.setText(quest.progress + "%");

        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvProgressValue.setText(progress + "%");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        new AlertDialog.Builder(this)
                .setTitle("Update Quest")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {

                    quest.progress = sbProgress.getProgress();

                    if (cbComplete.isChecked() && !quest.isFinished) {
                        // Determine success / failure based on due date
                        boolean success = true;
                        Date now = new Date();
                        if (quest.dueDate != null && quest.dueDate.before(now)) {
                            success = false;
                        }

                        GameState.QuestCompletionResult result =
                                GameState.completeQuest(quest, success);

                        GameState.updateCurrentQuest();
                        showQuestResultDialog(result);
                    }

                    refreshList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---- Build share text (same style as Profile) ----

    private String buildShareText() {
        Player p = GameState.player;

        return "My Fitness RPG progress:\n\n" +
                "Name: " + p.name + "\n" +
                "Level: " + p.level + "\n" +
                "XP: " + p.currentXp + "/" + p.xpToNextLevel + "\n" +
                "Completed quests: " + GameState.getCompletedQuestCount() + "\n" +
                "Current quest: " + p.currentQuestTitle + "\n\n" +
                "#FitnessRPG";
    }

    // ---- Finished Quest View (success / failure) ----

    private void showQuestResultDialog(GameState.QuestCompletionResult result) {
        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_quest_result, null, false);

        TextView tvTitle = view.findViewById(R.id.tvQuestResultTitle);
        TextView tvBody = view.findViewById(R.id.tvQuestResultBody);
        Button btnShare = view.findViewById(R.id.btnShareQuestResult);

        if (result.success) {
            String text = "Quest Success!\n\n" +
                    "XP gained: " + result.xpGained + "\n" +
                    "Level: " + GameState.player.level +
                    "  (XP: " + GameState.player.currentXp +
                    "/" + GameState.player.xpToNextLevel + ")\n";

            if (result.cosmeticReward != null) {
                text += "\nYou earned a new cosmetic: " +
                        result.cosmeticReward.id + " (" +
                        result.cosmeticReward.category + ")";
            } else {
                text += "\nNo cosmetic this time. Keep it up!";
            }

            tvTitle.setText("Quest Success");
            tvBody.setText(text);
        } else {
            String text = "Quest Failed.\n\n" +
                    result.encouragementText +
                    "\n\nBonus XP multiplier applied for your next quest!";

            tvTitle.setText("Quest Failure");
            tvBody.setText(text);
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", null)
                .create();

        btnShare.setOnClickListener(v -> {
            String shareText = buildShareText();
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(sendIntent, "Share Achievement"));
        });

        dialog.show();
    }
}
