package com.mobdeve.s20.teves.hannah.mco;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeaponAdapter extends RecyclerView.Adapter<WeaponViewHolder> {
    private List<WeaponData> weaponDataList;
    private boolean isDetailView; // Flag to check if in detail view
    private Context context;

    public WeaponAdapter(List<WeaponData> weaponDataList, boolean isDetailView, Context context) {
        this.weaponDataList = weaponDataList;
        this.isDetailView = isDetailView;
        this.context = context;
    }

    @NonNull
    @Override
    public WeaponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weapon, parent, false);
        return new WeaponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeaponViewHolder holder, int position) {
        WeaponData weaponData = weaponDataList.get(position);

        if (isDetailView) {
            holder.bindIndividualWeapon(weaponData, context);
        } else {
            holder.bindWeapon(weaponData, context);

            holder.view.setOnClickListener(v -> {
                Fragment indivWeaponFragment = new IndivWeaponFragment();
                Bundle args = new Bundle();
                args.putString("WEAPON_NAME", weaponData.name);
                indivWeaponFragment.setArguments(args);

                // Replace the fragment in the container
                ((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, indivWeaponFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return weaponDataList.size();
    }
}



