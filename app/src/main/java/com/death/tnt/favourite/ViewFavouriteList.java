package com.death.tnt.favourite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.death.tnt.R;
import com.death.tnt.photogallery.ImageUploadInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;
import java.util.List;

public class ViewFavouriteList extends AppCompatActivity {
    JazzyListView jaz;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;
    List<FavouriteModule> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewfavouritelist);
        jaz=(JazzyListView) findViewById(R.id.jaz);
        jaz.setTransitionEffect(JazzyHelper.GROW);

        //get current user Userid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();

        // Setting up Firebase image upload folder path in databaseReference.
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("user").child(userid).child("Favourite Place");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    FavouriteModule favouriteModule = postSnapshot.getValue(FavouriteModule.class);

                    list.add(favouriteModule);
                }
                jaz.setAdapter(new MyCustomAdapter(ViewFavouriteList.this,R.layout.viewfavouritelistitem,list));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
