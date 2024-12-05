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

    public CharData(String name,
                    String charImgUrl,
                    String charElementType,
                    String charWeaponType,
                    String charDescription,
                    Map<String, Integer> ascensionRequirements,
                    int ascensionMora,
                    List<String> bestArtifactSets,
                    List<String> bestWeapons,
                    String skillPrio) {
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

        // Initialize Volley RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Fetch the list of document names under the characters collection
        db.collection("characters").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> charNames = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    charNames.add(document.getId());
                }

                // Fetch character data for each document name
                for (String charName : charNames) {
                    String apiUrl = "https://genshin-db-api.vercel.app/api/v5/characters?query=" + charName;

                    // Create the JsonObjectRequest for fetching the character data
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Parse the JSON response for character data
                                        String name = response.getString("name");
                                        String charImgUrl = response.getJSONObject("images").getString("hoyolab-avatar");
                                        String charElementType = response.getString("elementText");
                                        String charWeaponType = response.getString("weaponText");
                                        String charDescription = response.getString("description");

                                        // Fetch and parse ascension materials
                                        Map<String, Integer> ascensionRequirements = getAscensionMaterials(response.getJSONObject("costs"));

                                        // Firestore query to fetch other character data
                                        db.collection("characters").document(charName).get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    if (documentSnapshot.exists()) {
                                                        // Parse the data from Firestore
                                                        List<String> bestArtifactSets = (List<String>) documentSnapshot.get("bestArtifactSets");
                                                        List<String> bestWeapons = (List<String>) documentSnapshot.get("bestWeapons");
                                                        String skillPrio = (String) documentSnapshot.get("skillPrio");

                                                        // Create a CharData object with the combined data
                                                        CharData charData = new CharData(
                                                                name,
                                                                charImgUrl,
                                                                charElementType,
                                                                charWeaponType,
                                                                charDescription,
                                                                ascensionRequirements,
                                                                0, // Placeholder for ascensionMora
                                                                bestArtifactSets,
                                                                bestWeapons,
                                                                skillPrio
                                                        );

                                                        // Add the character data to the list
                                                        charList.add(charData);

                                                        // Check if all character data has been fetched
                                                        if (charList.size() == charNames.size()) {
                                                            // Sort the list by character name
                                                            charList.sort(Comparator.comparing(CharData::getName));
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
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("VolleyError", error.getMessage(), error);
                                }
                            });
                    // Add the request to the Volley request queue
                    requestQueue.add(request);
                }
            } else {
                Log.e("Firebase", "Error getting documents: ", task.getException());
            }
        });
    }
}