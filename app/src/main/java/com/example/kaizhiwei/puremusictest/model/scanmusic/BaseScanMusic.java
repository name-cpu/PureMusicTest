package com.example.kaizhiwei.puremusictest.model.scanmusic;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public abstract class BaseScanMusic {
    private List<IScanListener> mListeners = new ArrayList<>();

    public abstract void scan(BaseHandler handler);

    public void addListener(IScanListener listener){
        if(mListeners.contains(listener) == false){
            mListeners.add(listener);
        }
    }

    public void removeListener(IScanListener listener){
        if(mListeners.contains(listener)){
            mListeners.remove(listener);
        }
    }

    public void notifyStart(){

    }

    public void notifyScaning(String fileName, String filePath, int process){

    }

    public void notifyFinish(){

    }
}
