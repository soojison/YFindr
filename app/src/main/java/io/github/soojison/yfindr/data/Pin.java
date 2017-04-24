package io.github.soojison.yfindr.data;

public class Pin {

    private String networkName;
    private String address;
    private String uid;
    private double rating;

    public Pin() {

    }

    public Pin(String networkName, String address, String uid, double rating) {
        this.networkName = networkName;
        this.address = address;
        this.uid = uid;
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
}
