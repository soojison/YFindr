package io.github.soojison.yfindr;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.security.Security;

public class MyLocationManager implements LocationListener {

    public interface OnNewLocationAvailible {
        public void onNewLocation(Location location);
    }

    private LocationManager locationManager;
    private OnNewLocationAvailible onNewLocationAvailible;

    public MyLocationManager(OnNewLocationAvailible onNewLocationAvailible) {
        this.onNewLocationAvailible = onNewLocationAvailible;
    }

    public void startLocationMonitoring(Context context) throws SecurityException {
        locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE
        );
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void stopLocationMonitoring() {
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        onNewLocationAvailible.onNewLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
