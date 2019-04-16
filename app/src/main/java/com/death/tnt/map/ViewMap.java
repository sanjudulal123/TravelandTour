package com.death.tnt.map;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ViewMap extends Fragment {
    Button satellite, terrain, hybrid, normal;
    GoogleMap gmap;
    SearchView searchView;
    double lat, lng;
    RequestQueue requestQueue;
    String url = "http://maps.googleapis.com/maps/api/geocode/json?address=";
    //map desc
    TextView loc, desc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map,null);
        satellite = (Button) view.findViewById(R.id.satellite);
        terrain= (Button) view.findViewById(R.id.terrain);
        hybrid = (Button) view.findViewById(R.id.hybrid);
        normal = (Button) view.findViewById(R.id.normal);
        searchView = (SearchView) view.findViewById(R.id.search);
        requestQueue = Volley.newRequestQueue(getContext());
        //map desc
        loc = (TextView) view.findViewById(R.id.loc);
        desc = (TextView) view.findViewById(R.id.desc);
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.mapsfragment);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final GoogleMap googleMap) {
                gmap = googleMap;
                LatLng l = new LatLng(26.502943, 87.282757);
                googleMap.addMarker(new MarkerOptions().position(l));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 15));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("lat:"+latLng.latitude+","+"lng:"+latLng.longitude));
                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(latLng);
                        circleOptions.radius(10);
                        circleOptions.strokeColor(getResources().getColor(R.color.colorPrimary));
                        circleOptions.fillColor(getResources().getColor(R.color.colorAccent));
                        googleMap.addCircle(circleOptions);
                        lat = latLng.latitude;
                        lng = latLng.longitude;
                        getAddress(lat,lng);
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
                        Toast.makeText(getContext(), diatance/1000+"km", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url+searchView.getQuery(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject first = new JSONObject(response);
                            JSONArray ar = first.getJSONArray("results");
                            JSONObject second = ar.getJSONObject(0);
                            JSONObject third = second.getJSONObject("geometry");
                            JSONObject fourth = third.getJSONObject("location");
                            double a = fourth.getDouble("lat");
                            double p = fourth.getDouble("lng");
                            LatLng o = new LatLng(a, p);
                            gmap.addMarker(new MarkerOptions().position(o).title("i am here"));
                            float zoomlevel = 14.0f;
                            gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(o,zoomlevel));
                        }catch (Exception e){

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(stringRequest);



            }
        });





        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
        });

        hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }
        });

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
        });
        return view;


    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 6);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            loc.setText(obj.getLocality()+"\n"
                    +obj.getSubAdminArea());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getContext(), ""+ e, Toast.LENGTH_SHORT).show();
        }
    }
}