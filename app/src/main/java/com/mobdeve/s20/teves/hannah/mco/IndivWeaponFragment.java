package com.mobdeve.s20.teves.hannah.mco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IndivWeaponFragment extends Fragment {

    private TextView nameHolder;
    private ImageView imgHolder;
    private Button btnClose;

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
        imgHolder = view.findViewById(R.id.weaponImg);
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
            getWeaponDataByName(weaponName, charData -> {
                if (charData != null) {
                    displayWeaponData(charData);
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
                    .commit();
        });

        return view;
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
