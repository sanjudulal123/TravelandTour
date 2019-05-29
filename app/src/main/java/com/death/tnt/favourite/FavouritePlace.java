package com.death.tnt.favourite;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.death.tnt.R;

public class FavouritePlace extends Fragment {

    CheckBox one, two, three, four, five, six, seven, eight, nine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_map_favourite, null);
        one = (CheckBox)view.findViewById(R.id.one);
        two = (CheckBox)view.findViewById(R.id.two);
        three = (CheckBox)view.findViewById(R.id.three);
        four = (CheckBox)view.findViewById(R.id.four);
        five = (CheckBox)view.findViewById(R.id.five);
        six = (CheckBox)view.findViewById(R.id.six);
        seven = (CheckBox)view.findViewById(R.id.seven);
        eight = (CheckBox)view.findViewById(R.id.eight);
        nine = (CheckBox)view.findViewById(R.id.nine);

        return view;
    }
}
