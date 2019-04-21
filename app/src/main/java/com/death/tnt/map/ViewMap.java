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
//                        CircleOptions circleOptions = new CircleOptions();
//                        circleOptions.center(latLng);
//                        circleOptions.radius(0);
//                        circleOptions.strokeColor(getResources().getColor(R.color.colorPrimary));
//                        circleOptions.fillColor(getResources().getColor(R.color.colorAccent));
//                        googleMap.addCircle(circleOptions);
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

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url + searchView.getQuery(), new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject first = new JSONObject(response);
//                            JSONArray ar = first.getJSONArray("results");
//                            JSONObject second = ar.getJSONObject(0);
//                            JSONObject third = second.getJSONObject("geometry");
//                            JSONObject fourth = third.getJSONObject("location");
//                            double a = fourth.getDouble("lat");
//                            double p = fourth.getDouble("lng");
//                            LatLng o = new LatLng(a, p);
//                            gmap.addMarker(new MarkerOptions().position(o).title("i am here"));
//                            float zoomlevel = 14.0f;
//                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(o, zoomlevel));
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//                requestQueue.add(stringRequest);
//
//
//            }
//        });


//        satellite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
//            }
//        });

//        terrain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gmap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//            }
//        });
//
//        hybrid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            }
//        });

//        normal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
//                        .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                Log.e("called", "normal");
//                gmap.clear();
//                gmap.setMyLocationEnabled(true);
//                Location myLocation = gmap.getMyLocation();
//                LatLng latLng = new LatLng(lat, lng);
//                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//                stringBuilder.append("location=" + latLng.latitude + "," + latLng.longitude);
//                stringBuilder.append("&radius=" + 10000);
//                stringBuilder.append("&keyword=" + "restaurant");
//                stringBuilder.append("&key" + getResources().getString(R.string.google_places_key));
//
//                String url = stringBuilder.toString();
//
//                Object[] datatransfer = new Object[2];
//                datatransfer[0] = gmap;
//                datatransfer[1] = url;
//                GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();
//                getNearbyPlaces.execute(datatransfer);
//            }
//        });
        return view;


    }

//    public void getAddress(double lat, double lng) {
//        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 6);
//            Address obj = addresses.get(0);
//            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();//NP
//            add = add + "\n" + obj.getAdminArea();//EDR
//            add = add + "\n" + obj.getLocality();
//
//            String locality = obj.getLocality();
//            String adminArea = obj.getAdminArea();
//
//
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
//        }
//    }


//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        request = new LocationRequest().create();
//        request.setInterval(1000);
//        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
//                .checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(client, request, this);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        if (location == null) {
//            Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show();
//        } else {
//
//            LatLng l = new LatLng(location.getLatitude(), location.getLongitude());
//            gmap.addMarker(new MarkerOptions().position(l).title("current location"));
//            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(l, 15));
//        }
//    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        gmap = googleMap;
//
//        client = new GoogleApiClient.Builder(context)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//        client.connect();
//    }

//    public void findRestaurents() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        gmap.setMyLocationEnabled(true);
//        Location myLocation = gmap.getMyLocation();
//        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        stringBuilder.append("location="+myLocation.getLatitude()+","+myLocation.getLongitude());
//        stringBuilder.append("&radius="+1000);
//        stringBuilder.append("&keyword="+"restaurant");
//        stringBuilder.append("&key"+getResources().getString(R.string.google_places_key));
//
//        String url = stringBuilder.toString();
//
//        Object[] datatransfer = new Object[2];
//        datatransfer[0]=gmap;
//        datatransfer[1]=url;
//        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();
//        getNearbyPlaces.execute(datatransfer);
//    }

}
