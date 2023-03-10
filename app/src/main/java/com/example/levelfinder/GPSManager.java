package com.example.levelfinder;


import android.location.LocationListener;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSManager implements LocationListener {

    private LocationManager locationManager;
    private OnLocationChangedListener onLocationChangedListener;

    public GPSManager(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void startLocationUpdates() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            // GPS is disabled
        }
    }

    public void stopLocationUpdates() {
        locationManager.removeUpdates(this);
    }

    public void setOnLocationChangedListener(OnLocationChangedListener listener) {
        onLocationChangedListener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (onLocationChangedListener != null) {
            onLocationChangedListener.onLocationChanged(location);
        }
    }


    public interface OnLocationChangedListener {
        void onLocationChanged(Location location);
    }
}

