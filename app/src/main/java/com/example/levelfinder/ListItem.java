package com.example.levelfinder;


public class ListItem {
    private String gpsText;
    private String locationText;

    public ListItem(String gpsText, String locationText) {
        this.gpsText = gpsText;
        this.locationText = locationText;
    }

    public String getGpsText() {
        return gpsText;
    }

    public String getLocationText() {
        return locationText;
    }
}

