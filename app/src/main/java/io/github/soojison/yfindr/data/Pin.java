package io.github.soojison.yfindr.data;

import android.os.Parcel;
import android.os.Parcelable;


public class Pin implements Parcelable {

    private String networkName;
    private String address;
    private String uid;
    private double rating;
    private MyLatLng latLng;
    private boolean locked;
    private int numReports;

    public Pin() {

    }

    public Pin(String networkName, String address, String uid, MyLatLng latLng, boolean locked, double rating) {
        this.networkName = networkName;
        this.address = address;
        this.uid = uid;
        this.latLng = latLng;
        this.locked = locked;
        this.rating = rating;
        this.numReports = 0;
    }

    public Pin(Parcel in){
        networkName = in.readString();
        address = in.readString();
        latLng = new MyLatLng(in.readDouble(), in.readDouble());
        locked = (in.readInt() == 1);
        uid = in.readString();
        numReports = in.readInt();
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

    public int getNumReports() {
        return numReports;
    }

    public void setNumReports(int numReports) {
        this.numReports = numReports;
    }

    public void incrementNumReport() {
        this.numReports += 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.networkName);
        dest.writeString(this.address);
        dest.writeDouble(latLng.getLatitude());
        dest.writeDouble(latLng.getLongitude());
        dest.writeInt(locked ? 1 : 0);
        dest.writeString(uid);
        dest.writeInt(numReports);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Pin createFromParcel(Parcel in) {
            return new Pin(in);
        }

        public Pin[] newArray(int size) {
            return new Pin[size];
        }
    };

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
