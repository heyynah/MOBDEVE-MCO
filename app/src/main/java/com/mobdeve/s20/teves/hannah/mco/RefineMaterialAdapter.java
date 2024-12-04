package com.mobdeve.s20.teves.hannah.mco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RefineMaterialAdapter extends RecyclerView.Adapter<RefineMaterialAdapter.ViewHolder> {

    private List<RefineMaterialData> refineMaterials;

    public RefineMaterialAdapter(List<RefineMaterialData> refineMaterials) {
        this.refineMaterials = refineMaterials;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refine_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RefineMaterialData refineMaterial = refineMaterials.get(position);
        holder.refineMaterialName.setText(refineMaterial.getMaterialName());
        holder.refineMaterialAmount.setText(String.valueOf(refineMaterial.getMaterialAmount()));
    }

    @Override
    public int getItemCount() {
        return refineMaterials.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView refineMaterialName;
        TextView refineMaterialAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            refineMaterialName = itemView.findViewById(R.id.refineMaterialName);
            refineMaterialAmount = itemView.findViewById(R.id.refineMaterialAmount);
        }
    }
}

