package com.death.tnt.home;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.death.tnt.R;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    int layout;
    String[] textview;
    int[] images;

    public GridViewAdapter(FragmentActivity activity,
                           int gridview,
                           int[] imagesforgridview,
                           String[] textforgridview) {
        context = activity;
        layout = gridview;
        textview = textforgridview;
        images = imagesforgridview;

    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(layout, null);
//         ImageView imageView = (ImageView)convertView.findViewById(R.id.gridimageview);
        TextView textView = (TextView) convertView.findViewById(R.id.gridtextview);
        /**
         * Now Get images URL and Text from Database as Array
         * and use PICASSO to display image
         *  For Testing we are using something else
         */
        textView.setText(textview[position]);
        return convertView;
    }
}
