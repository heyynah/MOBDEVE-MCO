package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide; // Add this for Glide
import android.content.Context; // To access context if needed

public class CharViewHolder extends RecyclerView.ViewHolder {
    // TextView for character name
    TextView nameHolder;

    // ImageView for character image
    ImageView charImgView;

    // Root view
    View view;

    // Constructor
    public CharViewHolder(View itemView) {
        super(itemView);
        this.nameHolder = itemView.findViewById(R.id.charName);
        this.charImgView = itemView.findViewById(R.id.charImg);
        this.view = itemView;
    }

    // Method to bind character list data
    public void bindCharacter(CharData charData, Context context) {
        nameHolder.setText(charData.name);

        // Load image using Glide
        Glide.with(context)
                .load(charData.getCharImgUrl()) // Load the image URL
                .placeholder(R.drawable.ganyu) // Optional placeholder
                .error(R.drawable.ic_character_aether) // Optional error image
                .into(charImgView); // Target the ImageView
    }

    // Method to bind individual character data
    public void bindIndividualCharacter(CharData charData, Context context) {
        nameHolder.setText(charData.name);

        // Load image using Glide
        Glide.with(context)
                .load(charData.getCharImgUrl())
                .placeholder(R.drawable.ganyu)
                .error(R.drawable.ic_character_aether)
                .into(charImgView);
    }
}
