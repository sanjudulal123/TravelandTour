package com.death.tnt;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    LoginButton loginButton;
    TextView fn,ln,fun;
    ImageView imageView;
    private static final String EMAIL = "email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *Generating Key Hash for Facebook login
         */

//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.death.tnt",
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
        /**
         * Key Hash End
         */


        fn=(TextView) findViewById(R.id.first_name);
        ln=(TextView) findViewById(R.id.last_name);
        fun=(TextView) findViewById(R.id.full_name);
        imageView=(ImageView)findViewById(R.id.img);

        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList(EMAIL, "public_profile", "user_birthday"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                handleFacebookToken(loginResult.getAccessToken());
                AccessToken accessToken =loginResult.getAccessToken();
                GraphRequest graphRequest=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Display_data(object);
                    }
                });
                Bundle bundle=new Bundle();
                bundle.putString("fields","email,id");
                graphRequest.setParameters(bundle);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    GraphRequest request1;

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d("tokenfb",accessToken.getToken());
        Profile profile=Profile.getCurrentProfile();
        if (profile!=null)
        {
            String facebook_id=profile.getId();
            String f_name=profile.getFirstName();
            String m_name=profile.getMiddleName();
            String l_name=profile.getLastName();
            String full_name=profile.getName();

            String profile_image=profile.getProfilePictureUri(250,250).toString();

            fn.setText(f_name);
            ln.setText(l_name);
            fun.setText(full_name);
            Picasso.get().load(profile_image).into(imageView);

            Bundle bundle=new Bundle();
            request1 = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        String email_id=object.getString("email");
                        String gender=object.getString("gender");
                        String profile_name=object.getString("name");
                        long fb_id=object.getLong("id");

                        Log.d("email_id","eamil_id");
                        Log.d("gender","gender");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            request1.executeAsync();
        }
    }

        private void Display_data(JSONObject object){
        TextView textView= (TextView) findViewById(R.id.full_name);
        textView.setText(object.toString());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
