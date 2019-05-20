package com.death.tnt.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.death.tnt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    GridView gridView;
    // Creating DatabaseReference.
    DatabaseReference databaseReference;
    // Creating List of ImageUploadInfo class.
    List<HomeInfoModule> list = new ArrayList<>();
    // Creating Progress dialog
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home,null);
        gridView = (GridView)view.findViewById(R.id.gridview);
        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(getContext());

        // Setting up message in Progress dialog.
        progressDialog.setMessage("Loading Images...");
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("home");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    HomeInfoModule homeInfoModule = postSnapshot.getValue(HomeInfoModule.class);

                    list.add(homeInfoModule);
                }
                gridView.setAdapter(new GridHomeAdapter(getActivity(),R.layout.home_grid_view,list));


                // Hiding the progress dialog.
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
