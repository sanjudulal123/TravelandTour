package com.death.tnt.favourite;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.death.tnt.home.DashboardActivity;
import com.death.tnt.signup.Signup;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class FavouritePlace extends Fragment {
    Button go, view_favourite;
    SearchView searchView;
    RequestQueue requestQueue;
    GoogleMap gmap;
    LocationManager locationManager = null;
    double latitude = 0,
            longitude = 0;
    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    final static int for_permission = 1;
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;


    //flag for WIFi
    boolean isWifiEnabled = false;


    //firebase database
    DatabaseReference databaseReference;

    //creating firebase auth
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map_favourite, null);
        if (!isGooglePlayServicesAvailable()) {
            Log.e("play service", "not available");
            Toast.makeText(getActivity(), "Google play services not available", Toast.LENGTH_SHORT).show();
        }
        searchView = (SearchView) view.findViewById(R.id.search);
        go = (Button) view.findViewById(R.id.go);
        view_favourite = (Button) view.findViewById(R.id.view_favourite);
        requestQueue = Volley.newRequestQueue(getContext());

        //get current user Userid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();

//        databaseReference = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child("user").child(userid).child("Favourite Place");


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
                            add = add + "\n" + obj.getSubAdminArea();//bagmati
                            add = add + "\n" + obj.getFeatureName();
                            add = add + "\n" + obj.getPremises();
                            Toast.makeText(getActivity(), "" + add, Toast.LENGTH_SHORT).show();
                            /**
                             * if the above code works
                             *
                             */
                            final String locality = obj.getLocality();//biratnagar
                            final String adminArea = obj.getAdminArea();//EDR
                            final String subAdminArea = obj.getSubAdminArea();//Koshi
                            final Double lati = obj.getLatitude();
                            final Double longi = obj.getLongitude();

//                            String ss = obj.getSubLocality();

                            Log.e("Location12", "" + locality + "\n" + adminArea
                                    + "\n" + subAdminArea);

                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                    .setTitle("Place:" + locality).setMessage(
                                            "Is this your favourite place?");
                            dialog.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            databaseReference = FirebaseDatabase
                                                    .getInstance()
                                                    .getReference()
                                                    .child("user").child(userid).child("Favourite Place");
                                            //add place to favourite place module and to database
                                            final FavouriteModule module = new FavouriteModule(locality + "," + subAdminArea, lati, longi);
                                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot data : dataSnapshot.getChildren())
                                                        if (data.child(locality + "," + subAdminArea).exists()) {
                                                            Log.e("place", "already added to favourite");
                                                            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                                                    .setTitle("Place: " + locality).setMessage(
                                                                            "Already in your favourite places.");
                                                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            }).show();
                                                        } else {
                                                            String favouriteplacekey = databaseReference.push().getKey();
                                                            databaseReference.child(favouriteplacekey).setValue(module).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                                                                            .setTitle("Success")
                                                                            .setMessage(locality + " is added to your favourite places.");
                                                                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    }).show();
                                                                }
                                                            });
                                                        }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();


                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        view_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewFavouriteList.class);
                startActivity(intent);
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
