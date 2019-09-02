package com.death.tnt.directionmaps;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.death.tnt.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class ListDirectionMap extends AppCompatActivity {

    GoogleMap gmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directionmaps);
        final double lat1 = getIntent().getExtras().getDouble("lat1");
        final double lon1 = getIntent().getExtras().getDouble("lon1");
        final double lat2 = getIntent().getExtras().getDouble("lat2");
        final double lon2 = getIntent().getExtras().getDouble("lon2");
        Log.e("location", String.valueOf(lat1));
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                LatLng startPoint = new LatLng(lat1, lon1);
                gmap.addMarker(new MarkerOptions().position(startPoint).title("You are here"));
                LatLng endPoint = new LatLng(lat2, lon2);
                gmap.addMarker(new MarkerOptions().position(endPoint).title("Destination"));

                //Define list to get all latlng for the route
                List<LatLng> path = new ArrayList<>();

                //Execute Directions API request
                GeoApiContext context = new GeoApiContext.Builder().apiKey(getResources()
                        .getString(R.string.google_places_key)).build();
                DirectionsApiRequest req = DirectionsApi
                        .getDirections(context, startPoint.toString(), endPoint.toString());
                try {
                    DirectionsResult res = req.await();
                    //Loop through legs and steps to get encoded polylines of each step
                    if (res.routes != null && res.routes.length > 0) {
                        DirectionsRoute route = res.routes[0];

                        if (route.legs != null) {
                            for (int i = 0; i < route.legs.length; i++) {
                                DirectionsLeg leg = route.legs[i];
                                if (leg.steps != null) {
                                    for (int j = 0; j < leg.steps.length; j++) {
                                        DirectionsStep step = leg.steps[j];
                                        if (step.steps != null && step.steps.length > 0) {
                                            for (int k = 0; k < step.steps.length; k++) {
                                                DirectionsStep step1 = step.steps[k];
                                                EncodedPolyline points1 = step1.polyline;
                                                if (points1 != null) {
                                                    //Decode polyline and add points to list of route coordinates
                                                    List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                    for (com.google.maps.model.LatLng coord1 : coords1) {
                                                        path.add(new LatLng(coord1.lat, coord1.lng));
                                                    }
                                                }
                                            }
                                        } else {
                                            EncodedPolyline points = step.polyline;
                                            if (points != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords = points.decodePath();
                                                for (com.google.maps.model.LatLng coord : coords) {
                                                    path.add(new LatLng(coord.lat, coord.lng));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {

                }
                //Draw the polyline
                if (path.size() > 0) {
                    PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
                    gmap.addPolyline(opts);
                }

                gmap.getUiSettings().setZoomControlsEnabled(true);

                gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 10));

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
