package com.death.tnt.tabHost;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.death.tnt.R;


public class ActivityTabHost extends Fragment {
    PagerSlidingTabStrip tabstrip;
    ViewPager vp;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.slidingtab,null);
        tabstrip = (PagerSlidingTabStrip)v.findViewById(R.id.pager_tabs);
        vp = (ViewPager)v.findViewById(R.id.viewpager);
        vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(),getActivity()));
        tabstrip.setViewPager(vp);
        return  v;
    }
}