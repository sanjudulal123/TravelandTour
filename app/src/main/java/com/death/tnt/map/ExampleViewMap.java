package com.death.tnt.map;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.death.tnt.R;
import com.death.tnt.ViewMap.ViewOnPage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * From
 * https://gist.github.com/enginebai/adcae1f17d3b2114590c
 */
public class ExampleViewMap extends Fragment {
    //progress dialog
    ProgressDialog progressDialog;
    Button go, atms, hotels, view_on_page;
    final static int for_permission = 1;
    SearchView searchView;
    //    EditText radius;
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

        searchView = (SearchView) view.findViewById(R.id.search);
        go = (Button) view.findViewById(R.id.go);
        requestQueue = Volley.newRequestQueue(getContext());
        atms = (Button) view.findViewById(R.id.atms);
        hotels = (Button) view.findViewById(R.id.hotels);
//        radius = (EditText) view.findViewById(R.id.radius);
        view_on_page = (Button) view.findViewById(R.id.view_on_page);
//        final String radiustxt = radius.getText().toString();


        final SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                getCurrentLocation();
                gmap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                gmap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {

                        gmap.clear();
                        gmap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("lat:" + latLng.latitude + "\n" + "lng:" + latLng.longitude));
                        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));// previous zoom level 12
                        latitude = latLng.latitude;
                        longitude = latLng.longitude;
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            Address obj = addresses.get(0);
                            String add = obj.getAddressLine(0);
                            add = add + "\n" + obj.getCountryName();//NP
                            add = add + "\n" + obj.getAdminArea();//EDR
                            add = add + "\n" + obj.getLocality();
                            add = add + "\n" + obj.getLocale();
                            add = add + "\n" + obj.getSubLocality();
                            add = add + "\n" + obj.getSubAdminArea();
                            add = add + "\n" + obj.getFeatureName();
                            add = add + "\n" + obj.getPremises();
                            Toast.makeText(getActivity(), "" + add, Toast.LENGTH_SHORT).show();
                            /**
                             * if the above code works
                             * use intent to show the above data in next activity
                             */
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

            }
        });


/**
 * search starts here
 */
        //search on click
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "button clicked", Toast.LENGTH_SHORT).show();


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
//                        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//                        getNearbyPlaces.execute(datatransfer);
                        Log.e("URL", "" + url);
//                        drawMarker(location);
//                        new AlertDialog.Builder(getActivity())
//                                .setTitle("Info")
//                                .setMessage("Did you find the places you are looking for?")
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                //show some information
//                                dialog.dismiss();
//                            }
//                        });

                    } else {
                        //This is what you need:
                        Log.e("error", "location null");
                        Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) getActivity());
                    }

//                    getCurrentLocation();
//                    gmap.setMyLocationEnabled(true);
//                    Location myLocation = gmap.getMyLocation();
//                    StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//                    stringBuilder.append("location=" + myLocation.getLatitude() + "," + myLocation.getLongitude());
//                    stringBuilder.append("&radius=" + 1000);
//                    //types or keyword
//                    stringBuilder.append("&keyword=" + placetxt);
//                    stringBuilder.append("&key" + getResources().getString(R.string.google_places_key));
//
//                    String url = stringBuilder.toString();
//
//                    Object[] datatransfer = new Object[2];
//                    datatransfer[0] = gmap;
//                    datatransfer[1] = url;
//                    GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//                    getNearbyPlaces.execute(datatransfer);
                } catch (Exception e) {
                    Log.e("map", "" + e);
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
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

        /**
         * search ends here
         */

        /**
         atm click
         */


        atms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        stringBuilder.append("&keyword=atm");
                        stringBuilder.append("&key=" + getResources().getString(R.string.google_places_key));

                        String url = stringBuilder.toString();

                        Object[] datatransfer = new Object[2];
                        datatransfer[0] = gmap;
                        datatransfer[1] = url;
                        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                        googlePlacesReadTask.execute(datatransfer);
                        Log.e("URL", "" + url);
                    } else {
                        //This is what you need:
                        Log.e("error", "location null");
                        Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) getActivity());
                    }
                } catch (Exception e) {
                    Log.e("map", "" + e);
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
        });


        /**
         * hotels click
         */


        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        stringBuilder.append("&keyword=hotel");
                        stringBuilder.append("&key=" + getResources().getString(R.string.google_places_key));

                        String url = stringBuilder.toString();

                        Object[] datatransfer = new Object[2];
                        datatransfer[0] = gmap;
                        datatransfer[1] = url;
                        GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
                        googlePlacesReadTask.execute(datatransfer);
                        Log.e("URL", "" + url);
                    } else {
                        //This is what you need:
                        Log.e("error", "location null");
                        Toast.makeText(getActivity(), "location null", Toast.LENGTH_SHORT).show();
                        locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) getActivity());
                    }
                } catch (Exception e) {
                    Log.e("map", "" + e);
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_LONG).show();
                }
            }
        });

        /**
         * view_on_page click listener
         */


        view_on_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "there's nothing to show", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), ViewOnPage.class);
//                startActivity(intent);
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
