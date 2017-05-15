package io.github.soojison.yfindr.eventbus;

import android.location.Location;

public class LocationEvent {

    private Location myLocation;

    public LocationEvent(Location location) {
        this.myLocation = location;
    }

    public Location getMyLocation() {
        return myLocation;
    }
}
