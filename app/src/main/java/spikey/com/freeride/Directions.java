package spikey.com.freeride;

import android.location.Location;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by spikey on 11/07/17.
 */

public class Directions {

    //GeoApiContext geoApiContext;

    public Directions() {
        //geoApiContext = getGeoContext();
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey("AIzaSyAkD84YGSwh5L-yMOGWQ6_f-xzqbyr-Bz4")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    public DirectionsResult getDirections(Task task, DateTime dateTime) {
        Location startLocation = task.getStartLocation();
        Location endLocation = task.getEndLocation();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .origin(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()))
                    .destination(new LatLng(endLocation.getLatitude(), endLocation.getLongitude()))
                    .departureTime(dateTime)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public DirectionsResult getDirections() {
        LatLng soton = new LatLng(50.930939,-1.390175);
        DateTime now = new DateTime();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .origin(soton)
                    .destination("Southampton, Hampshire")
                    .departureTime(now)
                    .await();
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
