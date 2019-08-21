package com.death.tnt.favourite;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.death.tnt.R;

import java.util.ArrayList;

class WeatherAdapter extends BaseAdapter {
    Context c;
    int layout;
    ArrayList<WeatherModule> we;

    public WeatherAdapter(FragmentActivity activity, int weather_adapter, ArrayList<WeatherModule> weatherdata) {
        c = activity;
        layout = weather_adapter;
        we = weatherdata;
    }

    @Override
    public int getCount() {
        return we.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(c).inflate(layout, null);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView temp = (TextView) view.findViewById(R.id.temp);
        TextView rain = (TextView) view.findViewById(R.id.rain);
        Button more = (Button) view.findViewById(R.id.more);
        date.setText("Date : " + we.get(position).getDate());
        temp.setText("Temperature : " + (int) we.get(position).getTemperature());
        rain.setText("Rain : " + we.get(position).getRain_description());

        final String date1 = we.get(position).getDate();
        final String rain1 = we.get(position).getRain_description();
        final double temp1 = we.get(position).getTemperature();
        final double min_temp = we.get(position).getMin_temperature();
        final double max_temp = we.get(position).getMax_temperature();
        final double pressure = we.get(position).getPressure();
        final double humidity = we.get(position).getHumidity();
        final double wind_speed = we.get(position).getWind_speed();
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, MoreWeather.class);
                intent.putExtra("date1", date1);
                intent.putExtra("rain1", rain1);
                intent.putExtra("temp1", temp1);
                intent.putExtra("min_temp", min_temp);
                intent.putExtra("max_temp", max_temp);
                intent.putExtra("pressure", pressure);
                intent.putExtra("humidity", humidity);
                intent.putExtra("wind_speed", wind_speed);
                c.startActivity(intent);
            }
        });


        return view;
    }
}
