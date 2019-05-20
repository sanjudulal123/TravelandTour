package com.death.tnt.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.death.tnt.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GridHomeAdapter extends BaseAdapter {
    Context context;
    int layout;
    List<HomeInfoModule> homeInfoModuleList;
    public GridHomeAdapter(FragmentActivity home, int home_grid_view, List<HomeInfoModule> list) {
        context = home;
        layout = home_grid_view;
        homeInfoModuleList = list;

    }

    @Override
    public int getCount() {
        return homeInfoModuleList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout,null);
        HomeInfoModule data = homeInfoModuleList.get(position);

        ImageView imageview_cover_art = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
        final TextView place = (TextView)convertView.findViewById(R.id.place);//JOMSOM
        TextView place_district = (TextView)convertView.findViewById(R.id.place_district);//MUSTANG
        Button more = (Button)convertView.findViewById(R.id.more);

        final String image_cover_art_url = data.getPlace_cover_art_url();
        Picasso.get().load(image_cover_art_url).into(imageview_cover_art);

        place.setText(data.getPlace_name());
        place_district.setText(data.getPlace_district());
        final String place_desc = data.getPlace_description();
        final String place_full_name = data.getPlace_name()+","+data.getPlace_district();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MoreGrid.class);
                Bundle bundle = new Bundle();
                bundle.putString("place", String.valueOf(place));
                bundle.putString("desc",place_desc);
                bundle.putString("cover_art",image_cover_art_url);
                bundle.putString("place_full",place_full_name);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
