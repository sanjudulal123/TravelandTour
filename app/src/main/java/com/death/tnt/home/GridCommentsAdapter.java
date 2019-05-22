package com.death.tnt.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.death.tnt.R;

import java.util.ArrayList;


/**
 * continue
 */
public class GridCommentsAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<GridComments> list1;
    public GridCommentsAdapter(MoreGrid moreGrid, int gridcommentslist, ArrayList<GridComments> list) {
        context = moreGrid;
        layout = gridcommentslist;
        list1 = list;
    }

    @Override
    public int getCount() {
        return list1.size();
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
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView timestamp = (TextView)convertView.findViewById(R.id.timestamp);
        TextView comments = (TextView)convertView.findViewById(R.id.comments);

        name.setText(list1.get(position).getUserid());
        timestamp.setText(list1.get(position).getDate_time());
        comments.setText(list1.get(position).getComments());


        return convertView;
    }
}
