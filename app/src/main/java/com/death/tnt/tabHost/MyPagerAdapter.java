package com.death.tnt.tabHost;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.death.tnt.map.ViewMap;
import com.death.tnt.slidingTab.Feed;
import com.death.tnt.slidingTab.Places;
import com.death.tnt.slidingTab.UserLocation;
import com.death.tnt.slidingTab.UserProfile;

class MyPagerAdapter extends FragmentStatePagerAdapter {
    public MyPagerAdapter(FragmentManager childFragmentManager, FragmentActivity activity) {
        super(childFragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserLocation();
            case 1:
                return new Places();
            case 2:
                return new Feed();
            case 3:
                return new ViewMap();
            case 4:
                return new UserProfile();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Location";
            case 1:
                return "Places";
            case 2:
                return "Feed";
            case 3:
                return "Map";
            case 4:
                return "Profile";
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}