package com.death.tnt.home;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.death.tnt.R;
import com.death.tnt.directionmaps.DirectionMaps;
import com.death.tnt.favourite.Weather;
import com.death.tnt.map.ExampleViewMap;
import com.death.tnt.photogallery.PhotoGallery;
import com.death.tnt.tabHost.ActivityTabHost;
import com.death.tnt.visited.VisitedPlace;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    LinearLayout home, map, gallery, weather, places, maps_direction;
    //for hamburger-icon, toggle drawers
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GPS settings
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        home = (LinearLayout) findViewById(R.id.l1);
        map = (LinearLayout) findViewById(R.id.l2);
        maps_direction = (LinearLayout) findViewById(R.id.l3);
        places = (LinearLayout) findViewById(R.id.l4);
        weather = (LinearLayout) findViewById(R.id.l5);
        gallery = (LinearLayout) findViewById(R.id.l6);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.add(R.id.framelayout, new Home());
        ft.commit();
        drawerLayout.closeDrawers();
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.icons));
        getSupportActionBar().setTitle("Home");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout, new Home());
                ft.commit();
                drawerLayout.closeDrawers();
                setSupportActionBar(toolbar);
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Home");

            }
        });
        maps_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new DirectionMaps());
                setSupportActionBar(toolbar);
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Direction");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });


        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *show tabs
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new ExampleViewMap());
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Map");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new PhotoGallery());
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Gallery");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * show weather in list view
                 */

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new Weather());
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Weather");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

        places.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * show places in list view
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new VisitedPlace());
                toolbar.setTitleTextColor(R.color.icons);
                getSupportActionBar().setTitle("Places");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }
}