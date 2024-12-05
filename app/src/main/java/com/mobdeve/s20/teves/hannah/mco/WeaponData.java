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
    String weaponType;
    String weaponDescription;
    Map<String, Integer> refineRequirements;
    boolean isFavorite;

    // Optimized constructor
    public WeaponData(String name, String weaponImg, String weaponType, String weaponDescription,
                      Map<String, Integer> refineRequirements, boolean isFavorite) {
        this.name = name;
        this.weaponImg = weaponImg;
        this.weaponType = weaponType;
        this.weaponDescription = weaponDescription;
        this.refineRequirements = refineRequirements;
        this.isFavorite = isFavorite;
    }

    // Getter for name
    public String getName() {
        return this.name;
    }

    public String getWeaponImgUrl() {
        return this.weaponImg;
    }

    public String getWeaponType() {
        return this.weaponType;
    }

    public String getWeaponDescription() {
        return this.weaponDescription;
    }

    public boolean getIsFavorite() {
        return this.isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
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

        db.collection("weapons").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> weaponNames = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult()) {
                    weaponNames.add(document.getId());
                }

                for (String weaponName : weaponNames) {
                    String apiUrl = "https://genshin-db-api.vercel.app/api/v5/weapons?query=" + weaponName;

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                            response -> {
                                try {
                                    String name = response.getString("name");
                                    String weaponImgUrl = response.getJSONObject("images").getString("icon");
                                    String weaponType = response.getString("weaponText");
                                    String weaponDescription = response.getString("description");

                                    Map<String, Integer> refineRequirements = getRefineMaterials(response.getJSONObject("costs"));

                                    db.collection("weapons").document(weaponName).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    boolean isFavorite = (boolean) documentSnapshot.get("isFavorite");

                                                    WeaponData weaponData = new WeaponData(
                                                            name,
                                                            weaponImgUrl,
                                                            weaponType,
                                                            weaponDescription,
                                                            refineRequirements,
                                                            isFavorite
                                                    );

                                                    weaponList.add(weaponData);

                                                    if (weaponList.size() == weaponNames.size()) {
                                                        weaponList.sort(Comparator.comparing(WeaponData::getIsFavorite).reversed()
                                                                .thenComparing(WeaponData::getName));
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