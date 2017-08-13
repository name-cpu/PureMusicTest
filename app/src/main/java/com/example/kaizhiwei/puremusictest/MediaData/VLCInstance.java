package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.kaizhiwei.puremusictest.application.PureMusicApplication;

import org.videolan.libvlc.LibVLC;

import java.util.ArrayList;

/**
 * Created by kaizhiwei on 16/11/6.
 */
public class VLCInstance {
    private static LibVLC mLibVlc;

    public static ArrayList<String> getLibOptions(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        ArrayList<String> listOption = new ArrayList<String>();
        //boolean timeStreching = sp.getBoolean("enable_time_stre")
        return listOption;
    }

    public synchronized static LibVLC getInstance(){
        if(mLibVlc == null){
            mLibVlc = new LibVLC(getLibOptions(PureMusicApplication.getInstance()));
            LibVLC.setOnNativeCrashListener(new LibVLC.OnNativeCrashListener() {
                @Override
                public void onNativeCrash() {

                }
            });
        }

        return mLibVlc;
    }

    public synchronized static void restart(){
        if(mLibVlc != null){
            mLibVlc.release();
        }
        mLibVlc = new LibVLC(getLibOptions(PureMusicApplication.getInstance()));
    }



}
