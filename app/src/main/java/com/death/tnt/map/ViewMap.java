package com.death.tnt.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.death.tnt.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ViewMap extends Fragment {
    Button satellite, terrain, hybrid, normal;
    GoogleMap gmap;
    SearchView searchView;
    double lat, lng;
    RequestQueue requestQueue;
    LocationRequest request;

    String url = "http://maps.googleapis.com/maps/api/geocode/json?address=";


    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        context = container.getContext();
        View view = inflater.inflate(R.layout.view_map, null);
        satellite = (Button) view.findViewById(R.id.satellite);
        terrain = (Button) view.findViewById(R.id.terrain);
        hybrid = (Button) view.findViewById(R.id.hybrid);
        normal = (Button) view.findViewById(R.id.normal);
        searchView = (SearchView) view.findViewById(R.id.search);
        requestQueue = Volley.newRequestQueue(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                final GPSTracker gpsTracker = new GPSTracker(getContext());

                if (gpsTracker.getIsGPSTrackingEnabled()) {
                    gmap = googleMap;
//                googleMap.setMyLocationEnabled(true);
//                Location myLocation = googleMap.getMyLocation();

                    Double latitude, longitude;
                    latitude = gpsTracker.latitude;
                    longitude = gpsTracker.longitude;
                    LatLng l = new LatLng(latitude, longitude);
//                LatLng l = new LatLng(28.776267, 83.753319);
                    googleMap.addMarker(new MarkerOptions().position(l).title("current location"));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 9));
                } else {
                    gpsTracker.showSettingsAlert();
                }


                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("lat:" + latLng.latitude + "\n" + "lng:" + latLng.longitude));
                        lat = latLng.latitude;
                        lng = latLng.longitude;
//                        getAddress(lat, lng);
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                            Address obj = addresses.get(0);
                            String add = obj.getAddressLine(0);
                            add = add + "\n" + obj.getCountryName();//NP
                            add = add + "\n" + obj.getAdminArea();//EDR
                            add = add + "\n" + obj.getLocality();

                            String locality = obj.getLocality();//biratnagar
                            String adminArea = obj.getAdminArea();//EDR
//                            String as = obj.getSubAdminArea();//Koshi
                            String ss = obj.getSubLocality();

                            Log.e("Location12", "" + locality + "\n" + adminArea
                                    + "\n" + ss);


                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        googleMap.clear();

                        googleMap.addMarker(new MarkerOptions().position(latLng).
                                icon(BitmapDescriptorFactory.
                                        defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        PolylineOptions polylineOptions = new PolylineOptions();
                        polylineOptions.add(latLng);
                        polylineOptions.add(new LatLng(lat, lng));
                        googleMap.addPolyline(polylineOptions);
                        Location locl = new Location("origin");
                        locl.setLatitude(lat);
                        locl.setLongitude(lng);
                        Location locl2 = new Location("destination");
                        locl2.setLatitude(latLng.latitude);
                        locl2.setLongitude(latLng.longitude);

                        double diatance = locl.distanceTo(locl2);
                        Toast.makeText(getContext(), diatance / 1000 + "km", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        return view;


    }

}
