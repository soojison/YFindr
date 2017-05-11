package io.github.soojison.yfindr.data;

import com.google.android.gms.maps.model.LatLng;

public class Pin {

    private String networkName;
    private String address;
    private String uid;
    private double rating;
    private MyLatLng latLng;
    private boolean locked;

    public Pin() {

    }

    public Pin(String networkName, String address, String uid, MyLatLng latLng, boolean locked, double rating) {
        this.networkName = networkName;
        this.address = address;
        this.uid = uid;
        this.latLng = latLng;
        this.locked = locked;
        this.rating = rating;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public MyLatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(MyLatLng latLng) {
        this.latLng = latLng;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
