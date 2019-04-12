package com.death.tnt.tabHost;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
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
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.addTab(tabHost.newTabSpec("First")
                .setIndicator("first")
                .setContent(new Intent(this, FirstTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Second")
                .setIndicator("second")
                .setContent(new Intent(this, SecondTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Third")
                .setIndicator("third")
                .setContent(new Intent(this, ThirdTabLayout.class)));
        tabHost.addTab(tabHost.newTabSpec("Fourth")
                .setIndicator("fourth")
                .setContent(new Intent(this, FourthTabLayout.class)));
        tabHost.setCurrentTab(0);
    }
}
