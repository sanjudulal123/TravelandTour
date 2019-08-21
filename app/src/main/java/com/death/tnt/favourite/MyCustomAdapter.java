package com.death.tnt.favourite;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.R;
import com.death.tnt.photogallery.ImageUploadInfo;

import java.util.List;

public class MyCustomAdapter extends BaseAdapter {
    Context c;
    int layout;
    List<FavouriteModule> favouriteModuleList;
    public MyCustomAdapter(ViewFavouriteList viewFavouriteList, int viewfavouritelistitem, List<FavouriteModule> list) {
        c=viewFavouriteList;
        layout= viewfavouritelistitem;
        favouriteModuleList = list;
    }

    @Override
    public int getCount() {
        return favouriteModuleList.size();
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
        convertView = LayoutInflater.from(c).inflate(layout,null);
        FavouriteModule favouriteModule = favouriteModuleList.get(position);
        TextView placename = (TextView)convertView.findViewById(R.id.place);
        Button view_in_map = (Button)convertView.findViewById(R.id.view_in_map);
        Button description = (Button)convertView.findViewById(R.id.desc);
        Button removefromlist = (Button)convertView.findViewById(R.id.remove);

        placename.setText(favouriteModule.getPlace());
        double fav_lat = favouriteModule.getLati();
        double fav_lng = favouriteModule.getLongi();

        view_in_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "show in  map", Toast.LENGTH_SHORT).show();
            }
        });

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "description", Toast.LENGTH_SHORT).show();
            }
        });

        removefromlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "remove from list", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}
