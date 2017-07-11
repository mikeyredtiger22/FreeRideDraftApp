package spikey.com.freeride;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

import spikey.com.freeride.databinding.FragmentTaskSelectionBinding;

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
        LatLng isle = new LatLng(50.730939,-1.340175);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(soton, 15));

        //TODO: add walking directions to car then driving directions to destination

        Directions directions = new Directions();
        Task task = new Task(soton.latitude, soton.longitude, isle.latitude, isle.longitude);
        DirectionsResult directionsResult = directions.getDirections(task, new DateTime());
        if (directionsResult != null) {
            addMarkersToMap(directionsResult, mMap);
            addPolyline(directionsResult, mMap);
            fillRouteDetailsPanel(directionsResult);
        } else {
            Log.e(this.getLocalClassName(), "Directions generation encountered error.");
        }
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {

        mMap.addMarker(new MarkerOptions()
                .title(results.routes[0].legs[0].startAddress)
                .position(new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng)));

        mMap.addMarker(new MarkerOptions()
                .title(results.routes[0].legs[0].startAddress)
                .position(new LatLng(results.routes[0].legs[0].endLocation.lat, results.routes[0].legs[0].endLocation.lng)));
    }

    private void fillRouteDetailsPanel(DirectionsResult results) {
        //todo: remove findViewById - use Bindings
        //todo: show street and postcode only
        //todo: show start marker label
        //todo: set zoom to route bounds
        TextView startLoc = (TextView) findViewById(R.id.mapBottomPanel_title_start_location);
        startLoc.setText("Start: " + results.routes[0].legs[0].startAddress.split(",")[0]);
        TextView endLoc = (TextView) findViewById(R.id.mapBottomPanel_end_location);
        endLoc.setText("Destination: " + results.routes[0].legs[0].endAddress.split(",")[0]);
        TextView duration = (TextView) findViewById(R.id.mapBottomPanel_time);
        duration.setText("Duration: " + results.routes[0].legs[0].duration.humanReadable);
        TextView distance = (TextView) findViewById(R.id.mapBottomPanel_distance);
        distance.setText("Distance: " + results.routes[0].legs[0].distance.humanReadable);
        TextView extra = (TextView) findViewById(R.id.mapBottomPanel_extra);
        LocalTime currentTime = new LocalTime();
        LocalTime arrivalTime = currentTime.plusSeconds(new Long(results.routes[0].legs[0].durationInTraffic.inSeconds).intValue());
        extra.setText("Arrival Time " + arrivalTime.getHourOfDay() + ":" + arrivalTime.getMinuteOfHour());
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
