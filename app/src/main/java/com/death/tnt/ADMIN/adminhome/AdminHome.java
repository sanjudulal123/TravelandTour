package com.death.tnt.ADMIN.adminhome;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.death.tnt.R;
import com.death.tnt.home.Home;
import com.death.tnt.home.HomeInfoModule;
import com.death.tnt.photogallery.ImageUploadInfo;
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

import org.w3c.dom.Text;

import java.io.IOException;

public class AdminHome extends AppCompatActivity {
    // Folder path for Firebase Storage.
    String Storage_Path = "Home_Gallery/";

    // Creating button.
    Button ChooseButton, UploadButton;

    // Creating EditText.
    EditText ImageNameEditText, District_name, placeDescriptionEditText;

    //spinner
    Spinner placeRating;

    // Creating ImageView.
    ImageView SelectImage;

    // Creating URI.
    Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    StorageReference storageReference;
    DatabaseReference databaseReference;

    //creating firebase auth
    FirebaseAuth mAuth;

    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhome);
        // Assign FirebaseStorage instance to storageReference.
        storageReference = FirebaseStorage.getInstance().getReference();

        //get current user Userid
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        //Assign ID'S to button.
        ChooseButton = (Button) findViewById(R.id.ButtonChooseImage);
        UploadButton = (Button) findViewById(R.id.ButtonUploadImage);

        // Assign ID's to EditText.
        ImageNameEditText = (EditText) findViewById(R.id.ImageNameEditText);
        District_name = (EditText) findViewById(R.id.District_name);
        placeDescriptionEditText = (EditText) findViewById(R.id.placeDescriptionEditText);
        placeRating = (Spinner) findViewById(R.id.placeRating);
        String[] items = new String[]{"1", "2", "3", "4", "4.5", "5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        placeRating.setAdapter(adapter);



        // Assign ID'S to image view.
        SelectImage = (ImageView) findViewById(R.id.ShowImageView);

        // Assigning Id to ProgressDialog.
        progressDialog = new ProgressDialog(AdminHome.this);

        // Adding click listener to Choose image button.
        ChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

            }
        });
        // Adding click listener to Upload image button.
        UploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Calling method to upload selected image on Firebase storage.
                UploadImageFileToFirebaseStorage();

            }
        });

    }

    private void UploadImageFileToFirebaseStorage() {

        try {

            // Checking whether FilePathUri Is empty or not.
            if (FilePathUri != null) {

                // Setting progressDialog Title.
                progressDialog.setTitle("Image is Uploading...");

                // Showing progressDialog.
                progressDialog.show();

                // Creating second StorageReference.
                final StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

                //Adding every code in try catch to see if it is working
                // Adding addOnSuccessListener to second StorageReference.
                storageReference2nd.putFile(FilePathUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                            // Getting image name from EditText and store into string variable.
//                            String TempImageName = ImageName.getText().toString().trim();

                                //Getting Download URL
                                storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Getting image name from EditText and store into string variable.
                                        String TempPlaceName = ImageNameEditText.getText().toString();
                                        String TempDistrict = District_name.getText().toString();
                                        String TempPlaceDesc = placeDescriptionEditText.getText().toString();
                                        if (!TextUtils.isEmpty(TempPlaceName)
                                                && !TextUtils.isEmpty(TempDistrict)
                                                && !TextUtils.isEmpty(TempPlaceDesc)) {

                                            // Hiding the progressDialog after done uploading.
                                            progressDialog.dismiss();

                                            // Showing toast message after done uploading.
                                            Toast.makeText(AdminHome.this, "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();

//                                        @SuppressWarnings("VisibleForTests")
//                                        ImageUploadInfo imageUploadInfo = new ImageUploadInfo(TempImageName, uri.toString());

                                            String value = placeRating.getSelectedItem().toString();

                                            HomeInfoModule homeInfoModule = new HomeInfoModule();
                                            homeInfoModule.setPlace_cover_art_url(uri.toString());
                                            homeInfoModule.setPlace_description(TempPlaceDesc);
                                            homeInfoModule.setPlace_district(TempDistrict);
                                            homeInfoModule.setPlace_name(TempPlaceName);
                                            homeInfoModule.setPlace_rating(value);

                                            databaseReference = FirebaseDatabase
                                                    .getInstance()
                                                    .getReference()
                                                    .child("home");
                                            // Getting image upload ID.
                                            //use place name instead of push().get(key);
                                            String ImageUploadId = databaseReference.push().getKey();


                                            // Adding image upload id s child element into databaseReference.
                                            databaseReference.child(ImageUploadId).setValue(homeInfoModule);
                                            ImageNameEditText.setText("");
                                            District_name.setText("");
                                            placeDescriptionEditText.setText("");

                                        } else {
                                            Toast.makeText(AdminHome.this, "insert descriptions", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        // If something goes wrong .
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                                // Hiding the progressDialog.
                                progressDialog.dismiss();

                                // Showing exception error message.
                                Toast.makeText(AdminHome.this,
                                        exception.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        })

                        // On progress change upload time.
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                // Setting progressDialog Title.
                                progressDialog.setTitle("Image is Uploading...");

                            }
                        });
            } else {

                Toast.makeText(AdminHome.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

            }
        }//end try
        catch (Exception e) {
            Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                SelectImage.setImageBitmap(bitmap);

                // After selecting image change choose button above text.
                ChooseButton.setText("Image Selected");

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
}
