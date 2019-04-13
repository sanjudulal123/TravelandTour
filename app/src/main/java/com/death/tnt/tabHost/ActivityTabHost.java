package com.death.tnt.tabHost;

import android.app.TabActivity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.Toolbar;

import com.death.tnt.MainActivity;
import com.death.tnt.R;
import com.death.tnt.map.ViewMap;
import com.death.tnt.signup.Signup;

public class ActivityTabHost extends TabActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setActionBar(toolbar);
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.addTab(tabHost.newTabSpec("First")
                .setIndicator("first",getResources().getDrawable(R.drawable.ic_launcher_background))
                .setContent(new Intent(this, FirstTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Second")
                .setIndicator("second",getResources().getDrawable(R.drawable.ic_launcher_background))
                .setContent(new Intent(this, SecondTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Third")
                .setIndicator("third",getResources().getDrawable(R.drawable.ic_launcher_background))
                .setContent(new Intent(this, ThirdTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Fourth")
                .setIndicator("fourth",getResources().getDrawable(R.drawable.ic_launcher_background))
                .setContent(new Intent(this, FourthTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Profile")
                .setIndicator("Profile",getResources().getDrawable(R.drawable.ic_launcher_background))
                .setContent(new Intent(this, FifthTabLayout.class)));
        tabHost.setCurrentTab(2);
        tabHost.clearAllTabs();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                // display the name of the tab whenever a tab is changed
                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
