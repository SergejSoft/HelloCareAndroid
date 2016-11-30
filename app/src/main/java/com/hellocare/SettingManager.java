package com.hellocare;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.util.Base64;
import android.util.Log;


public class SettingManager {

    public static final String EMAIL = "EMAIL";

    public static final String TEMPORARY_TOKEN = "TEMPORARY_TOKEN";
    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String TOKEN_TYPE_FACEBOOK = "facebook";
    public static final String TOKEN_TYPE_HELLOCARE = "hellocare";
    private static SettingManager instance = null;

    private static final String TAG = "SettingManager";

    private static SharedPreferences mSharedPreferences;
    private Context mContext;
private  Location currentLocation;
    public static final String TOKEN = "Token";

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private String prefix = "HelloCare";

    public static SettingManager getInstance() {
        if (instance == null) {
            instance = new SettingManager();
        }
        return instance;
    }

    private SettingManager() {}

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(prefix, Context.MODE_PRIVATE);
        Log.i(TAG, "mSharedPreferences = " + mSharedPreferences);
    }

    public void saveValue(String key, boolean value) {
        Editor edit = mSharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public boolean readValue(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void saveValue(String key, int value) {
        Editor edit = mSharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public int readValue(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public long readValue(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public void saveValue(String key, String value) {
        Editor edit = mSharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public String readValue(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }


    public void saveValue(String key, long value) {
        Editor edit = mSharedPreferences.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public boolean userLoggedIn(){
        return readValue(TOKEN, "")!=null && readValue(TOKEN,"").length()!=0;
    }

    public void createTempTokenFromLoginPassword(String email, String password){
        String bytesEncoded = Base64.encodeToString((email +":"+ password).getBytes(), Base64.NO_WRAP);


        saveValue(TEMPORARY_TOKEN, bytesEncoded);

    }


    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
