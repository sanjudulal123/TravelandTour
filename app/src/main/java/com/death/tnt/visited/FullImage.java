package com.death.tnt.visited;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.death.tnt.R;
import com.squareup.picasso.Picasso;

public class FullImage extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);
        imageView = (ImageView)findViewById(R.id.full_image);
        Bundle extras = getIntent().getExtras();
        String url = extras.getString("url");
        Picasso.get().load(url).placeholder(R.drawable.progress_animation).into(imageView);
        Toast.makeText(this, "press back button to go back", Toast.LENGTH_SHORT).show();

    }
}
