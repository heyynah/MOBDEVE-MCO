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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeaponData {
    String name;
    String weaponImg;
    Map<String, Integer> refineRequirements;

    // Optimized constructor
    public WeaponData(String name, String weaponImg, Map<String, Integer> refineRequirements) {
        this.name = name;
        this.weaponImg = weaponImg;
        this.refineRequirements = refineRequirements;
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    public String getWeaponImgUrl() {
        return this.weaponImg;
    }

    public interface WeaponDataCallback {
        void onWeaponDataFetched(List<WeaponData> weaponDataList);
    }

    // Method to get a formatted string of refinement materials
    public static Map<String, Integer> getRefineMaterials(JSONObject costs) throws JSONException {
        Map<String, Integer> refinementRequirements = new LinkedHashMap<>();
        String[] ascensionLevels = {"ascend1", "ascend2", "ascend3", "ascend4", "ascend5", "ascend6"};

        for (String level : ascensionLevels) {
            JSONArray ascendArray = costs.getJSONArray(level);
            for (int i = 0; i < ascendArray.length(); i++) {
                JSONObject material = ascendArray.getJSONObject(i);
                String materialName = material.getString("name");
                int materialCount = material.getInt("count");
                refinementRequirements.put(materialName, refinementRequirements.getOrDefault(materialName, 0) + materialCount);
            }
        }
        return refinementRequirements;
    }

    // Fetch weapon data from Firebase and API
    public static void getWeaponData(final Context context, final WeaponDataCallback callback) {
        List<WeaponData> weaponList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Fetch the list of document names under the weapons collection
        db.collection("weapons").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> weaponNames = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    weaponNames.add(document.getId());
                }

                // Fetch weapon data for each document name
                for (String weaponName : weaponNames) {
                    String apiUrl = "https://genshin-db-api.vercel.app/api/v5/weapons?query=" + weaponName;

                    // Create the JsonObjectRequest for fetching the weapon data
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        // Parse the JSON response for weapon data
                                        String name = response.getString("name");
                                        String weaponImg = response.getJSONObject("images").getString("icon");

                                        // Fetch and parse refinement materials
                                        Map<String, Integer> refinementRequirements = getRefineMaterials(response.getJSONObject("costs"));

                                        // Firestore query to fetch other weapon data
                                        db.collection("weapons").document(weaponName).get()
                                                .addOnSuccessListener(documentSnapshot -> {
                                                    if (documentSnapshot.exists()) {
                                                        // Create a WeaponData object with the combined data
                                                        WeaponData weaponData = new WeaponData(
                                                                name,
                                                                weaponImg,
                                                                refinementRequirements
                                                        );

                                                        // Add the weapon data to the list
                                                        weaponList.add(weaponData);

                                                        // Check if all weapon data has been fetched
                                                        if (weaponList.size() == weaponNames.size()) {
                                                            // Sort the list by weapon name
                                                            weaponList.sort(Comparator.comparing(WeaponData::getName));
                                                            callback.onWeaponDataFetched(weaponList);
                                                        }
                                                    } else {
                                                        Log.e("Firebase", "No such document");
                                                    }
                                                })
                                                .addOnFailureListener(e -> Log.e("Firebase", "Error fetching document", e));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.e("WeaponData", "Failed to parse weapon data", e);
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