package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WeaponViewHolder extends RecyclerView.ViewHolder {
    // TextView for character name
    TextView weaponName;
    ImageView weaponImg;

    // View reference
    View view;

    // Constructor
    public WeaponViewHolder(View itemView) {
        super(itemView);
        this.weaponName = itemView.findViewById(R.id.weaponName);
        this.weaponImg = itemView.findViewById(R.id.weaponImg);
        this.view = itemView;
    }

    // Method to bind character list data
    public void bindWeapon(WeaponData weaponData) {
        weaponName.setText(weaponData.name);
        weaponImg.setImageResource(weaponData.getWeaponImg());
    }

    public void bindIndividualWeapon(WeaponData weaponData) {
        weaponName.setText(weaponData.name);
        weaponImg.setImageResource(weaponData.getWeaponImg());
    }
}
