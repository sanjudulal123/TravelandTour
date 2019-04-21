package com.death.tnt.slidingTab;

import android.app.Activity;
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
import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    private List<String> name;
    private List<String> profilrurl;
    private List<String> feedImageUrl;
    private List<String> caption;

    public FeedAdapter(FragmentActivity activity, int feedlistviewitem, List<String> arrcaption, List<String> arrfeedurl, List<String> arrname, List<String> arrprofileurl) {
        context = activity;
        layout = feedlistviewitem;
        name = arrname;
        profilrurl = arrprofileurl;
        feedImageUrl = arrfeedurl;
        caption = arrcaption;
    }

    @Override
    public int getCount() {
        return name.size();
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
        ImageView profilepic = (ImageView)convertView.findViewById(R.id.profilePic);
        TextView txtname = (TextView)convertView.findViewById(R.id.name);
        TextView txttimestamp = (TextView)convertView.findViewById(R.id.timestamp);
        TextView txtcaption = (TextView)convertView.findViewById(R.id.txtCaption);
        ImageView feedImage = (ImageView)convertView.findViewById(R.id.feedImage);
        TextView txtviews = (TextView)convertView.findViewById(R.id.txtViews);

        //Image
        String profileImageurl = String.valueOf(profilrurl);
        Picasso.get().load(profileImageurl).into(profilepic);
        //Name
        txtname.setText(Integer.valueOf(String.valueOf(name)));
        //Feed Image
        String feedimageurl = String.valueOf(feedImageUrl);
        Picasso.get().load(feedimageurl).into(feedImage);
        //caption
        txtcaption.setText((CharSequence) caption);

        return convertView;
    }
}
