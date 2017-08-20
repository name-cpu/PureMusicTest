package com.example.kaizhiwei.puremusictest.service;

/**
 * Created by kaizhiwei on 17/8/18.
 */

public interface IPlayMusicListener {
    void onStateChange(int state);

    void onPlayPosUpdate(int percent, int curPos, int duration);

    void onBufferingUpdate(int cur, int total);
}
