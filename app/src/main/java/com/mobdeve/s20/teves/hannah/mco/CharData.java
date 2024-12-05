package com.mobdeve.s20.teves.hannah.mco;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CharData {
    String name;
    String charImgUrl; // Changed to String for URL images
    String charElementType;
    String charWeaponType;
    String charDescription;
    Map<String, Integer> ascensionRequirements;
    int ascensionMora;
    List<String> bestArtifactSets;
    List<String> bestWeapons;
    String skillPrio;
    boolean isFavorite;

    public CharData(String name,
                    String charImgUrl,
                    String charElementType,
                    String charWeaponType,
                    String charDescription,
                    Map<String, Integer> ascensionRequirements,
                    int ascensionMora,
                    List<String> bestArtifactSets,
                    List<String> bestWeapons,
                    String skillPrio,
                    boolean isFavorite) {
        this.name = name;
        this.charImgUrl = charImgUrl;
        this.charElementType = charElementType;
        this.charWeaponType = charWeaponType;
        this.charDescription = charDescription;
        this.ascensionRequirements = ascensionRequirements;
        this.ascensionMora = ascensionMora;
        this.bestArtifactSets = bestArtifactSets;
        this.bestWeapons = bestWeapons;
        this.skillPrio = skillPrio;
        this.isFavorite = isFavorite;
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    // Getter for charImgUrl
    public String getCharImgUrl() {
        return this.charImgUrl;
    }

    // Getter for charElementType
    public String getCharElementType() {
        return this.charElementType;
    }

    // Getter for charWeaponType
    public String getCharWeaponType() {
        return this.charWeaponType;
    }

    // Getter for charDescription
    public String getCharDescription() {
        return this.charDescription;
    }

    public boolean getIsFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public interface CharacterDataCallback {
        void onCharacterDataFetched(List<CharData> charDataList);
    }

    // Static method to fetch and parse ascension materials
    private static Map<String, Integer> getAscensionMaterials(JSONObject costs) throws JSONException {
        Map<String, Integer> ascensionRequirements = new LinkedHashMap<>();
        String[] ascensionLevels = {"ascend1", "ascend2", "ascend3", "ascend4", "ascend5", "ascend6"};

        for (String level : ascensionLevels) {
            JSONArray ascendArray = costs.getJSONArray(level);
            for (int i = 0; i < ascendArray.length(); i++) {
                JSONObject material = ascendArray.getJSONObject(i);
                String materialName = material.getString("name");
                int materialCount = material.getInt("count");
                ascensionRequirements.put(materialName, ascensionRequirements.getOrDefault(materialName, 0) + materialCount);
            }
        }
        return ascensionRequirements;
    }

    // Static method to fetch character data and add it to the list
    public static void getCharacterData(final Context context, final CharacterDataCallback callback) {
        List<CharData> charList = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        db.collection("characters").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> charNames = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    charNames.add(document.getId());
                }

                for (String charName : charNames) {
                    String apiUrl = "https://genshin-db-api.vercel.app/api/v5/characters?query=" + charName;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                            response -> {
                                try {
                                    String name = response.getString("name");
                                    String charImgUrl = response.getJSONObject("images").getString("hoyolab-avatar");
                                    String charElementType = response.getString("elementText");
                                    String charWeaponType = response.getString("weaponText");
                                    String charDescription = response.getString("description");

                                    Map<String, Integer> ascensionRequirements = getAscensionMaterials(response.getJSONObject("costs"));

                                    db.collection("characters").document(charName).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    List<String> bestArtifactSets = (List<String>) documentSnapshot.get("bestArtifactSets");
                                                    List<String> bestWeapons = (List<String>) documentSnapshot.get("bestWeapons");
                                                    String skillPrio = (String) documentSnapshot.get("skillPrio");
                                                    boolean isFavorite = (boolean) documentSnapshot.get("isFavorite");

                                                    CharData charData = new CharData(
                                                            name,
                                                            charImgUrl,
                                                            charElementType,
                                                            charWeaponType,
                                                            charDescription,
                                                            ascensionRequirements,
                                                            0,
                                                            bestArtifactSets,
                                                            bestWeapons,
                                                            skillPrio,
                                                            isFavorite
                                                    );

                                                    charList.add(charData);

                                                    if (charList.size() == charNames.size()) {
                                                        charList.sort(Comparator.comparing(CharData::getIsFavorite).reversed()
                                                                .thenComparing(CharData::getName));
                                                        callback.onCharacterDataFetched(charList);
                                                    }
                                                } else {
                                                    Log.e("Firebase", "No such document");
                                                }
                                            })
                                            .addOnFailureListener(e -> Log.e("Firebase", "Error fetching document", e));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("CharData", "Failed to parse character data", e);
                                }
                            },
                            error -> Log.e("VolleyError", error.getMessage(), error));
                    requestQueue.add(request);
                }
            } else {
                Log.e("Firebase", "Error getting documents: ", task.getException());
            }
        });
    }
}