package spikey.com.freeride;

import android.location.Location;

/**
 * Created by spikey on 10/07/17.
 */

public class Task {

    private Location startLocation;
    private Location endLocation;

    public Task(Location startLocation, Location endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    public Task(double startLatitude, double startLongitude,
                double endLatitude, double endLongitude) {

        startLocation.setLatitude(startLatitude);
        startLocation.setLongitude(startLongitude);
        endLocation.setLatitude(endLatitude);
        endLocation.setLongitude(endLongitude);
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Location getEndLocation() {
        return endLocation;
    }
}
