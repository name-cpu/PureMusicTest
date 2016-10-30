package com.example.kaizhiwei.puremusictest.Databases;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class MediaScanHelper implements AsyncTaskScanPath.ScanResultListener{
    private volatile static MediaScanHelper instance;
    private Context mContext;

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
        AsyncTaskScanPath task = new AsyncTaskScanPath(this, filePaths);
        task.execute();
    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(int process, String strFilePath) {
        Log.i("weikaizhi", "onScaning: " + process + ", strFilePath: " + strFilePath);
    }

    @Override
    public void onScanCompleted(HashMap<String, String> mapResult) {
        Set<String> keySet = mapResult.keySet();
        List<ScanFile> listScanFile = new ArrayList<>();
        for (String s : keySet){
            String format = mapResult.get(s);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(format);
            ScanFile scanFile = new ScanFile(s, mimeType);
            listScanFile.add(scanFile);
        }

        ScanClient client = new ScanClient(mContext, listScanFile);
        client.connectAndScan();
    }
}


