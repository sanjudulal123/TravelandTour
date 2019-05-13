package com.death.tnt.slidingTab;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.DataModule;
import com.death.tnt.Nexample;
import com.death.tnt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends Fragment {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    TextView name, address, bio, likes;
    ImageView proimage;
    Button edit_info;
    Context context;
    //Dialog
    EditText dialog_address, dialog_bio;
    Button dialog_btn_save;
    //firebase
    String userid;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userid = user.getUid();
//        if (user != FirebaseAuth.getInstance().getCurrentUser()) { }
        View view = inflater.inflate(R.layout.profile_example, null);
        context = getActivity();
        name = (TextView) view.findViewById(R.id.textViewProfileName);
        address = (TextView) view.findViewById(R.id.textViewAddress);
        bio = (TextView) view.findViewById(R.id.textViewBio);
        likes = (TextView) view.findViewById(R.id.prolikes);
        proimage = (ImageView) view.findViewById(R.id.imageProfile);
        edit_info = (Button) view.findViewById(R.id.edit_info);
        //everything null until added
        address.setText("");
        bio.setText("");
        likes.setText("");

        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.add_info);
                dialog.setCancelable(false);
                dialog.show();
                dialog_address = (EditText) dialog.findViewById(R.id.add_address);
                dialog_bio = (EditText) dialog.findViewById(R.id.add_bio);
                dialog_btn_save = (Button) dialog.findViewById(R.id.btn_save);
                if (dialog_address != null && dialog_bio != null) {

                    dialog_btn_save.setEnabled(true);
                    dialog_btn_save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            userid = user.getUid();
                            if (user != null) {

                                final String up_add = dialog_address.getText().toString();
                                final String up_bio = dialog_bio.getText().toString();
                                databaseReference = FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("user")
                                        .child(userid);
                                Log.e("userid",""+userid);
                                final DataModule dataModule = new DataModule();
                                dataModule.setUser_address(up_add);
                                dataModule.setUser_bio(up_bio);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        dataSnapshot.getRef().child("address").setValue(dataModule);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                });
//                                databaseReference.setValue(dataModule)
//                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                Log.e("Push to database","success");
//                                                dialog.dismiss();
//                                            }
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        dialog.dismiss();
//                                        Log.e("Firebase Database", "Exception : " + e);
//                                    }
//                                });


                            } else {
                                dialog.dismiss();
                                Log.e("Firebase", "user is null");
                                Intent intent = new Intent(context, Nexample.class);
                                startActivity(intent);

                            }
                        }
                    });
                } else {
                    dialog_btn_save.setEnabled(false);
                }

            }
        });

        return view;
    }
}