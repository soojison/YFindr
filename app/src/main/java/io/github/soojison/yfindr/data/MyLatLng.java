package io.github.soojison.yfindr.data;

public class MyLatLng {

    private Double latitude;
    private Double longitude;

    public MyLatLng() {

    }

    public MyLatLng(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public double getDistance(MyLatLng other) {
        double earthRadius = 3958.75;
        double latDistance = Math.toRadians(other.getLatitude() - this.getLatitude());
        double lonDistance = Math.toRadians(other.getLongitude() - this.getLongitude());
        double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) +
                Math.cos(Math.toRadians(this.getLatitude())) * Math.cos(Math.toRadians(other.getLatitude())) *
                        Math.sin(lonDistance /2) * Math.sin(lonDistance /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;
        int meterConversion = 1609;
        return (distance * meterConversion);

    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;   //If objects equal, is OK
        if (o instanceof MyLatLng) {
            MyLatLng that = (MyLatLng) o;
            return (this.latitude.equals(that.latitude)  && this.longitude.equals(that.longitude));
        }
        return false;
    }
}
