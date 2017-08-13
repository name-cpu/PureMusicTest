package com.example.kaizhiwei.puremusictest.model.scanmusic;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public interface IScanListener {
    void onScanStart();
    void onProcess(String fileName, String filePath, int process);
    void onScanFinish();
}
