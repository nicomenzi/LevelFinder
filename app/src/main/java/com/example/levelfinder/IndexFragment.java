package com.example.levelfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.levelfinder.databinding.FragmentFirstBinding;


public class IndexFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private TextView textView;

    private GPSManager gpsLocationManager;
    private SharedPreferences sharedPreferences;

    private FragmentFirstBinding binding;

    private boolean isLevel = false;
    private Location latestLocation;



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
        gpsLocationManager.setOnLocationChangedListener(new GPSManager.OnLocationChangedListener() {
            @Override
            public void onLocationChanged(Location location) {
                latestLocation = location;
            }

        });
        gpsLocationManager.startLocationUpdates();
        return view;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];


        textView.setText("Accelerometer:\n" + "X: " + x + "\nY: " + y + "\nZ: " + z);


        if ((x < 1 && x > -1 && y < 1 && y > -1 )&& !isLevel) {
            saveLocationData();
            isLevel = true;
        }
        if (x > 1 || x < -1 || y > 1 || y < -1) {
            isLevel = false;
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


    private void saveLocationData() {
        if (latestLocation != null) {

            binding.gpstext.setText("GPS working");
            double latitude = latestLocation.getLatitude();
            double longitude = latestLocation.getLongitude();
            String coordinateString = latitude + "," + longitude;
            String coordinates = sharedPreferences.getString("coordinates", "");
            if (!coordinates.isEmpty()) {
                coordinates += "$";
            }
            coordinates += coordinateString;
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("coordinates", coordinates);
            editor.apply();
            vibrateDevice();
            System.out.println("Location data saved");
        }
        else {
            binding.gpstext.setText("GPS not working");
        }
    }

    private void vibrateDevice() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);
    }


    @Override
    public void onPause() {
        super.onPause();
        gpsLocationManager.stopLocationUpdates();
        sensorManager.unregisterListener(this);
    }


}
