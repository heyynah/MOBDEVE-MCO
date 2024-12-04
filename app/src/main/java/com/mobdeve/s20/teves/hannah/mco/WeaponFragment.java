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
        RecyclerView recyclerView;
        WeaponAdapter adapter;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weapon, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.weaponRecyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        // Set LayoutManager
        recyclerView.setLayoutManager(gridLayoutManager);

        // Set Adapter with the data
        List<WeaponData> weaponDataList = WeaponData.getWeaponData();
        adapter = new WeaponAdapter(weaponDataList, false);
        recyclerView.setAdapter(adapter);

        return view;
    }
}