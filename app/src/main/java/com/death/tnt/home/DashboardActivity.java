package com.death.tnt.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.death.tnt.R;
import com.death.tnt.map.ViewMap;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    LinearLayout view_map, favourite_place, visited_place, gallery, about, exit;
    //for hamburger-icon, toggle drawers
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerid);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        view_map = (LinearLayout)findViewById(R.id.l1);
        favourite_place = (LinearLayout)findViewById(R.id.l2);
        visited_place = (LinearLayout)findViewById(R.id.l3);
        gallery = (LinearLayout)findViewById(R.id.l4);
        about = (LinearLayout)findViewById(R.id.l5);
        exit = (LinearLayout)findViewById(R.id.l6);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(DashboardActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        /**
         * first page will show the first page after login
         * where we will get the current location and show the users
         * the places nearby
         */
        ft.add(R.id.framelayout,new FirstPage());
        getSupportActionBar().setTitle(R.string.Travel);
        ft.commit();

        view_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * code to add map and show the current location
                 * satellite view
                 * terrain view
                 * add marker in map and
                 * calculate distance between two marker points
                 */
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.framelayout,new ViewMap());
                getSupportActionBar().setTitle(R.string.Map);
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
