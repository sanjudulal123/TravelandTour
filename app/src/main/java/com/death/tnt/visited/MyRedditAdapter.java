package com.death.tnt.visited;

import android.content.Context;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.death.tnt.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class MyRedditAdapter extends BaseAdapter {
    Context c;
    int layout;
    ArrayList<RedditModule> d;
    public MyRedditAdapter(FragmentActivity activity, int reddit_list_view_item, ArrayList<RedditModule> ardata) {
        c = activity;
        layout = reddit_list_view_item;
        d = ardata;
    }

    @Override
    public int getCount() {
        return d.size();
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
        view = LayoutInflater.from(c).inflate(layout,null);
        TextView reddit_caption = (TextView)view.findViewById(R.id.reddit_caption);
        TextView reddit_score = (TextView)view.findViewById(R.id.reddit_score);
        TextView reddit_no_of_comments = (TextView)view.findViewById(R.id.reddit_no_of_comments);
        ImageView reddit_image = (ImageView)view.findViewById(R.id.reddit_image);
        ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        reddit_caption.setText(d.get(position).getCaption());
        reddit_score.setText("Score: "+d.get(position).getScore().toString());
        reddit_no_of_comments.setText("Comments: "+d.get(position).getNo_of_comments().toString());
        String image_url = d.get(position).getImage_url();
        Picasso.get().load(image_url).into(reddit_image);
        progressBar.setVisibility(View.INVISIBLE);


        reddit_no_of_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, "display comments", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
