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

public class IndivCharFragment extends Fragment {

    private TextView nameHolder;
    private ImageView imgHolder;
    private Button btnClose;
    private ImageView ascensionArrow, talentArrow, bestArtifactsArrow, bestWeaponsArrow, skillPriorityArrow;
    private LinearLayout ascensionMaterialHolder, talentMaterialHolder, bestArtifactsHolder, bestWeaponsHolder, skillPriorityHolder;
    private RecyclerView ascensionRecyclerView, talentRecyclerView, bestArtifactsRecyclerView, bestWeaponsRecyclerView, skillPriorityRecyclerView;
    private CharMaterialAdapter ascensionAdapter, talentAdapter, bestArtifactsAdapter, bestWeaponsAdapter, skillPriorityAdapter;
    private List<CharMaterialData> ascensionMaterialList, talentMaterialList, bestArtifactsList, bestWeaponsList, skillPriorityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_char_indiv, container, false);

        // Initialize views
        nameHolder = view.findViewById(R.id.charName);
        imgHolder = view.findViewById(R.id.charImg);
        ascensionMaterialHolder = view.findViewById(R.id.ascensionMaterialHolder);
        ascensionRecyclerView = view.findViewById(R.id.ascensionRecyclerView);
        talentMaterialHolder = view.findViewById(R.id.talentMaterialHolder);
        talentRecyclerView = view.findViewById(R.id.talentRecyclerView);
        bestArtifactsHolder = view.findViewById(R.id.bestArtifactsHolder);
        bestArtifactsRecyclerView = view.findViewById(R.id.bestArtifactsRecyclerView);
        bestWeaponsHolder = view.findViewById(R.id.bestWeaponsHolder);
        bestWeaponsRecyclerView = view.findViewById(R.id.bestWeaponsRecyclerView);
        skillPriorityHolder = view.findViewById(R.id.skillPriorityHolder);
        skillPriorityRecyclerView = view.findViewById(R.id.skillPriorityRecyclerView);
        btnClose = view.findViewById(R.id.btn_close);

        ascensionArrow = view.findViewById(R.id.ascensionArrow);
        talentArrow = view.findViewById(R.id.talentArrow);
        bestArtifactsArrow = view.findViewById(R.id.bestArtifactsArrow);
        bestWeaponsArrow = view.findViewById(R.id.bestWeaponsArrow);
        skillPriorityArrow = view.findViewById(R.id.skillPriorityArrow);

        // Initialize RecyclerViews with LayoutManagers
        ascensionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        talentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bestArtifactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bestWeaponsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        skillPriorityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up collapsible sections
        setUpCollapsibleSections();

        // Get character data
        Bundle args = getArguments();
        if (args != null) {
            String charName = args.getString("CHAR_NAME");
            CharData charData = getCharacterDataByName(charName);
            if (charData != null) {
                displayCharacterData(charData);
            }
        }

        // Close button listener
        btnClose.setOnClickListener(v -> {
            CharFragment charFragment = new CharFragment();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, charFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void setUpCollapsibleSections() {
        ascensionArrow.setOnClickListener(v -> toggleVisibility(ascensionMaterialHolder, ascensionArrow));
        talentArrow.setOnClickListener(v -> toggleVisibility(talentMaterialHolder, talentArrow));
        bestArtifactsArrow.setOnClickListener(v -> toggleVisibility(bestArtifactsHolder, bestArtifactsArrow));
        bestWeaponsArrow.setOnClickListener(v -> toggleVisibility(bestWeaponsHolder, bestWeaponsArrow));
        skillPriorityArrow.setOnClickListener(v -> toggleVisibility(skillPriorityHolder, skillPriorityArrow));
    }

    private void toggleVisibility(View view, ImageView arrow) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_arrow_up);
        } else {
            view.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_arrow_down);
        }
    }

    private CharData getCharacterDataByName(String charName) {
        List<CharData> allCharacterData = new ArrayList<>(); // Initialize a new list to hold the character data
        CharData.getCharacterData(allCharacterData, requireContext()); // Pass the list and context to getCharacterData

        for (CharData data : allCharacterData) {
            if (data.name.equals(charName)) {
                return data;
            }
        }
        return null;
    }


    private void displayCharacterData(CharData charData) {
        if (charData != null) {
            nameHolder.setText(charData.name);
            Picasso.get()
                    .load(charData.getCharImgUrl()) // Pass the URL string
                    .placeholder(R.drawable.ganyu) // Optional: Placeholder image while loading
                    .error(R.drawable.ic_character_aether) // Optional: Image to show on error
                    .into(imgHolder); // Your ImageView


            // Ascension Material
            ascensionMaterialList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : charData.ascensionRequirements.entrySet()) {
                ascensionMaterialList.add(new CharMaterialData(entry.getKey(), entry.getValue()));
            }
            ascensionMaterialList.add(new CharMaterialData("Total Mora", charData.ascensionMora));
            ascensionAdapter = new CharMaterialAdapter(ascensionMaterialList);
            ascensionRecyclerView.setAdapter(ascensionAdapter);

            // Talent Material
            talentMaterialList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : charData.talentRequirements.entrySet()) { // Assuming you have a similar structure for talents
                talentMaterialList.add(new CharMaterialData(entry.getKey(), entry.getValue()));
            }
            talentMaterialList.add(new CharMaterialData("Total Mora", charData.talentMora));
            talentAdapter = new CharMaterialAdapter(talentMaterialList);
            talentRecyclerView.setAdapter(talentAdapter);

            // Best Artifacts
            bestArtifactsList = new ArrayList<>();
            for (String artifact : charData.bestArtifactSets) {
                bestArtifactsList.add(new CharMaterialData(artifact, 1)); // Assuming quantity is 1 for each set
            }
            bestArtifactsAdapter = new CharMaterialAdapter(bestArtifactsList);
            bestArtifactsRecyclerView.setAdapter(bestArtifactsAdapter);

            // Best Weapons
            bestWeaponsList = new ArrayList<>();
            for (String weapon : charData.bestWeapons) {
                bestWeaponsList.add(new CharMaterialData(weapon, 1)); // Assuming quantity is 1 for each weapon
            }
            bestWeaponsAdapter = new CharMaterialAdapter(bestWeaponsList);
            bestWeaponsRecyclerView.setAdapter(bestWeaponsAdapter);

            // Skill Priority
            skillPriorityList = new ArrayList<>();
            for (String skill : charData.skillPrio) {
                skillPriorityList.add(new CharMaterialData(skill, 1)); // Assuming quantity is 1 for each skill
            }
            skillPriorityAdapter = new CharMaterialAdapter(skillPriorityList);
            skillPriorityRecyclerView.setAdapter(skillPriorityAdapter);
        }
    }
}
