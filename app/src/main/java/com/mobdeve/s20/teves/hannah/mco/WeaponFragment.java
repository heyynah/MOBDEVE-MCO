package com.mobdeve.s20.teves.hannah.mco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeaponFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weapon, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.weaponRecyclerView);

        // Set LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Fetch the weapon data using the updated method
        WeaponData.getWeaponData(requireContext(), new WeaponData.WeaponDataCallback() {
            @Override
            public void onWeaponDataFetched(List<WeaponData> weaponDataList) {
                // Set Adapter with the data
                WeaponAdapter adapter = new WeaponAdapter(weaponDataList, false, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}