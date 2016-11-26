package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kaizhiwei.puremusictest.PureMusicApplication;

/**
 * Created by kaizhiwei on 16/11/26.
 */
public class PreferenceConfig {
    public static final String FIRST_LAUNCH = "FIRST_LAUNCH";

    private static PreferenceConfig mInstance;

    public synchronized static PreferenceConfig getInstance(){
        if(mInstance == null){
            mInstance = new PreferenceConfig();
        }

        return mInstance;
    }

    public boolean getFirstLaunch(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getBoolean(FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean firstLaunch){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putBoolean(FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }
}
