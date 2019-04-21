package com.death.tnt.slidingTab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.death.tnt.DataModule;
import com.death.tnt.Nexample;
import com.death.tnt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class ADDnp extends AppCompatActivity {
    DatabaseReference databaseReference;
    EditText caption;
    Button addphoto, post;
    ImageView addNPfeedImage;
    private int PICK_IMAGE_REQUEST = 1;
    FirebaseStorage firebaseStorage;
    Uri uri;
    String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnewpost);
        caption = (EditText) findViewById(R.id.caption);
        addphoto = (Button) findViewById(R.id.addphoto);
        post = (Button) findViewById(R.id.post);
        addNPfeedImage = (ImageView) findViewById(R.id.addNpfeedImage);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                String userid = user.getUid();
                if (user != null) {
                    if (path != null){
                        final ProgressDialog progressDialog = new ProgressDialog(ADDnp.this);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        File f = new File(path);
                        uri = Uri.fromFile(f);
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        final StorageReference feedPhotoRef = storageRef.child("images/" + UUID.randomUUID().toString());
                        feedPhotoRef.putFile(uri)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Log.e("Firebase Storage", "Upload Failed");
                                        e.printStackTrace();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                String userid = user.getUid();
                                String downloadUrl = String.valueOf(feedPhotoRef.getDownloadUrl());
                                databaseReference = FirebaseDatabase
                                        .getInstance()
                                        .getReference()
                                        .child("user").child(userid);
                                DataModule dataModule = new DataModule();
                                dataModule.setFeedImageURL(uri.toString());
                                dataModule.setFeedPhotoDowloadUrl(downloadUrl);
                                dataModule.setCaption(caption.getText().toString());

                                databaseReference.setValue(dataModule).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(ADDnp.this,Feed.class);
                                        startActivity(intent);
                                        Log.e("Database NpFeedImage", "push Success");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        e.printStackTrace();
                                        Log.e("Database Exception", "" + e);
                                    }
                                });
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                            }
                        });
                    } else {
                        Log.e("File Path", " is null");
                    }
                } else {
                    Toast.makeText(ADDnp.this, "Please Login First", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ADDnp.this, Nexample.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"),
                        PICK_IMAGE_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == PICK_IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {

                uri = data.getData();
                path = getPathFromURI(uri);
                addNPfeedImage.setImageURI(uri);









//                if (path != null) {
//                    File f = new File(path);
//                    uri = Uri.fromFile(f);
//                    final ProgressDialog progressDialog = new ProgressDialog(this);
//                    progressDialog.setTitle("Uploading...");
//                    progressDialog.setCancelable(false);
//                    progressDialog.show();
//                    /**
//                     * Find a way to perform this method in post.OnClick
//                     */
//                    addNPfeedImage.setImageURI(uri);
//                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    if (user != null) {
//                        final Uri finalUri = uri;
//                        FirebaseStorage storage = FirebaseStorage.getInstance();
//                        StorageReference storageRef = storage.getReference();
//                        final StorageReference feedPhotoRef = storageRef.child("images/" + UUID.randomUUID().toString());
//                        feedPhotoRef.putFile(uri)
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
//                                        Log.e("Firebase Storage", "Upload Failed");
//                                        e.printStackTrace();
//                                    }
//                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                progressDialog.dismiss();
//                                String userid = user.getUid();
//                                String downloadUrl = String.valueOf(feedPhotoRef.getDownloadUrl());
//                                databaseReference = FirebaseDatabase
//                                        .getInstance()
//                                        .getReference()
//                                        .child("user").child(userid);
//                                DataModule dataModule = new DataModule();
//                                dataModule.setFeedImageURL(finalUri.toString());
//                                dataModule.setFeedPhotoDowloadUrl(downloadUrl);
//
//                                databaseReference.setValue(dataModule).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        progressDialog.dismiss();
//                                        Log.e("Database NpFeedImage", "push Success");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
//                                        e.printStackTrace();
//                                        Log.e("Database Exception", "" + e);
//                                    }
//                                });
//                            }
//                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
//                                        .getTotalByteCount());
//                                progressDialog.setMessage("Uploaded " + (int) progress + "%");
//                            }
//                        });
//
//                    } else {
//                        Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ADDnp.this, Nexample.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                } else {
//                    Log.e("File Path", " is null");
//                }











//
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//                addNPfeedImage.setImageBitmap(bitmap);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", "" + e);
        }
    }

    private String getPathFromURI(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
