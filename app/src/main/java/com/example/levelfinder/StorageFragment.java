package com.example.levelfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.levelfinder.databinding.FragmentFirstBinding;
import com.example.levelfinder.databinding.FragmentSecondBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.Inflater;

public class StorageFragment extends Fragment {

    private ListView listView;
    private FragmentSecondBinding binding;
    private SharedPreferences sharedPreferences;
    private List<String> gpsLocations;

    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        listView = view.findViewById(R.id.listView);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        // combine "longitude" and "latitude" from shared preferences to one string
        Set<String> gpsLocationsSet = sharedPreferences.getStringSet("coordinates", new HashSet<String>());
        // convert set to list
        System.out.println(gpsLocationsSet);
        gpsLocations = new ArrayList<>(gpsLocationsSet);

        String[] gpsLocationsArray = gpsLocations.toArray(new String[gpsLocations.size()]);
        System.out.println(gpsLocationsArray[0]);

        // add gps locations to list view
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, gpsLocationsArray);
        listView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StorageFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });




    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

