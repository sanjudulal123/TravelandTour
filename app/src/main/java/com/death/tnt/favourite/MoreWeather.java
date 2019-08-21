package com.death.tnt.favourite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.death.tnt.R;

public class MoreWeather extends AppCompatActivity {
    TextView more_date, more_temp, min_temp, max_temp, more_rain, more_humidity, more_wind_speed,more_pressure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_weather);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String date = bundle.getString("date1");
        String rain = bundle.getString("rain1");
        double temp = bundle.getDouble("temp1");
        double min_temp1 = bundle.getDouble("min_temp");
        double max_temp1 = bundle.getDouble("max_temp");
        double pressure1 = bundle.getDouble("pressure");
        double humidity1 = bundle.getDouble("humidity");
        double wind_speed1 = bundle.getDouble("wind_speed");


        more_date.setText("Date : "+date);
        more_temp.setText("Temperature : "+temp);
        min_temp.setText("Min Temperature : "+min_temp1);
        max_temp.setText("Max Temperature : "+max_temp1);
        more_rain.setText("Rain : "+rain);
        more_humidity.setText("Humidity : "+humidity1);
        more_wind_speed.setText("Wind Speed : "+wind_speed1);
        more_pressure.setText("Pressure : "+pressure1);
    }
}
