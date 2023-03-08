package com.example.levelfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.Arrays;
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
        String gpsLocationsString = sharedPreferences.getString("coordinates", "");
        // split string to get individual coordinates
        String[] gpsLocationsArray = gpsLocationsString.split("\\$");
        System.out.println(Arrays.toString(gpsLocationsArray));

        // add gps locations to list view
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, gpsLocationsArray);
        listView.setAdapter(adapter);

        // set click listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] coordinates = gpsLocationsArray[position].split(",");
                double latitude = Double.parseDouble(coordinates[0]);
                double longitude = Double.parseDouble(coordinates[1]);
                // create Uri with coordinates
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude);
                // create Intent to open Google Maps
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

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

