package com.hellocare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hellocare.MainActivity;
import com.hellocare.R;
import com.hellocare.SettingManager;


public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_splash);
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!SettingManager.getInstance().userLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Intent goToMainActivity = new Intent(SplashActivity.this, MainActivity.class);
                    goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(goToMainActivity);
                    finish();
                }
            }
        }, secondsDelayed * 500);

    }
}
