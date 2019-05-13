package com.death.tnt.slidingTab;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    SwipeRefreshLayout swipeRefreshLayout;
    FloatingActionButton floatingActionButton;
    //individual array for testing
//    List<String> arrname = new ArrayList<String>();
//    List<String> arrprofileurl = new ArrayList<String>();
//    List<String> arrfeedurl = new ArrayList<String>();
//    List<String> arrcaption = new ArrayList<String>();
//    //single array for testing
//    ArrayList<DataModule> arr = new ArrayList<>();
    ProgressDialog progressDialog;
    ArrayList<DataModule> a1 = new ArrayList<>();
    ArrayList<DataModule> a2 = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.feedlistview, null);
        siblingContext = getContext();
        context = getActivity();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userid = user.getUid();
        if (user != null) {

            databaseReference = FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("user");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    a1.clear();
                    a2.clear();

                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                        DataModule dataModule = d1.getValue(DataModule.class);
                        a1.add(dataModule);
                    }

                }
                    listView = (ListView) view.findViewById(R.id.list);
                    listView.setAdapter(new FeedAdapterExample(getActivity(), R.layout.feedlistviewitem, a1));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(siblingContext, "user not logged in", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.pullToRefresh);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(siblingContext, ADDnp.class);
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;

    }

}
