package com.mobdeve.s20.teves.hannah.mco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeFragment extends Fragment {
    private HomePageAdapter adapter;
    private List<HomeDailiesData> dailyTasks;

    // Constructor to accept dailyTasks and adapter
    public HomeFragment(List<HomeDailiesData> dailyTasks, HomePageAdapter adapter) {
        this.dailyTasks = dailyTasks;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.dailyTaskRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set Adapter with the data
        recyclerView.setAdapter(adapter);

        // Show or hide the empty message based on the presence of tasks
        TextView emptyMessage = view.findViewById(R.id.emptyMessage);
        if (dailyTasks.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            emptyMessage.setVisibility(View.GONE);
        }

        return view;
    }
}