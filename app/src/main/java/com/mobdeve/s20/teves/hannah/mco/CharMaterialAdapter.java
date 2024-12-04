package com.mobdeve.s20.teves.hannah.mco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CharMaterialAdapter extends RecyclerView.Adapter<CharMaterialAdapter.CharMaterialViewHolder> {

    private List<CharMaterialData> materialList;

    public CharMaterialAdapter(List<CharMaterialData> materialList) {
        this.materialList = materialList;
    }

    @NonNull
    @Override
    public CharMaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_char_material, parent, false);
        return new CharMaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CharMaterialViewHolder holder, int position) {
        CharMaterialData material = materialList.get(position);
        holder.charMaterialName.setText(material.getName());
        holder.charMaterialAmount.setText(String.valueOf(material.getAmount()));
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    static class CharMaterialViewHolder extends RecyclerView.ViewHolder {
        TextView charMaterialName;
        TextView charMaterialAmount;

        public CharMaterialViewHolder(View itemView) {
            super(itemView);
            charMaterialName = itemView.findViewById(R.id.charMaterialName);
            charMaterialAmount = itemView.findViewById(R.id.charMaterialAmount);
        }
    }
}
