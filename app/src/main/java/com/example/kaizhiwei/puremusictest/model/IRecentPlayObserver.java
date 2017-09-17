package com.example.kaizhiwei.puremusictest.model;

/**
 * Created by kaizhiwei on 17/9/11.
 */

public interface IRecentPlayObserver {
    void onRecentPlayChanged(int type, long id);
}
