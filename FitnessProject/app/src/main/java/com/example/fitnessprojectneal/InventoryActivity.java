package com.example.fitnessprojectneal;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private RecyclerView rvInventory;
    private Button btnSave, btnQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        rvInventory = findViewById(R.id.rvInventory);
        btnSave = findViewById(R.id.btnSave);
        btnQuit = findViewById(R.id.btnQuit);

        List<CosmeticItem> inventory = GameState.player.inventory;

        InventoryAdapter adapter = new InventoryAdapter(inventory, GameState.player);
        rvInventory.setLayoutManager(new GridLayoutManager(this, 3));
        rvInventory.setAdapter(adapter);

        btnSave.setOnClickListener(v -> finish());
        btnQuit.setOnClickListener(v -> finish());
    }
}
