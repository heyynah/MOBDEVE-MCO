package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide; // Add this for Glide
import android.content.Context; // To access context if needed

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

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
        String name = weaponData.getName();
        SpannableString spannableString = new SpannableString(name);
        if (weaponData.getIsFavorite()) {
            spannableString = new SpannableString(name + " ★");
            ImageSpan starSpan = new ImageSpan(context, R.drawable.ic_star);
            spannableString.setSpan(starSpan, name.length(), name.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        weaponName.setText(spannableString);

        // Load image using Glide
        Glide.with(context)
                .load(weaponData.getWeaponImgUrl()) // Load the image URL
                .placeholder(R.drawable.ic_character_lumine) // Optional placeholder
                .error(R.drawable.ic_character_lumine) // Optional error image
                .into(weaponImg);
    }

    public void bindIndividualWeapon(WeaponData weaponData, Context context) {
        String name = weaponData.getName();
        SpannableString spannableString = new SpannableString(name);
        if (weaponData.getIsFavorite()) {
            spannableString = new SpannableString(name + " ★");
            ImageSpan starSpan = new ImageSpan(context, R.drawable.ic_star);
            spannableString.setSpan(starSpan, name.length(), name.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        weaponName.setText(spannableString);

        Glide.with(context)
                .load(weaponData.getWeaponImgUrl()) // Load the image URL
                .placeholder(R.drawable.ic_character_lumine) // Optional placeholder
                .error(R.drawable.ic_character_lumine) // Optional error image
                .into(weaponImg);
    }
}
