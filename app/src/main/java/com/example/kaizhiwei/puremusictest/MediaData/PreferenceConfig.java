package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kaizhiwei.puremusictest.PureMusicApplication;

/**
 * Created by kaizhiwei on 16/11/26.
 */
public class PreferenceConfig {
    private static final String LAST_FIRST_LAUNCH = "LAST_FIRST_LAUNCH";
    private static final String FIRST_LAUNCH = "FIRST_LAUNCH";
    private static final String PLAY_MODE = "PLAY_MODE";
    public static final int PLAYMODE_ORDER = 0;    //顺序播放
    public static final int PLAYMODE_ONECIRCLE = 1;//单曲循环
    public static final int PLAYMODE_ALLCIRCLE = 2;//整个播放列表循环播放
    public static final int PLAYMODE_RANDOM = 3;    //随机播放


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
        setLastFirstLaunch(getFirstLaunch());
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putBoolean(FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }

    public boolean getLastFirstLaunch(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getBoolean(LAST_FIRST_LAUNCH, true);
    }

    public void setLastFirstLaunch(boolean firstLaunch){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putBoolean(LAST_FIRST_LAUNCH, firstLaunch);
        editor.commit();
    }

    public int getPlayMode(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getInt(PLAY_MODE, PLAYMODE_ORDER);
    }

    public void setPlayMode(int iMode){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putInt(PLAY_MODE, iMode);
        editor.commit();
    }
}
