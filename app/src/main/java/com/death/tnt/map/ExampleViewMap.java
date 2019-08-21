package com.death.tnt.map;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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



import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * From
 * https://gist.github.com/enginebai/adcae1f17d3b2114590c
 */
public class ExampleViewMap extends Fragment {
    //progress dialog
    ProgressDialog pDialog;
    Button go, list_view;
    final static int for_permission = 1;
    SearchView searchView;
    //    EditText radius;
    double latitude = 0,
            longitude = 0;
    RequestQueue requestQueue;
    GoogleMap gmap;
    LocationManager locationManager;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 0;
    public static final int LOCATION_UPDATE_MIN_TIME = 0;
    String url = "https://maps.googleapis.com/maps/api/place/search/json?";

    // flag for GPS status
    boolean isGPSEnabled;

    // flag for network status
    boolean isNetworkEnabled;


    //flag for WIFi
    boolean isWifiEnabled;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map, null);
        if (!isGooglePlayServicesAvailable()) {
            Log.e("play service", "not available");
            Toast.makeText(getActivity(), "Google play services not available", Toast.LENGTH_SHORT).show();
        }

        searchView = (SearchView) view.findViewById(R.id.search);
        go = (Button) view.findViewById(R.id.go);
        requestQueue = Volley.newRequestQueue(getContext());

//        radius = (EditText) view.findViewById(R.id.radius);
        list_view = (Button) view.findViewById(R.id.view_on_page);
//        final String radiustxt = radius.getText().toString();


        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                getCurrentLocation();
                gmap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        //Already two locations
                    }
                });

            }
        });


/**
 * search starts here
 */
        //search on click
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(getActivity());
                pDialog.setTitle("Getting Locations");
                pDialog.setMessage("Please Wait...");
                pDialog.setCancelable(false);
                pDialog.show();
                String placetxt = "" + searchView.getQuery();
                try {

                    if (ActivityCompat.checkSelfPermission(getActivity()
                            , Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getActivity()
                            , Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    locationManager = (LocationManager) getContext()
                            .getSystemService(LOCATION_SERVICE);
                    Criteria criteria = new Criteria();
                    String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
                    Location location = locationManager.getLastKnownLocation(bestProvider);
                    if (location != null) {
                        getCurrentLocation();
                        Log.e("TAG", "GPS is on");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        stringBuilder.append("location=" + latitude + "," + longitude);
                        stringBuilder.append("&radius=" + 5000);
                        //types or keyword
                        stringBuilder.append("&keyword=" + placetxt);
                        stringBuilder.append("&key=" + getResources().getString(R.string.google_places_key));

                        String url = stringBuilder.toString();

                        Object[] datatransfer = new Object[2];
                        datatransfer[0] = gmap;
                        datatransfer[1] = url;
                        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                        googlePlacesReadTask.execute(datatransfer);
                        Log.e("URL", "" + url);
                        pDialog.dismiss();

                    } else {
                        pDialog.dismiss();
                        //This is what you need:
                        Log.e("error", "location null");
                        Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) getActivity());
                    }

                } catch (Exception e) {
                    pDialog.dismiss();
                    Log.e("map", "" + e);
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                }

            }
        });

        /**
         * search ends here
         */

        /**
         * view_on_page click listener
         */
        list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placetxt = "" + searchView.getQuery();

                if (!TextUtils.isEmpty(placetxt)) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getActivity()
                                , Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(getActivity()
                                , Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "Permission not granted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        locationManager = (LocationManager) getContext()
                                .getSystemService(LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));
                        Location location = locationManager.getLastKnownLocation(bestProvider);
                        if (location != null) {
                            getCurrentLocation();
                            Log.e("TAG", "GPS is on");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Double lat1 = latitude;
                            Double lon1 = longitude;
                            float lat11 = (float) latitude;
                            float lon11 = (float) longitude;
                            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                            stringBuilder.append("location=" + latitude + "," + longitude);
                            stringBuilder.append("&radius=" + 5000);
                            //types or keyword
                            stringBuilder.append("&keyword=" + placetxt);
                            stringBuilder.append("&key=" + getResources().getString(R.string.google_places_key));

                            String url = stringBuilder.toString();
                            //SharedPreferences instead of sqlite db
                            //this is used for the weather
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.clear();
                            editor.putFloat("lat", lat11);
                            editor.putFloat("lng", lon11);
                            editor.apply();
                            Intent intent = new Intent(getActivity(), InListView.class);
                            intent.putExtra("iurl", url);
                            intent.putExtra("lat1", lat1);
                            intent.putExtra("lon1", lon1);
                            startActivity(intent);

                        } else {
                            //This is what you need:
                            Log.e("error", "location null");
                            Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                            locationManager.requestLocationUpdates(bestProvider, 1000, 0, (android.location.LocationListener) getActivity());
                        }

                    } catch (Exception e) {
                        Log.e("map", "" + e);
                        Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                    }
                }//if close
                else {
                    Toast.makeText(getActivity(), "type in the search bar first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getCurrentLocation() {
        try {


            locationManager = (LocationManager) getContext()
                    .getSystemService(Context.LOCATION_SERVICE);
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
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE,  locationListener);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        drawMarker(location);
                    }
                }
                if (isGPSEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE,  locationListener);
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
        }//try closed
        catch (Exception e) {
            Log.e("ex",""+e);
            Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
        }


    }

    private void drawMarker(Location location) {
        if (gmap != null) {
            gmap.clear();
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            gmap.addMarker(new MarkerOptions()
                    .position(gps).title("YOU ARE HERE !"));
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 14));// previous zoom level 12
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
