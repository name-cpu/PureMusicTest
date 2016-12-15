package com.example.kaizhiwei.puremusictest.Util;

import android.os.Handler;

import java.lang.ref.WeakReference;

/**
 * Created by kaizhiwei on 16/12/10.
 */
public abstract class WeakHandler<T> extends Handler {
    private WeakReference<T> weakReference;

    public WeakHandler(T cls){
        weakReference = new WeakReference<T>(cls);
    }

    public T getOwner(){
        return weakReference.get();
    }
}
