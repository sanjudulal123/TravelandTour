package com.death.tnt.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.death.tnt.R;

public class HeaderActivity extends AppCompatActivity {
    ImageView headerprofile;
    TextView nameprofile, emailprofile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.header);
    }
}
