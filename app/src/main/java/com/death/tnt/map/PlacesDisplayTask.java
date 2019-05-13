package com.death.tnt.map;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {
    JSONObject googlePlacesJson;
    GoogleMap gmap;

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... objects) {
        List<HashMap<String, String>> googlePlacesList = null;
        Places placeJsonParser = new Places();

        try {
            gmap = (GoogleMap) objects[0];
            googlePlacesJson = new JSONObject((String) objects[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        gmap.clear();
        for (int i = 0; i < list.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            String ratings = googlePlace.get("rating");
            String total_user_ratings = googlePlace.get("user_ratings_total");
            String open_now = googlePlace.get("open_now");
            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title("Name: " + placeName + " : " + vicinity
                    + "\n" + "rating : " + ratings
                    + "\n" + "Total User Rating: " + total_user_ratings
                    + "\n" + "Open now" + open_now);
            gmap.addMarker(markerOptions);
        }
    }
}
