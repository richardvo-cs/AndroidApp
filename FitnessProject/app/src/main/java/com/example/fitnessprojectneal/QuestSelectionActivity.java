package com.example.fitnessprojectneal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class QuestSelectionActivity extends AppCompatActivity {

    private RecyclerView recyclerViewQuests;
    private QuestSelectionAdapter questSelectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_selection);

        recyclerViewQuests = findViewById(R.id.recycler_view_quests);

        // TODO: Get the real list of quests
        questSelectionAdapter = new QuestSelectionAdapter(new ArrayList<>());

        recyclerViewQuests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuests.setAdapter(questSelectionAdapter);
    }
}
