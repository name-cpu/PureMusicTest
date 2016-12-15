package com.example.kaizhiwei.puremusictest.MediaData;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
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
    private List<AsyncTaskScanPath.ScanResultListener> mListListener;
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

    public void addScanListener(AsyncTaskScanPath.ScanResultListener listener){
        if(mListListener == null){
            mListListener = new ArrayList<AsyncTaskScanPath.ScanResultListener>();
        }
        mListListener.add(listener);
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
        if(mListListener == null || mListListener.size() == 0)
            return ;

        handler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < mListListener.size();i++){
                    AsyncTaskScanPath.ScanResultListener listener = mListListener.get(i);
                    if(listener == null)
                        continue;

                    listener.onScanStart();
                }
            }
        });
    }

    @Override
    public void onScaning(final int process, final String strFilePath,final boolean bAudioFile) {
        if(mListListener == null || mListListener.size() == 0)
            return ;

        handler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < mListListener.size();i++){
                    AsyncTaskScanPath.ScanResultListener listener = mListListener.get(i);
                    if(listener == null)
                        continue;

                    listener.onScaning(process, strFilePath, bAudioFile);
                }
            }
        });
    }

    @Override
    public void onScanCompleted(final HashMap<String, String> mapResult) {
        if(mListListener == null || mListListener.size() == 0)
            return ;

        Set<String> keySet = mapResult.keySet();
        List<ScanFile> listScanFile = new ArrayList<>();
        for (String s : keySet){
            String format = mapResult.get(s);
            String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(format);
            ScanFile scanFile = new ScanFile(s, mimeType);
            listScanFile.add(scanFile);
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < mListListener.size();i++){
                    AsyncTaskScanPath.ScanResultListener listener = mListListener.get(i);
                    if(listener == null)
                        continue;

                    listener.onScanCompleted(mapResult);
                }
            }
        });

        ScanClient client = new ScanClient(mContext, listScanFile);
        client.connectAndScan();
    }
}


