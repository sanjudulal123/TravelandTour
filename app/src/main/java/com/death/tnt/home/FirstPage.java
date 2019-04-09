package com.death.tnt.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.death.tnt.FirstPageCustomLayout;
import com.death.tnt.R;

public class FirstPage extends Fragment {
    GridView gridView;
    /**
     * for testing we are using random number and text
     * Get text and images URL from Database as Array
     * and assign them
     */
    int[] imagesforgridview = {0,1,2,3,4,5,6,7,8,9};
    String[] textforgridview = {"Hello","for","testing","we","are","using","random","number","and","text"};
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_page_new, null);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        gridView = (GridView) view.findViewById(R.id.simplegridview);

        FirstPageCustomLayout firstPageCustomLayout = new FirstPageCustomLayout(getContext());
        viewPager.setAdapter(firstPageCustomLayout);
        gridView.setAdapter(new GridViewAdapter(getActivity(),
                R.layout.gridview,imagesforgridview,textforgridview));

        return view;

    }
}
