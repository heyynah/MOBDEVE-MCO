package com.mobdeve.s20.teves.hannah.mco;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomePageAdapter extends RecyclerView.Adapter<HomeDailiesViewHolder> {
    List<HomeDailiesData> list;
    private final HomeDailiesViewHolder.OnItemClickListener listener;
    private final HomeDailiesViewHolder.OnDeleteClickListener deleteListener;

    public HomePageAdapter(List<HomeDailiesData> list, HomeDailiesViewHolder.OnItemClickListener listener, HomeDailiesViewHolder.OnDeleteClickListener deleteListener) {
        this.list = list;
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public HomeDailiesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_task, parent, false);
        return new HomeDailiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HomeDailiesViewHolder holder, final int position) {
        HomeDailiesData data = list.get(position);
        holder.timeHolder.setText(data.time);
        holder.taskHolder.setText(data.task);
        holder.bind(data, listener, deleteListener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }
}