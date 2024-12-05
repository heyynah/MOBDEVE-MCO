package com.mobdeve.s20.teves.hannah.mco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndivWeaponFragment extends Fragment {

    private WeaponData weaponData;

    private TextView nameHolder;
    private ImageButton favButton;
    private ImageView imgHolder;
    private Button btnClose;
    private TextView weaponType;
    private TextView weaponDescription;
    private ImageView refineArrow;
    private LinearLayout refineMaterialsSection;
    private RecyclerView recyclerView;
    private RefineMaterialAdapter adapter;
    private List<RefineMaterialData> refineMaterialList;

    private boolean isRefineSectionVisible = false; // Track visibility state

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weapon_indiv, container, false);

        // Initialize Views
        nameHolder = view.findViewById(R.id.weaponName);
        favButton = view.findViewById(R.id.favButton);
        imgHolder = view.findViewById(R.id.weaponImg);
        weaponType = view.findViewById(R.id.weaponType);
        weaponDescription = view.findViewById(R.id.weaponDesc);
        btnClose = view.findViewById(R.id.btn_close);
        refineArrow = view.findViewById(R.id.refineArrow);
        refineMaterialsSection = view.findViewById(R.id.refineMaterialsSection);
        recyclerView = view.findViewById(R.id.refineRecyclerView);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Get weapon data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String weaponName = args.getString("WEAPON_NAME");
            getWeaponDataByName(weaponName, weaponData -> {
                if (weaponData != null) {
                    this.weaponData = weaponData;
                    displayWeaponData(weaponData);
                }
            });
        }

        // Set OnClickListener to toggle visibility of refine materials
        refineArrow.setOnClickListener(v -> toggleRefineMaterialsVisibility());

        // Set close button functionality
        btnClose.setOnClickListener(v -> {
            WeaponFragment weaponFragment = new WeaponFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, weaponFragment)
                    .addToBackStack(null)
                    .commit();
        });

        favButton.setOnClickListener(v -> {
            if (weaponData != null) {
                boolean isFavorite = !weaponData.getIsFavorite();
                weaponData.setIsFavorite(isFavorite);
                updateFavoriteButtonState(isFavorite); // Update the button's appearance immediately
                updateFavoriteInFirestore(weaponData.getName().toLowerCase(), isFavorite);
            }
        });
        return view;
    }

    private void updateFavoriteInFirestore(String weaponName, boolean isFavorite) {
        // Make the weaponName lowercase to match the document ID in Firestore and remove spaces and special characters
        weaponName = weaponName.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("weapons").document(weaponName)
                .update("isFavorite", isFavorite)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Favorite status updated.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update favorite status.", Toast.LENGTH_SHORT).show();
                    // Revert the UI change in case of failure
                    weaponData.setIsFavorite(!isFavorite);
                    updateFavoriteButtonState(!isFavorite);
                });
    }

    private void updateFavoriteButtonState(boolean isFavorite) {
        if (isFavorite) {
            favButton.setImageResource(R.drawable.ic_star);
        } else {
            favButton.setImageResource(R.drawable.ic_star_border);
        }
    }

    private void toggleRefineMaterialsVisibility() {
        if (isRefineSectionVisible) {
            // Hide the refine materials section
            refineMaterialsSection.setVisibility(View.GONE);
            refineArrow.setImageResource(R.drawable.ic_arrow_down); // Arrow points down when collapsed
        } else {
            // Show the refine materials section
            refineMaterialsSection.setVisibility(View.VISIBLE);
            refineArrow.setImageResource(R.drawable.ic_arrow_up); // Arrow points up when expanded
        }
        isRefineSectionVisible = !isRefineSectionVisible; // Toggle state
    }

    public interface WeaponDataCallback {
        void onWeaponDataFetched(WeaponData weaponData);
    }

    private void getWeaponDataByName(String weaponName, WeaponDataCallback callback) {
        WeaponData.getWeaponData(requireContext(), weaponDataList -> {
            for (WeaponData data : weaponDataList) {
                if (data.name.equals(weaponName)) {
                    callback.onWeaponDataFetched(data);
                    return;
                }
            }
            callback.onWeaponDataFetched(null);
        });
    }

    private void displayWeaponData(WeaponData weaponData) {
        if (weaponData != null) {
            nameHolder.setText(weaponData.name);
            updateFavoriteButtonState(weaponData.getIsFavorite());
            weaponType.setText(weaponData.getWeaponType());
            weaponDescription.setText(weaponData.getWeaponDescription());
            Picasso.get()
                    .load(weaponData.getWeaponImgUrl()) // Pass the URL string
                    .error(R.drawable.ic_character_lumine) // Optional: Image to show on error
                    .into(imgHolder);

            // Convert refine requirements to list of RefineMaterial
            refineMaterialList = new ArrayList<>();

            // Add refine requirements from the map
            for (Map.Entry<String, Integer> entry : weaponData.refineRequirements.entrySet()) {
                refineMaterialList.add(new RefineMaterialData(entry.getKey(), entry.getValue()));
            }

            // Initialize and set the adapter
            adapter = new RefineMaterialAdapter(refineMaterialList);
            recyclerView.setAdapter(adapter);
        }
    }
}