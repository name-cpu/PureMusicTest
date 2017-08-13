package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kaizhiwei.puremusictest.application.PureMusicApplication;

import java.util.ArrayList;
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

    //扫描过滤设置
    private static final String SCAN_FILTER_BY_DURATION = "SCAN_FILTER_BY_DURATION";
    private static final String SCAN_FILTER_BY_FOLERNAME = "SCAN_FILTER_BY_FOLERNAME";

    //播放模式
    private static final String PLAY_MODE = "PLAY_MODE";
    public static final int PLAYMODE_ORDER = 0;    //顺序播放
    public static final int PLAYMODE_ONECIRCLE = 1;//单曲循环
    public static final int PLAYMODE_ALLCIRCLE = 2;//整个播放列表循环播放
    public static final int PLAYMODE_RANDOM = 3;    //随机播放
    public static final int PLAYMODE_NUM = 4;

    //播放列表
    private static final String PLAY_LIST = "PLAY_LIST";
    private static final String POSITION_IN_PLAY_LIST = "POSITION_IN_PLAY_LIST";
    private static final String POSITION_IN_MEDIA = "POSITION_IN_MEDIA";

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

    public List<String> getPlaylist(){
        String strPlaylist = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getString(PLAY_LIST, "");
        List<String> list = new ArrayList<>();
        String[] strArray = strPlaylist.split("\\|");
        for(String str : strArray){
            list.add(str);
        }
        if(list.size() >= 1){
            list.remove(list.size() - 1);
        }
        return list;
    }

    public void setPlaylist(List<String> list){
        if(list == null)
            return;

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i < list.size();i++) {
            builder.append(list.get(i));
            builder.append("|");
        }
        editor.putString(PLAY_LIST, builder.toString());
        editor.commit();
    }

    public int getPositionInPlaylist(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getInt(POSITION_IN_PLAY_LIST, 0);
    }

    public void setPositionInPlaylist(int position){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putInt(POSITION_IN_PLAY_LIST, position);
        editor.commit();
    }

    public int getPositionInMedia(){
        return PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).getInt(POSITION_IN_MEDIA, 0);
    }

    public void setPositionInMedia(int position){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(PureMusicApplication.getInstance()).edit();
        editor.putInt(POSITION_IN_MEDIA, position);
        editor.commit();
    }
}
