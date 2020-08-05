package com.acpay.acapytrade.fragments.Locations;

import com.google.android.gms.maps.model.LatLng;

public class location {
    String user;
    String latitude;
    String longlatitude;
    String date;


    public location(String user, String latitude, String longlatitude, String date) {
        this.user = user;
        this.latitude = latitude;
        this.longlatitude = longlatitude;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public LatLng latlang() {
        double latitude = Double.parseDouble(this.latitude);
        double longitude = Double.parseDouble(this.longlatitude);
        LatLng location = new LatLng(latitude, longitude);
        return location;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLonglatitude() {
        return longlatitude;
    }

    public void setLonglatitude(String longlatitude) {
        this.longlatitude = longlatitude;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
