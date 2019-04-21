package com.death.tnt.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.death.tnt.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.logging.Logger;

import static android.content.Context.LOCATION_SERVICE;

/**
 * From
 * https://gist.github.com/enginebai/adcae1f17d3b2114590c
 */
public class ExampleViewMap extends Fragment {
    Button satellite, terrain, hybrid, normal, go;
    final static int for_permission = 1;
    SearchView searchView;
    double latitude = 0,
            longitude = 0;
    private int PROXIMITY_RADIUS = 5000;
    RequestQueue requestQueue;
    GoogleMap gmap;
    LocationManager locationManager = null;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    String url = "https://maps.googleapis.com/maps/api/place/search/json?";

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;


    //flag for WIFi
    boolean isWifiEnabled = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map, null);
        if (!isGooglePlayServicesAvailable()) {
            Log.e("play service", "not available");
            Toast.makeText(getActivity(), "Google play services not available", Toast.LENGTH_SHORT).show();
        }
        satellite = (Button) view.findViewById(R.id.satellite);
        terrain = (Button) view.findViewById(R.id.terrain);
        hybrid = (Button) view.findViewById(R.id.hybrid);
        normal = (Button) view.findViewById(R.id.normal);
        searchView = (SearchView) view.findViewById(R.id.search);
        go = (Button) view.findViewById(R.id.go);
        requestQueue = Volley.newRequestQueue(getContext());


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                getCurrentLocation();
                gmap = googleMap;
                gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        gmap.clear();
                        gmap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("lat:" + latLng.latitude + "\n" + "lng:" + latLng.longitude));
//
                    }
                });

            }
        });
        // Satellite View
        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });
        //Terrain View
        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });
        //Hybrid View
        hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });
        //Normal View
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placetxt = "" + searchView.getQuery();
                try {

                if (ActivityCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                gmap.setMyLocationEnabled(true);
                Location myLocation = gmap.getMyLocation();
                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                stringBuilder.append("location=" + myLocation.getLatitude() + "," + myLocation.getLongitude());
                stringBuilder.append("&radius=" + 1000);
                stringBuilder.append("&keyword=" + placetxt);
                stringBuilder.append("&key" + getResources().getString(R.string.google_places_key));

                String url = stringBuilder.toString();

                Object[] datatransfer = new Object[2];
                datatransfer[0] = gmap;
                datatransfer[1] = url;
                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
                getNearbyPlaces.execute(datatransfer);
            } catch (Exception e){
                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
                }
//                try {
//                    getCurrentLocation();
//                    String placetxt = ""+searchView.getQuery();
//                    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//                    googlePlacesUrl.append("location=" + latitude + "," + longitude);
//                    googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
//                    googlePlacesUrl.append("&keyword="+placetxt);
//                    googlePlacesUrl.append("&sensor=true");
//                    googlePlacesUrl.append("&key=" + getResources().getString(R.string.google_places_key));
//
////                    GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
//                    GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//                    Object[] toPass = new Object[2];
//                    toPass[0] = gmap;
//                    toPass[1] = googlePlacesUrl.toString();
////                    googlePlacesReadTask.execute(toPass);
//                    getNearbyPlaces.execute(toPass);
//                } catch (Exception e){
//                    Log.e("Exception",""+e);
//                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_LONG).show();
//                }

            }
        });

        return view;
    }

    private void getCurrentLocation() {
        try {


            locationManager = (LocationManager) getContext()
                    .getSystemService(LOCATION_SERVICE);
            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting NETWORK status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            isWifiEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);


            Location location = null;

            if (!isGPSEnabled && !isNetworkEnabled && !isWifiEnabled) {
                //no GPS or NETWORK provider is enabled
                Toast.makeText(getContext(), "Please Enable GPS first", Toast.LENGTH_SHORT).show();
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                for_permission);
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        drawMarker(location);
                    }
                }
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        drawMarker(location);
                    }
                }
                if (isWifiEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    if (location != null) {
                        drawMarker(location);
                    }
                }
            }
//            if (location != null) {
//                drawMarker(location);
//            }
        }//try closed
        catch (Exception e) {
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
        }


    }

    private void drawMarker(Location location) {
        if (gmap != null) {
            gmap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            gmap.addMarker(new MarkerOptions()
                    .position(gps));
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 12));
        }


    }


    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                drawMarker(location);
                locationManager.removeUpdates(locationListener);
            } else {
                Log.e("Location", "Location is null");
            }
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
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case for_permission: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {

                }

                return;
            }
        }
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

}
