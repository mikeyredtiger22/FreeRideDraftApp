package spikey.com.freeride;

import android.location.Location;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
//import com.google.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;

import org.joda.time.DateTime;

import java.util.List;

public class MapsActivity extends FragmentActivity
                          implements OnMapReadyCallback, TaskSelectionFragment.OnFragmentInteractionListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_panel_both);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // todo: add animation

        LatLng soton = new LatLng(50.930939,-1.390175);
        LatLng soton2 = new LatLng(50.730939,-1.340175);
        mMap.addMarker(new MarkerOptions().position(soton).title("Best House").snippet("mike lives here btw").visible(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(soton));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        Directions directions = new Directions();
        Task task = new Task(soton.latitude, soton.longitude, soton2.latitude, soton2.longitude);
        DirectionsResult directionsResult = directions.getDirections(task, new DateTime());
        if (directionsResult != null) {
            addMarkersToMap(directionsResult, mMap);
            addPolyline(directionsResult, mMap);
        } else {
            Log.e(this.getLocalClassName(), "Directions generation encountered error.");
        }
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {

        mMap.addMarker(new MarkerOptions().title(results.routes[0].legs[0].startAddress)
                                          .position(new LatLng( results.routes[0].legs[0].startLocation.lat,
                                                                results.routes[0].legs[0].startLocation.lng)).visible(true));

        mMap.addMarker(new MarkerOptions().title(results.routes[0].legs[0].startAddress)
                                          .snippet(getEndLocationTitle(results))
                                          .position(new LatLng( results.routes[0].legs[0].endLocation.lat,
                                                                results.routes[0].legs[0].endLocation.lng)));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable +
                " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
