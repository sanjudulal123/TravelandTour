package com.death.tnt.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.death.tnt.DataModule;
import com.death.tnt.R;
import com.death.tnt.slidingTab.FeedAdapterExample;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MoreGrid extends AppCompatActivity {
    ImageView image_cover_pic;
    TextView place_name, place_rating, place_name_full, desc;
    EditText commentss;
    Button send_comments;
    ListView listview_comments;

    FirebaseAuth mAuth;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    // Creating List of ImageUploadInfo class.
    ArrayList<GridComments> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moregrid);
        image_cover_pic =(ImageView)findViewById(R.id.image_cover_pic);
        place_name = (TextView) findViewById(R.id.place_name);
        place_rating = (TextView) findViewById(R.id.place_rating);
        place_name_full = (TextView) findViewById(R.id.place_name_full);
        desc = (TextView) findViewById(R.id.desc);
        commentss = (EditText) findViewById(R.id.comments);
        send_comments = (Button) findViewById(R.id.send_comments);
        listview_comments = (ListView) findViewById(R.id.listview_comments);
        mAuth = FirebaseAuth.getInstance();
        try {
            Bundle extras = getIntent().getExtras();
            Picasso.get().load(extras.getString("cover_art")).into(image_cover_pic);
            place_name.setText(extras.getString("place"));
            desc.setText(extras.getString("desc"));
            place_name_full.setText(extras.getString("place_full"));
            place_rating.setText(extras.getString("place_rating"));
        }catch(Exception ex){
            Toast.makeText(this, "error getting values : "+ex, Toast.LENGTH_SHORT).show();
        }
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("home")
                .child(place_name.toString());
        final String comments = commentss.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        Date c = Calendar.getInstance().getTime();
        String time = c.toString();
        final GridComments gridComments = new GridComments();
        gridComments.setComments(comments);
        gridComments.setDate_time(time);
        gridComments.setUserid(userid);
        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("home")
                .child(String.valueOf(place_name)).child("reviews").child(userid);

        send_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        databaseReference.push().setValue(gridComments);

                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                                GridComments gridComments1 = d1.getValue(GridComments.class);
                                list.add(gridComments1);
                            }

                        }
                        listview_comments.setAdapter(new GridCommentsAdapter(MoreGrid.this, R.layout.gridcommentslist, list));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}
