package com.example.kaizhiwei.puremusictest.download;

/**
 * Created by kaizhiwei on 17/7/18.
 */

public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}