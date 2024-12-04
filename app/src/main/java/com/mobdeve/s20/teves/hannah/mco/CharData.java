package com.mobdeve.s20.teves.hannah.mco;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharData {
    String name;
    String charImgUrl; // Changed to String for URL images
    Map<String, Integer> ascensionRequirements;
    int ascensionMora;
    Map<String, Integer> talentRequirements;
    int talentMora;
    List<String> bestArtifactSets;
    List<String> bestWeapons;
    List<String> skillPrio;

    public CharData(String name,
                    String charImgUrl,
                    Map<String, Integer> ascensionRequirements,
                    int ascensionMora,
                    Map<String, Integer> talentRequirements,
                    int talentMora,
                    List<String> bestArtifactSets,
                    List<String> bestWeapons,
                    List<String> skillPrio) {
        this.name = name;
        this.charImgUrl = charImgUrl;
        this.ascensionRequirements = ascensionRequirements;
        this.ascensionMora = ascensionMora;
        this.talentRequirements = talentRequirements;
        this.talentMora = talentMora;
        this.bestArtifactSets = bestArtifactSets;
        this.bestWeapons = bestWeapons;
        this.skillPrio = skillPrio;
    }

    // Getter for charImgUrl
    public String getCharImgUrl() {
        return this.charImgUrl;
    }


    // Static method to fetch character data and add it to the list
    public static void getCharacterData(final List<CharData> charList, final Context context) {
        // sample data
        charList.add(new CharData("Aether", "https://uploadstatic-sea.mihoyo.com/contentweb/20210129/2021012910075996276.png", new HashMap<>(), 0, new HashMap<>(), 0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        // Sample list of character names
        List<String> charNames = new ArrayList<>();
        charNames.add("Xiao");
        charNames.add("Ganyu");
        charNames.add("Diluc");

        // Loop through the list of character names
        for (String charName : charNames) {

            String apiUrl = "https://genshin-db-api.vercel.app/api/v5/characters?query=" + charName;

            // Initialize Volley RequestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            // Create the JsonObjectRequest for fetching the character data
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Parse the JSON response for character data
                            String name = response.getString("name");
                            String charImgUrl = response.getJSONObject("images").getString("hoyolab-avatar");

                            // Parsing ascend1 material data
                            JSONArray ascend1Array = response.getJSONObject("costs").getJSONArray("ascend1");
                            Map<String, Integer> ascend1Map = new HashMap<>();
                            int moraCost = 0;

                            for (int i = 0; i < ascend1Array.length(); i++) {
                                JSONObject material = ascend1Array.getJSONObject(i);
                                String materialName = material.getString("name");
                                int materialCount = material.getInt("count");

                                if (materialName.equals("Mora")) {
                                    moraCost = materialCount;
                                } else {
                                    ascend1Map.put(materialName, materialCount);
                                }
                            }

                            // Add the character data to the list
                            charList.add(new CharData(name, charImgUrl, ascend1Map, moraCost, new HashMap<>(), 0,
                                    new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

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
    }
}
