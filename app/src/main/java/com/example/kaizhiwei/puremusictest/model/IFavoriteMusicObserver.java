package com.example.kaizhiwei.puremusictest.model;

/**
 * Created by kaizhiwei on 17/9/17.
 */

public interface IFavoriteMusicObserver {
    void onFavoriteMusicChanged(int type, long id);
}
