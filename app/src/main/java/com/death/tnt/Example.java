package com.death.tnt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ChasingDots;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class is safe to delete
 */
public class Example extends AppCompatActivity {
    // Declaring Variables.
    //for facebook login
    CallbackManager callbackManager;
    ImageView imageView;
    TextView FacebookDataTextView;
    LoginButton loginButton;
    AccessTokenTracker accessTokenTracker;
    //for database
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    ArrayList<DataModule> a1 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Passing MainActivity in Facebook SDK.
        FacebookSdk.sdkInitialize(Example.this);

        // Adding Callback Manager.
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.example);

        // Assign TextView ID.
        FacebookDataTextView = (TextView) findViewById(R.id.TextView1);

        // Assign Facebook Login button ID.
        loginButton = (LoginButton) findViewById(R.id.login_button);
        imageView = (ImageView) findViewById(R.id.img);

        firebaseAuth = FirebaseAuth.getInstance();

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite chasingDots = new ChasingDots();
        progressBar.setIndeterminateDrawable(chasingDots);


        // Giving permission to Login Button.
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday", "user_friends"));

        // Checking the Access Token.
        if (AccessToken.getCurrentAccessToken() != null) {
            progressBar.setVisibility(View.VISIBLE);
            GraphLoginRequest(AccessToken.getCurrentAccessToken());

            // If already login in then show the Toast.
//            Toast.makeText(Example.this,"Already logged in",Toast.LENGTH_SHORT).show();

        } else {

            // If not login in then show the Toast.
//            Toast.makeText(Example.this,"User not logged in",Toast.LENGTH_SHORT).show();
        }
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        // Adding Click listener to Facebook login button.
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressBar.setVisibility(View.INVISIBLE);
                // Calling method to access User Data After successfully login.
                GraphLoginRequest(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Example.this, "Login Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(Example.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }

        });

        // Detect user is login or not. If logout then clear the TextView and delete all the user info from TextView.
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken2) {
                if (accessToken2 == null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    // Clear the TextView after logout.
                    FacebookDataTextView.setText("");
                    imageView.setVisibility(View.INVISIBLE);

                }
            }
        };
    }

    // Method to access Facebook User Data.
    protected void GraphLoginRequest(AccessToken accessToken) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {

                            // Adding all user info one by one into TextView.
                            String profilePicUrl = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                            Picasso.get().load(profilePicUrl).into(imageView);


                            FacebookDataTextView.setText("ID: " + jsonObject.getString("id"));
                            final String id = jsonObject.getString("id");

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nName : " + jsonObject.getString("name"));
                            String name = jsonObject.getString("name");

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nFirst name : " + jsonObject.getString("first_name"));
                            String firstName = jsonObject.getString("first_name");

                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nLast name : " + jsonObject.getString("last_name"));
                            String lastName = jsonObject.getString("last_name");

//                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nEmail : " + jsonObject.getString("email"));

//                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nGender : " + jsonObject.getString("gender"));

//                            FacebookDataTextView.setText(FacebookDataTextView.getText() + "\nBirthday : " + jsonObject.getString("birthday"));

                            databaseReference = FirebaseDatabase.getInstance().getReference().child("user").child(jsonObject.getString("id"));

                            final DataModule m = new DataModule();
                            m.setName(name);
                            m.setFirstname(firstName);
                            m.setLastname(lastName);
                            m.setUserid(id);
                            m.setProfilepictureurl(profilePicUrl);




//                            databaseReference.addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                    for (DataSnapshot d: dataSnapshot.getChildren()){
//                                        DataModule d1 = d.getValue(DataModule.class);
//                                        a1.add(d1);
//                                    }
//                                    for (int i = 0; i < a1.size(); i++) {
//                                        if (a1.get(i).getUserid().equals(FirebaseDatabase.getInstance()
//                                        .getReference().child("user").child(id))) {
//                                            Log.e("called", "auth");
//                                            Toast.makeText(Example.this, "Data Already Present", Toast.LENGTH_SHORT).show();
////                                            Intent in = new Intent(Example.this, DashboardActivity.class);
////                                            startActivity(in);
//                                            break;
//                                        } else {
//
//                                            databaseReference.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(Example.this, "push Success", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(Example.this, "" + e, Toast.LENGTH_LONG).show();
//                                                }
//                                            });
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                    Toast.makeText(Example.this, ""+databaseError, Toast.LENGTH_SHORT).show();
//                                }
//                            });









                            databaseReference.push().setValue(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Example.this, "push Success", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Example.this, "" + e, Toast.LENGTH_LONG).show();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Example.this, ""+e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        Bundle bundle = new Bundle();
        bundle.putString(
                "fields",
                "id,name,picture.type(large),link,email,gender,last_name,first_name,locale,timezone,updated_time,verified"
        );
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(Example.this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(Example.this);

    }


}
