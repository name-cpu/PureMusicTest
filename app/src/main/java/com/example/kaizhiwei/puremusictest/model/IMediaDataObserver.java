package com.example.kaizhiwei.puremusictest.model;

/**
 * Created by kaizhiwei on 17/9/17.
 */

public interface IMediaDataObserver {
    void onMediaDataChanged(int type, long  musicId);
}
