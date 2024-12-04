package com.mobdeve.s20.teves.hannah.mco;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CharFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_char, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.charRecyclerView);

        // Set LayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        // Initialize the list to hold character data
        List<CharData> characterDataList = new ArrayList<>();

        // Fetch the character data using the updated method
        CharData.getCharacterData(characterDataList, requireContext()); // Pass the List and Context

        // Set Adapter with the data
        CharAdapter adapter = new CharAdapter(characterDataList, false, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
