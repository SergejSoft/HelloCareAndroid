package com.hellocare;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class HelloCareApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SettingManager.getInstance().setContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

}
