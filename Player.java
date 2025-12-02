package com.example.fitnessprojectneal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Player {

    public String name = "Player";
    public int level = 1;
    public int currentXp = 0;
    public int xpToNextLevel = 100;

    // Cosmetics inventory
    public List<CosmeticItem> inventory;
    public HashMap<String, String> equippedCosmetics;

    // Current quest display info
    public String currentQuestTitle;
    public String currentQuestDueDate;

    public Player() {
        // Initialize collections
        inventory = new ArrayList<>();
        equippedCosmetics = new HashMap<>();

        // Initialize quest info
        currentQuestTitle = "None";
        currentQuestDueDate = "";

        // ---- Default starter cosmetics ----
        CosmeticItem starterHead = new CosmeticItem("starter_headband", "Head");
        CosmeticItem starterBody = new CosmeticItem("starter_training_shirt", "Body");
        CosmeticItem starterFeet = new CosmeticItem("starter_running_shoes", "Feet");

        // Add to inventory
        inventory.add(starterHead);
        inventory.add(starterBody);
        inventory.add(starterFeet);

        // Equip them by default
        equippedCosmetics.put(starterHead.category, starterHead.id);
        equippedCosmetics.put(starterBody.category, starterBody.id);
        equippedCosmetics.put(starterFeet.category, starterFeet.id);
    }
}
