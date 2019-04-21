package com.death.tnt.slidingTab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.death.tnt.DataModule;
import com.death.tnt.R;
import com.death.tnt.signup.Signup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Feed extends Fragment {
    Context siblingContext;
    Activity context;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    ListView listView;
    List<String> arrname = new ArrayList<String>();
    List<String> arrprofileurl = new ArrayList<String>();
    List<String> arrfeedurl = new ArrayList<String>();
    List<String> arrcaption = new ArrayList<String>();
    Button addNP, refresh;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.feedlistview, null);
        siblingContext = getContext();
        context = getActivity();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("user")
                .child(userid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<DataModule> arr = new ArrayList<>();
//                ArrayList<DataModule> arrname = new ArrayList<DataModule>();
//                ArrayList<DataModule> arrprofileurl = new ArrayList<DataModule>();
//                ArrayList<DataModule> arrfeedurl = new ArrayList<DataModule>();
//                ArrayList<DataModule> arrcaption = new ArrayList<DataModule>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DataModule dataModule = ds.getValue(DataModule.class);
                    String name = dataModule.getName();
                    String profileURL = dataModule.getProfilepictureurl();
                    String feedImageUrl = dataModule.getFeedImageURL();
                    String caption = dataModule.getCaption();
                    arrname.add(name);
                    arrprofileurl.add(profileURL);
                    arrfeedurl.add(feedImageUrl);
                    arrcaption.add(caption);

                    Log.e("This is the Error", "Can't convert " +
                            "object of type java.lang.String" +
                            " to type com.death.tnt.DataModule");
                }
                listView = (ListView) view.findViewById(R.id.list);
                listView.setAdapter(new FeedAdapter(getActivity(), R.layout.feedlistviewitem, arrname, arrprofileurl, arrfeedurl, arrcaption));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addNP = (Button) view.findViewById(R.id.addNP);
        refresh = (Button) view.findViewById(R.id.refresh);


        addNP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(siblingContext, ADDnp.class);
                startActivity(intent);
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(siblingContext, "Refreshed", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}
