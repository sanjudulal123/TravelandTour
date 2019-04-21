package com.death.tnt.tabHost;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.death.tnt.map.ExampleViewMap;
import com.death.tnt.map.ViewMap;
import com.death.tnt.slidingTab.Feed;
import com.death.tnt.slidingTab.Places;
import com.death.tnt.slidingTab.UserLocation;
import com.death.tnt.slidingTab.UserProfile;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager childFragmentManager, FragmentActivity activity) {
        super(childFragmentManager);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserLocation();
            case 1:
                /**
                 * original
                 * return new ViewMap()
                 */
                return new ExampleViewMap();
            case 2:
                return new UserProfile();
            default:
                return null;
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Home";
            case 1:
                return "About";
            case 2:
                return "Contact";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}