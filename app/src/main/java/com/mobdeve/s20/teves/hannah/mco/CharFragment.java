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

        // Fetch the character data using the updated method
        CharData.getCharacterData(requireContext(), new CharData.CharacterDataCallback() {
            @Override
            public void onCharacterDataFetched(List<CharData> charDataList) {
                // Set Adapter with the data
                CharAdapter adapter = new CharAdapter(charDataList, false, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }
}
