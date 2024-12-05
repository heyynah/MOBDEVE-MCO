package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide; // Add this for Glide
import android.content.Context; // To access context if needed

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
    public void bindWeapon(WeaponData weaponData, Context context) {
        weaponName.setText(weaponData.name);

        // Load image using Glide
        Glide.with(context)
                .load(weaponData.getWeaponImgUrl()) // Load the image URL
                .placeholder(R.drawable.ic_character_lumine) // Optional placeholder
                .error(R.drawable.ic_character_lumine) // Optional error image
                .into(weaponImg);
    }

    public void bindIndividualWeapon(WeaponData weaponData, Context context) {
        weaponName.setText(weaponData.name);

        Glide.with(context)
                .load(weaponData.getWeaponImgUrl()) // Load the image URL
                .placeholder(R.drawable.ic_character_lumine) // Optional placeholder
                .error(R.drawable.ic_character_lumine) // Optional error image
                .into(weaponImg);
    }
}
