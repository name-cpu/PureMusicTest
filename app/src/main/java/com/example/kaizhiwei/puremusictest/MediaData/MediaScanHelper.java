package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.example.kaizhiwei.puremusictest.model.scanmusic.ExternFileSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class MediaScanHelper{
    private volatile static MediaScanHelper instance;
    private Context mContext;
    private static final int WHAT_SCAN_START = 1001;
    private static final int WHAT_SCAN_ING = 1002;
    private static final int WHAT_SCAN_COMPLETED = 1003;

    private Handler handler = new Handler();

    private MediaScanHelper(){

    }

    public synchronized static MediaScanHelper getInstance(){
        if(instance == null){
            instance = new MediaScanHelper();
        }
        return instance;
    }

    /**
     * 扫描一个媒体文件
     * @param strFilePath 要扫描的媒体文件
     */
    public void scanFile(Context context, String strFilePath) {
        mContext = context;
        List<String> listScanPath = new ArrayList<>();
        if(TextUtils.isEmpty(strFilePath) == false)
            listScanPath.add(strFilePath);
        scanFiles(context, listScanPath);
    }

    /**
     * 扫描多个媒体文件
     * @param filePaths 要扫描的文件列表
     */
    public void scanFiles(Context context, List<String> filePaths){
        mContext = context;
        ExternFileSource task = new ExternFileSource(filePaths);
        task.setMinMediaDuration(PreferenceConfig.getInstance().getScanFilterByDuration() ? 60 : 0);
        task.setFilterFolderPath(PreferenceConfig.getInstance().getScanFilterByFolderName());
    }
}


