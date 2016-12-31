package com.example.kaizhiwei.puremusictest.MediaData;

import android.bluetooth.BluetoothAssignedNumbers;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kaizhiwei.puremusictest.PureMusicApplication;

import org.w3c.dom.DOMStringList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by kaizhiwei on 16/11/26.
 */
public class PreferenceConfig {
    private static final String LAST_FIRST_LAUNCH = "LAST_FIRST_LAUNCH";
    private static final String FIRST_LAUNCH = "FIRST_LAUNCH";
    private static final String PLAY_MODE = "PLAY_MODE";

    //扫描过滤设置
    private static final String SCAN_FILTER_BY_DURATION = "SCAN_FILTER_BY_DURATION";
    private static final String SCAN_FILTER_BY_FOLERNAME = "SCAN_FILTER_BY_FOLERNAME";

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

    public boolean getScanFilterByDuration(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getBoolean(SCAN_FILTER_BY_DURATION, false);
    }

    public void setScanFilterByDuration(boolean iMode){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putBoolean(SCAN_FILTER_BY_DURATION, iMode);
        editor.commit();
    }

    public List<String> getScanFilterByFolderName(){
        Set<String> setFilter = new HashSet<>();
        setFilter = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getStringSet(SCAN_FILTER_BY_FOLERNAME, setFilter);
        List<String> list = new ArrayList<>();
        for(Iterator it = setFilter.iterator(); it.hasNext(); )
        {
           list.add((String)it.next());
        }

        return list;
    }

    public void setScanFilterByFolderName(List<String> list){
        if(list == null)
            return;

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        Set<String> setFilter = new HashSet<>();
        for(int i = 0;i < list.size();i++) {
            setFilter.add(list.get(i));
        }
        editor.putStringSet(SCAN_FILTER_BY_FOLERNAME, setFilter);
        editor.commit();
    }
}
