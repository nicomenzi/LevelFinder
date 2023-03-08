package com.example.levelfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.levelfinder.databinding.FragmentFirstBinding;

import java.util.HashSet;
import java.util.Set;

public class IndexFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textView;

    private GPSManager gpsLocationManager;
    private SharedPreferences sharedPreferences;

    private Boolean done = false;
    private FragmentFirstBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsLocationManager = new GPSManager(getActivity());
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sensorManager = (SensorManager) getActivity().getSystemService(getActivity().SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        textView = binding.coordinates;
        textView.setText("Accelerometer:\n" + "X: " + 0 + "\nY: " + 0 + "\nZ: " + 0);

        // start sensor listener
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        return view;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];


        textView.setText("Accelerometer:\n" + "X: " + x + "\nY: " + y + "\nZ: " + z);


        if ((x < 1 && x > -1 && y < 1 && y > -1 ) && done == false) {
            getLocationData();
            System.out.println("Location data saved");
            // print location data

            System.out.println(sharedPreferences.getStringSet("coordinates", new HashSet<String>()));
            done = true;
        }
        if (x > 1 || x < -1 || y > 1 || y < -1) {
            done = false;
        }

        // update chart

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // on places button click

        binding.places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IndexFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not used
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    private void getLocationData() {
            gpsLocationManager.setOnLocationChangedListener(new GPSManager.OnLocationChangedListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    saveLocationData(latitude, longitude);
                }
            });
            gpsLocationManager.startLocationUpdates();



    }

    private void saveLocationData(double latitude, double longitude) {
        Set<String> coordinates = sharedPreferences.getStringSet("coordinates", new HashSet<String>());
        String coordinateString = latitude + "," + longitude;
        coordinates.add(coordinateString);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("coordinates", coordinates);
        editor.apply();
    }


    @Override
    public void onPause() {
        super.onPause();
        gpsLocationManager.stopLocationUpdates();
        sensorManager.unregisterListener(this);
    }


}
