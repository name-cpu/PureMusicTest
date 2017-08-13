package com.example.kaizhiwei.puremusictest.model.scanmusic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public abstract class BaseScanMusic {
    private List<IScanListener> mListeners = new ArrayList<>();

    public abstract void onStart();

    public abstract void onScaning(String fileName, String filePath, int persent);

    public abstract void onFinish();

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

    public void notifyScaning(){

    }

    public void notifyFinish(){

    }
}
