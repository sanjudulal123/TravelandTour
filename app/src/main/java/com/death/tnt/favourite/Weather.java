package com.death.tnt.favourite;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.death.tnt.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class Weather extends Fragment {

    ListView weather_list;
    //step 1: definng a RequestQueue
    RequestQueue requestqueue;
    ArrayList<WeatherModule> weatherdata = new ArrayList<>();
    ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_weather, null);
        weather_list = (ListView) view.findViewById(R.id.weather_list);
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        Float lat = (Float) prefs.getFloat("lat", Float.parseFloat(""));
        Float lon = (Float) prefs.getFloat("lng", Float.parseFloat(""));
        StringBuilder stringBuilder = new StringBuilder("https://api.openweathermap.org/data/2.5/forecast?");
        stringBuilder.append("lat=" + lat + "&lon=" + lon);
        stringBuilder.append("&appid=28c2483014b61a55de2975c872f8d0ca");
        //step 2: define a URL source
        String url = stringBuilder.toString();
        //step 3 : Initializing requestqueue
        requestqueue = Volley.newRequestQueue(getContext());
        //step 4: defining a stringrequest
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    WeatherModule weatherModule = new WeatherModule();
                    JSONObject first = new JSONObject(response);
                    JSONArray listarray = first.getJSONArray("list");
                    for (int i = 0; i < listarray.length(); i++) {
                        JSONObject second = listarray.getJSONObject(i);
                        JSONObject main = second.getJSONObject("main");
                        Double temp = main.getDouble("temp");
                        Double min_temp = main.getDouble("temp_min");
                        Double max_temp = main.getDouble("temp_max");
                        Double pressure = main.getDouble("pressure");
                        Double humidity = main.getDouble("humidity");
                        JSONArray weatherarray = second.getJSONArray("weather");
                        for (i = 0; i < weatherarray.length(); i++) {
                            JSONObject third = weatherarray.getJSONObject(i);
                            String rain_desc = third.getString("description");
                            weatherModule.setRain_description(rain_desc);
                        }
                        JSONObject wind = second.getJSONObject("wind");
                        Double wind_speed = wind.getDouble("speed");
                        String date = second.getString("dt_txt");

                        weatherModule.setWind_speed(wind_speed);
                        weatherModule.setDate(date);
                        weatherModule.setTemperature(temp);
                        weatherModule.setMin_temperature(min_temp);
                        weatherModule.setMax_temperature(max_temp);
                        weatherModule.setPressure(pressure);
                        weatherModule.setHumidity(humidity);

                        weatherdata.add(weatherModule);
                    }
                    pDialog.dismiss();
                    weather_list.setAdapter(new WeatherAdapter(getActivity(), R.layout.weather_adapter, weatherdata));
                } catch (Exception e) {
                    pDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
