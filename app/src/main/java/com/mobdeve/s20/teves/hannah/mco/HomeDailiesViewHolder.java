package com.mobdeve.s20.teves.hannah.mco;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageButton;

public class HomeDailiesViewHolder extends RecyclerView.ViewHolder {
    TextView timeHolder;
    TextView taskHolder;
    ImageButton deleteButton;
    View view;

    HomeDailiesViewHolder(View itemView) {
        super(itemView);
        this.timeHolder = itemView.findViewById(R.id.timeHolder);
        this.taskHolder = itemView.findViewById(R.id.taskHolder);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
        this.view = itemView;
    }

    public void bind(final HomeDailiesData data, final OnItemClickListener listener, final OnDeleteClickListener deleteListener) {
        view.setOnClickListener(v -> listener.onItemClick(data));
        deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(getAdapterPosition()));
    }

    public interface OnItemClickListener {
        void onItemClick(HomeDailiesData data);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }
}