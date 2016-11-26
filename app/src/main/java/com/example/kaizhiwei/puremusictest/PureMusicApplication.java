package com.example.kaizhiwei.puremusictest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.MediaData.SongEntity;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class PureMusicApplication extends Application {
    private static PureMusicApplication instance;
    private Context mContext;
    private List<SongEntity> mListSongEntrty;

    public synchronized static PureMusicApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PreferenceConfig.getInstance().setFirstLaunch(false);
    }

    public void addSongEntrty(SongEntity entrty){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }
        mListSongEntrty.add(entrty);
    }

    public List<SongEntity> getListSongEntrty(){
        return mListSongEntrty;
    }

    public void addSongEntrty(List<SongEntity> list){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }
        mListSongEntrty.clear();
        mListSongEntrty.addAll(list);
    }

    public void removeSongEntrty(SongEntity entrty){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }

        int iSize = mListSongEntrty.size();
        for(int i = 0; i< iSize;i++){
            SongEntity temp = mListSongEntrty.get(i);
            if(temp == entrty){
                mListSongEntrty.remove(i);
                break;
            }
        }
    }
}