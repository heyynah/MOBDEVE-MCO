package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import android.content.Context;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;

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
        String name = charData.getName();
        SpannableString spannableString = new SpannableString(name);
        if (charData.getIsFavorite()) {
            spannableString = new SpannableString(name + " ★");
            ImageSpan starSpan = new ImageSpan(context, R.drawable.ic_star);
            spannableString.setSpan(starSpan, name.length(), name.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        nameHolder.setText(spannableString);

        // Load image using Glide
        Glide.with(context)
                .load(charData.getCharImgUrl())
                .placeholder(R.drawable.ic_character_aether)
                .error(R.drawable.ic_character_aether)
                .into(charImgView);
    }

    // Method to bind individual character data
    public void bindIndividualCharacter(CharData charData, Context context) {
        String name = charData.getName();
        SpannableString spannableString = new SpannableString(name);
        if (charData.getIsFavorite()) {
            spannableString = new SpannableString(name + " ★");
            ImageSpan starSpan = new ImageSpan(context, R.drawable.ic_star);
            spannableString.setSpan(starSpan, name.length(), name.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        nameHolder.setText(spannableString);

        // Load image using Glide
        Glide.with(context)
                .load(charData.getCharImgUrl())
                .placeholder(R.drawable.ic_character_aether)
                .error(R.drawable.ic_character_aether)
                .into(charImgView);
    }
}