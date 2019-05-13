package com.death.tnt.slidingTab;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.death.tnt.DataModule;
import com.death.tnt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapterExample extends BaseAdapter {
    private Activity context;
    private int layout;
    private ArrayList<DataModule> arr1;

    public FeedAdapterExample(FragmentActivity activity, int feedlistviewitem, ArrayList<DataModule> a1) {
        context = activity;
        layout = feedlistviewitem;
        arr1 = a1;
    }

    @Override
    public int getCount() {

            return arr1.size();

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
        convertView = LayoutInflater.from(context).inflate(layout, null);
        ImageView profilepic = (ImageView) convertView.findViewById(R.id.profilePic);
        TextView txtname = (TextView) convertView.findViewById(R.id.name);
        TextView txttimestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView txtcaption = (TextView) convertView.findViewById(R.id.txtCaption);
        ImageView feedImage = (ImageView) convertView.findViewById(R.id.feedImage);
//        TextView txtviews = (TextView)convertView.findViewById(R.id.txtViews);

        txtname.setText(arr1.get(position).getName());
//        txttimestamp.setText(arr1.get(position).getTimestamp());
        txtcaption.setText(arr1.get(position).getCaption());
        String propic = arr1.get(position).getProfilepictureurl();
        Picasso.get().load(propic).into(profilepic);
        String fimage = arr1.get(position).getFeedPhotoDowloadUrl();
        Picasso.get().load(fimage).into(feedImage);

//        final DataModule dm = (DataModule)this.getItem(position);
//        txtname.setText(dm.getName());
//        txtcaption.setText(dm.getCaption());
//        String propic = dm.getProfilepictureurl();
//        Picasso.get().load(propic).into(profilepic);
//        String fimage = dm.getFeedPhotoDowloadUrl();
//        Picasso.get().load(fimage).into(feedImage);
//        txttimestamp.setText(dm.getTimestamp());

        return convertView;
    }
}
