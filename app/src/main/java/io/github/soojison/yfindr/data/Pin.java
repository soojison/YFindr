package io.github.soojison.yfindr.data;

import org.parceler.Parcel;

import java.util.HashMap;

@Parcel
public class Pin {

    // no modifier because of @Parcel notation
    String networkName;
    String address;
    String uid;
    MyLatLng latLng;
    boolean locked;
    int numReports;
    HashMap<String, Double> ratingList;

    public Pin() {

    }

    public Pin(String networkName, String address, String uid, MyLatLng latLng, boolean locked) {
        this.networkName = networkName;
        this.address = address;
        this.uid = uid;
        this.latLng = latLng;
        this.locked = locked;
        this.numReports = 0;
        this.ratingList = new HashMap<>();
    }

    public String getNetworkName() {
        return networkName;
    }

    public String getAddress() {
        return address;
    }

    public String getUid() {
        return uid;
    }

    public MyLatLng getLatLng() {
        return latLng;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getNumReports() {
        return numReports;
    }

    public void incrementNumReport() {
        this.numReports += 1;
    }

    public boolean addRating(String uid, double rating) {
        if(ratingList.containsKey(uid)) {
            return false;
        } else {
            ratingList.put(uid, rating);
            return true;
        }
    }

    public double getTotalRating() {
        double sum = 0;
        for (Double rating : ratingList.values()) {
            sum += rating;
        }
        return sum/ratingList.size();
    }

    public void deleteRating(String uid) {
        ratingList.remove(uid);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Pin) {
            Pin that = (Pin) obj;
            return (this.uid.equals(that.uid) &&
                    this.latLng.equals(that.latLng));
        }
        return false;
    }
}
