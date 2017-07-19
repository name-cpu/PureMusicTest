package com.example.kaizhiwei.puremusictest.download;

/**
 * Created by kaizhiwei on 17/7/18.
 */

public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}