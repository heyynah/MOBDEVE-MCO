package com.mobdeve.s20.teves.hannah.mco;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaponData {
    String name;
    int weaponImg;

    Map<String, Integer> refineRequirements;
    int refineMora;
    int refineCrystal;

    // Optimized constructor
    public WeaponData(String name,
                      int weaponImg,
                      Map<String, Integer> refineRequirements,
                      int refineMora,
                      int refineCrystal) {
        this.name = name;
        this.weaponImg = weaponImg;
        this.refineRequirements = refineRequirements;
        this.refineMora = refineMora;
        this.refineCrystal = refineCrystal;
    }

    public int getWeaponImg() {
        return weaponImg;
    }

    // Method to get a formatted string of refinement materials
    public String getRefineMaterials() {
        StringBuilder materials = new StringBuilder();
        for (Map.Entry<String, Integer> entry : refineRequirements.entrySet()) {
            materials.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        materials.append("Total Mora: ").append(refineMora).append("\n");
        materials.append("Total Crystals: ").append(refineCrystal).append("\n");
        return materials.toString();
    }

    // Sample data
    public static List<WeaponData> getWeaponData() {
        List<WeaponData> weaponList = new ArrayList<>();

        // Sample data for Aether
        Map<String, Integer> aetherRefineRequirements = new HashMap<>();
        aetherRefineRequirements.put("Silver", 5);
        aetherRefineRequirements.put("Crystal Chunk", 10);
        aetherRefineRequirements.put("Weapon Material 1", 2);
        aetherRefineRequirements.put("Weapon Material 2", 3);

        weaponList.add(new WeaponData("Primordial Jade Wing Spear", R.drawable.primordial,
                aetherRefineRequirements, 10000, 2000));

        // Sample data for Character 2
        Map<String, Integer> character2RefineRequirements = new HashMap<>();
        character2RefineRequirements.put("Silver", 3);
        character2RefineRequirements.put("Crystal Chunk", 5);
        character2RefineRequirements.put("Weapon Material 1", 1);
        character2RefineRequirements.put("Weapon Material 2", 2);

        weaponList.add(new WeaponData("Amos Bow", R.drawable.amos,
                character2RefineRequirements, 8000, 1500));

        // Sample data for Character 3
        Map<String, Integer> character3RefineRequirements = new HashMap<>();
        character3RefineRequirements.put("Silver", 4);
        character3RefineRequirements.put("Crystal Chunk", 8);
        character3RefineRequirements.put("Weapon Material 1", 2);
        character3RefineRequirements.put("Weapon Material 2", 1);

        weaponList.add(new WeaponData("Staff of Homa", R.drawable.homa,
                character3RefineRequirements, 9000, 1800));

        return weaponList;
    }
}
