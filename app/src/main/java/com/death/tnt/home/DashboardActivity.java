package com.death.tnt.home;

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
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.death.tnt.R;
import com.death.tnt.map.ExampleViewMap;
import com.death.tnt.map.ViewMap;
import com.death.tnt.slidingTab.Feed;
import com.death.tnt.tabHost.ActivityTabHost;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView tabs,maps;
    //             favourite_place, visited_place, gallery, about, exit;
    //for hamburger-icon, toggle drawers
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
//        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
//        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        // Check if enabled and if not send user to the GPS settings
//        if (!enabled) {
//            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(intent);
//        }


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        tabs = (TextView) findViewById(R.id.tabs);
        maps = findViewById(R.id.maps);
//        favourite_place = (LinearLayout) findViewById(R.id.l2);
//        visited_place = (LinearLayout) findViewById(R.id.l3);
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
        ft.add(R.id.framelayout, new ExampleViewMap());
        ft.commit();
        drawerLayout.closeDrawers();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Feed");


        tabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 *
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout, new ActivityTabHost());
                getSupportActionBar().setTitle("Feed");
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