package com.death.tnt.visited;

import android.content.Context;
import android.content.Intent;
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
//        TextView reddit_score = (TextView)view.findViewById(R.id.reddit_score);
//        TextView reddit_no_of_comments = (TextView)view.findViewById(R.id.reddit_no_of_comments);
        ImageView reddit_image = (ImageView)view.findViewById(R.id.reddit_image);
        reddit_caption.setText(d.get(position).getCaption());
//        reddit_score.setText("Score: "+d.get(position).getScore());
//        reddit_no_of_comments.setText("Comments: "+d.get(position).getNo_of_comments());
        final String image_url = d.get(position).getImage_url();
        try{
            Picasso.get().load(image_url).fit().placeholder(R.drawable.progress_animation).error(R.drawable.ic_warning_black_48dp).into(reddit_image);
        }catch(Exception e){
            Toast.makeText(c, "some images had trouble loading pictures", Toast.LENGTH_SHORT).show();
        }
        reddit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c,FullImage.class);
                intent.putExtra("url",image_url);
                c.startActivity(intent);
            }
        });


        return view;
    }
}
