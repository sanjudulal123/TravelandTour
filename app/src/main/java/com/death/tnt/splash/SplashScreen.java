package com.death.tnt.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.death.tnt.Nexample;
import com.death.tnt.R;

public class SplashScreen extends Activity {
    protected boolean _active = true;
    protected int _splashTime = 3000; // time to display the splash screen in ms



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    startActivity(new Intent(SplashScreen.this,
                            Nexample.class));
                    finish();
                }
            }
        };
        splashTread.start();
    }
}
