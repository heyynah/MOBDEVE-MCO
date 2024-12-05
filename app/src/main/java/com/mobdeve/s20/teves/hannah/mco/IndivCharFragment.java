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
    private ImageView ascensionArrow, bestArtifactsArrow, bestWeaponsArrow, skillPriorityArrow;
    private LinearLayout ascensionMaterialHolder, bestArtifactsHolder, bestWeaponsHolder, skillPriorityHolder;
    private RecyclerView ascensionRecyclerView, bestArtifactsRecyclerView, bestWeaponsRecyclerView, skillPriorityRecyclerView;
    private CharMaterialAdapter ascensionAdapter, bestArtifactsAdapter, bestWeaponsAdapter, skillPriorityAdapter;
    private List<CharMaterialData> ascensionMaterialList, bestArtifactsList, bestWeaponsList, skillPriorityList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_char_indiv, container, false);

        // Initialize views
        nameHolder = view.findViewById(R.id.charName);
        imgHolder = view.findViewById(R.id.charImg);
        ascensionMaterialHolder = view.findViewById(R.id.ascensionMaterialHolder);
        ascensionRecyclerView = view.findViewById(R.id.ascensionRecyclerView);
        bestArtifactsHolder = view.findViewById(R.id.bestArtifactsHolder);
        bestArtifactsRecyclerView = view.findViewById(R.id.bestArtifactsRecyclerView);
        bestWeaponsHolder = view.findViewById(R.id.bestWeaponsHolder);
        bestWeaponsRecyclerView = view.findViewById(R.id.bestWeaponsRecyclerView);
        skillPriorityHolder = view.findViewById(R.id.skillPriorityHolder);
        skillPriorityRecyclerView = view.findViewById(R.id.skillPriorityRecyclerView);
        btnClose = view.findViewById(R.id.btn_close);

        ascensionArrow = view.findViewById(R.id.ascensionArrow);
        bestArtifactsArrow = view.findViewById(R.id.bestArtifactsArrow);
        bestWeaponsArrow = view.findViewById(R.id.bestWeaponsArrow);
        skillPriorityArrow = view.findViewById(R.id.skillPriorityArrow);

        // Initialize RecyclerViews with LayoutManagers
        ascensionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bestArtifactsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bestWeaponsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        skillPriorityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up collapsible sections
        setUpCollapsibleSections();

        // Get character data
        Bundle args = getArguments();
        if (args != null) {
            String charName = args.getString("CHAR_NAME");
            getCharacterDataByName(charName, charData -> {
                if (charData != null) {
                    displayCharacterData(charData);
                }
            });
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

    public interface CharacterDataCallback {
        void onCharacterDataFetched(CharData charData);
    }

    private void getCharacterDataByName(String charName, CharacterDataCallback callback) {
        CharData.getCharacterData(requireContext(), charDataList -> {
            for (CharData data : charDataList) {
                if (data.name.equals(charName)) {
                    callback.onCharacterDataFetched(data);
                    return;
                }
            }
            callback.onCharacterDataFetched(null);
        });
    }

    private void displayCharacterData(CharData charData) {
        if (charData != null) {
            nameHolder.setText(charData.name);
            Picasso.get()
                    .load(charData.getCharImgUrl()) // Pass the URL string
                    .error(R.drawable.ic_character_aether) // Optional: Image to show on error
                    .into(imgHolder); // Your ImageView

            // Ascension Material
            ascensionMaterialList = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : charData.ascensionRequirements.entrySet()) {
                ascensionMaterialList.add(new CharMaterialData(entry.getKey(), entry.getValue()));
            }
//            ascensionMaterialList.add(new CharMaterialData("Total Mora", charData.ascensionMora));
            ascensionAdapter = new CharMaterialAdapter(ascensionMaterialList);
            ascensionRecyclerView.setAdapter(ascensionAdapter);

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
            skillPriorityList.add(new CharMaterialData(charData.skillPrio, 1)); // Assuming quantity is 1 for the skill
            skillPriorityAdapter = new CharMaterialAdapter(skillPriorityList);
            skillPriorityRecyclerView.setAdapter(skillPriorityAdapter);
        }
    }
}