package com.death.tnt.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.death.tnt.Nexample;
import com.death.tnt.R;
import com.death.tnt.favourite.FavouritePlace;
import com.death.tnt.map.ExampleViewMap;
import com.death.tnt.photogallery.PhotoGallery;
import com.death.tnt.tabHost.ActivityTabHost;
import com.death.tnt.visited.VisitedPlace;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tabs, maps, gallery, fav_place, visited_place,maps_direction;
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
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        tabs = (TextView) findViewById(R.id.tabs);
        gallery = (TextView) findViewById(R.id.gallery);
        maps = findViewById(R.id.maps);
        fav_place = (TextView) findViewById(R.id.fav_place);
        visited_place = (TextView) findViewById(R.id.vis_place);
        maps_direction = (TextView) findViewById(R.id.maps_direction);
//        gallery = (LinearLayout) findViewById(R.id.l4);
//        about = (LinearLayout) findViewById(R.id.l5);
//        exit = (LinearLayout) findViewById(R.id.l6);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        /**
         * shows the feed first
         */
        ft.add(R.id.framelayout, new Home());
        ft.commit();
        drawerLayout.closeDrawers();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");

//        maps_direction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * show maps for direction
//                 */
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.framelayout, new DirectionMaps());
//                getSupportActionBar().setTitle("Directions");
//                ft.commit();
//                drawerLayout.closeDrawers();
//            }
//        });
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.framelayout, new ExampleViewMap());
                ft.commit();
                drawerLayout.closeDrawers();
                setSupportActionBar(toolbar);
                getSupportActionBar().setTitle("Map");

            }
        });


        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *show tabs
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new ActivityTabHost());
                getSupportActionBar().setTitle("Feed");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *show gallery
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new PhotoGallery());
                getSupportActionBar().setTitle("Gallery");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

        fav_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * show favourite places in list view
                 */

                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new FavouritePlace());
                getSupportActionBar().setTitle("Favourite Place");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

        visited_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * show Visited places in list view
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new VisitedPlace());
                getSupportActionBar().setTitle("Favourite Place");
                ft.commit();
                drawerLayout.closeDrawers();
            }
        });

    }

//    @Override
//    public void onBackPressed() {
//        new AlertDialog.Builder(this)
//                .setTitle("Really Exit?")
//                .setMessage("Are you sure you want to exit?")
//                .setNegativeButton(android.R.string.no, null)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        DashboardActivity.super.onBackPressed();
//                    }
//                }).create().show();
//    }

    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }
}