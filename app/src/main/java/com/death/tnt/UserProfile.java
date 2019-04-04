package com.death.tnt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.InputStream;

/**
Displaying User's Facebook Profile using AsyncTask
To Display the User's name, surname, and image
Url-
 */
public class UserProfile extends AppCompatActivity {
    private ShareDialog shareDialog;
    private Button logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shareDialog = new ShareDialog(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent content = new ShareLinkContent.Builder().build();
                shareDialog.show(content);
            }
        });
        Bundle inBundle = getIntent().getExtras();
        if (inBundle != null) {
            String name = inBundle.get("name").toString();
//        String surname = inBundle.get("surname").toString();
            String imageUrl = inBundle.get("imageUrl").toString();
            TextView nameview = (TextView) findViewById(R.id.nameAndSurname);
            nameview.setText("" + name + " ");
            new UserProfile.DownloadImage((ImageView) findViewById(R.id.profileImage)).execute(imageUrl);
        } else {
            Toast.makeText(this, "Wrong", Toast.LENGTH_SHORT).show();
        }


        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent login = new Intent(UserProfile.this, FacebookLogin.class);
                startActivity(login);
                finish();
            }
        });



    }

    public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
