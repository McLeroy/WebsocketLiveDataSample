package com.mcleroy.wesocketslivedatasample.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mcleroy.wesocketslivedatasample.R;
import com.mcleroy.wesocketslivedatasample.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity implements SplashFragment.SplashFragmentCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void countDownComplete() {
        startActivity(MainActivity.getStartIntent(this));
        finish();
    }
}
