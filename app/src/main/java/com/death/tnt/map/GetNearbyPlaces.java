package com.death.tnt.map;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {
    GoogleMap gmap;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;


    @Override
    protected String doInBackground(Object... objects) {
        gmap = (GoogleMap) objects[0];
        url = (String) objects[1];

        try {
            URL myurl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) myurl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            stringBuilder = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }


            data = stringBuilder.toString();

        } catch (Exception e) {
            Log.e("nearby",""+e);
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject parentObject = new JSONObject(s);
            JSONArray resultsArray = parentObject.getJSONArray("results");

            for (int i=0;i>resultsArray.length();i++){
                JSONObject jsonObject = resultsArray.getJSONObject(i);
                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");

                String latitude = locationObj.getString("lat");
                String longitude = locationObj.getString("lng");

                JSONObject nameObj = resultsArray.getJSONObject(i);
                String name_location = nameObj.getString("name");
                String vicinity = nameObj.getString("vicinity");

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(name_location);
                markerOptions.position(latLng);
                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                gmap.addMarker(markerOptions);
//                gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//
//                        return false;
//                    }
//                });

            }

        } catch(Exception e) {

            Log.e("exceptiongetnearbyplace",""+e);
        }
    }
}
