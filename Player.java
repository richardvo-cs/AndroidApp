package com.example.fitnessprojectneal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    public String name;
    public int level;
    public int currentXp;
    public int xpToNextLevel;
    public String currentQuestTitle;
    public String currentQuestDueDate;
    public String avatarId;
    public Map<String, String> equippedCosmetics; // category -> cosmetic id
    public List<CosmeticItem> inventory;

    public Player() {
        this.name = "Player1";
        this.level = 1;
        this.currentXp = 0;
        this.xpToNextLevel = 100;
        this.currentQuestTitle = "First Quest";
        this.currentQuestDueDate = "2025-12-31";
        this.avatarId = "avatar_default";
        this.equippedCosmetics = new HashMap<>();
        this.inventory = new ArrayList<>();
        // add some sample cosmetics
        inventory.add(new CosmeticItem("hat01", "Head"));
        inventory.add(new CosmeticItem("mask01", "Face"));
        inventory.add(new CosmeticItem("shirt01", "Torso"));
        inventory.add(new CosmeticItem("pants01", "Legs"));
        inventory.add(new CosmeticItem("boots01", "Feet"));
    }
}
