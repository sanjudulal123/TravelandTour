package com.death.tnt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class FacebookLogin extends AppCompatActivity {
    TextView fn,ln,fun;
    ImageView imageView;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fn=(TextView) findViewById(R.id.first_name);
        ln=(TextView) findViewById(R.id.last_name);
        fun=(TextView) findViewById(R.id.full_name);
        imageView=(ImageView)findViewById(R.id.img);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {

            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                nextActivity(currentProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);
        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(FacebookLogin.this, "Logging In...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(FacebookLogin.this, "Login cancled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(FacebookLogin.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        };
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_birthday"));
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }
    @Override
    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent){
        super.onActivityResult(requestCode,responseCode,intent);
        callbackManager.onActivityResult(requestCode, responseCode, intent);
    }
    private void nextActivity(Profile profile) {
        if (profile != null) {
            Intent main = new Intent(FacebookLogin.this, UserProfile.class);
            main.putExtra("name", profile.getName());
//            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200, 200).toString());
        }
    }
}
