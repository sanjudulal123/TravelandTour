package com.death.tnt.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.death.tnt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoreGrid extends AppCompatActivity {
    ImageView image_cover_pic;
    TextView place_name, place_rating, place_name_full, desc;
    EditText comments;
    Button send_comments;
    ListView listview_comments;

    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating List of ImageUploadInfo class.
    List<HomeInfoModule> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moregrid);
        image_cover_pic =(ImageView)findViewById(R.id.image_cover_pic);
        place_name = (TextView) findViewById(R.id.place_name);
        place_rating = (TextView) findViewById(R.id.place_rating);
        place_name_full = (TextView) findViewById(R.id.place_name_full);
        desc = (TextView) findViewById(R.id.desc);
        comments = (EditText) findViewById(R.id.comments);
        send_comments = (Button) findViewById(R.id.send_comments);
        listview_comments = (ListView) findViewById(R.id.listview_comments);
        try {
            Bundle extras = getIntent().getExtras();
            Picasso.get().load(extras.getString("cover_art")).into(image_cover_pic);
            place_name.setText(extras.getString("place"));
            desc.setText(extras.getString("desc"));
            place_name_full.setText(extras.getString("place_full"));
        }catch(Exception ex){
            Toast.makeText(this, "error getting values", Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("home")
                .child(String.valueOf(place_name));
//        button_rating.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //show dialog with 5 buttons from 1-5
//                //and get value from button and save to database under the node defined as above
//            }
//        });
        //show ratings from database


    }
}
