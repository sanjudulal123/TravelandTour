package com.death.tnt.map;

import android.Manifest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InListAdapter extends BaseAdapter {
    Context c;
    int layout;
    ArrayList<InListModule> in;

    public InListAdapter(InListView inListView, int in_list_adapter, ArrayList<InListModule> listdata) {
        c = inListView;
        layout = in_list_adapter;
        in = listdata;
    }

    @Override
    public int getCount() {
        return in.size();
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
        TextView name_location = (TextView) view.findViewById(R.id.name_location);
        TextView rating = (TextView) view.findViewById(R.id.rating);
        TextView location = (TextView) view.findViewById(R.id.location);
        TextView open_now = (TextView) view.findViewById(R.id.open_now);
        ImageView icon_image = (ImageView) view.findViewById(R.id.icon_image);
        TextView distance = (TextView) view.findViewById(R.id.distance);
        Button view_map = (Button)view.findViewById(R.id.view_map);
        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get two lat and lon and show in map and later show route between two location
                Toast.makeText(c, "future update", Toast.LENGTH_SHORT).show();
            }
        });
        Double lat1 = in.get(position).getLat1();
        Double lon1 = in.get(position).getLng1();
        Double lat2 = in.get(position).getLat2();
        Double lon2 = in.get(position).getLng2();
//        distance.setText("Distance : " +);
        double theta = lon1 - lon2;
            double dist = Math.sin(lat1* Math.PI / 180.0)
                    * Math.sin(lat2* Math.PI / 180.0)
                    + Math.cos(lat1* Math.PI / 180.0)
                    * Math.cos(lat2* Math.PI / 180.0)
                    * Math.cos(theta* Math.PI / 180.0);
            dist = Math.acos(dist);
            dist = dist* Math.PI / 180.0;
            dist = dist * 60 * 1.1515;
            distance.setText("Distance : " +dist+" KM");


        String icon_url = in.get(position).getIcon_url();
        Picasso.get().load(icon_url).into(icon_image);

        name_location.setText(in.get(position).getName());
        rating.setText("Rating : " + in.get(position).getRating());
        location.setText("Location : " + in.get(position).getLocation());
        if (in.get(position).isOpen_now()) {
            open_now.setText("State : Open");
        } else {
            open_now.setText("State : Closed");
        }

        return view;
    }
}
