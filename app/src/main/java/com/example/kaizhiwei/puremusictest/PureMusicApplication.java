package com.example.kaizhiwei.puremusictest;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.MediaDataBase;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.MediaData.SongEntity;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;
import com.example.kaizhiwei.puremusictest.download.ProgressHelper;
import com.example.kaizhiwei.puremusictest.download.ProgressUIListener;
import com.example.kaizhiwei.puremusictest.model.scanmusic.MediaModel;

import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class PureMusicApplication extends Application {
    private static PureMusicApplication instance;
    private Context mContext;
    private List<SongEntity> mListSongEntrty;

    public synchronized static PureMusicApplication getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        MediaModel.getInstance().init(mContext);
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志

//        String url = "http://assets.geilicdn.com/channelapk/1000n_shurufa_1.9.6.apk";
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        Request.Builder builder = new Request.Builder();
//        builder.url(url);
//        builder.get();
//        Call call = okHttpClient.newCall(builder.build());
//
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("TAG", "=============onFailure===============");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("TAG", "=============onResponse===============");
//                Log.e("TAG", "request headers:" + response.request().headers());
//                Log.e("TAG", "response headers:" + response.headers());
//                ResponseBody responseBody = ProgressHelper.withProgress(response.body(), new ProgressUIListener() {
//
//                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
//                    @Override
//                    public void onUIProgressStart(long totalBytes) {
//                        super.onUIProgressStart(totalBytes);
//                        Log.e("TAG", "onUIProgressStart:" + totalBytes);
//                        Toast.makeText(getApplicationContext(), "开始下载：" + totalBytes, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
//                        Log.e("TAG", "=============start===============");
//                        Log.e("TAG", "numBytes:" + numBytes);
//                        Log.e("TAG", "totalBytes:" + totalBytes);
//                        Log.e("TAG", "percent:" + percent);
//                        Log.e("TAG", "speed:" + speed);
//                        Log.e("TAG", "============= end ===============");
//                        //downloadProgeress.setProgress((int) (100 * percent));
//                        //downloadInfo.setText("numBytes:" + numBytes + " bytes" + "\ntotalBytes:" + totalBytes + " bytes" + "\npercent:" + percent * 100 + " %" + "\nspeed:" + speed * 1000 / 1024 / 1024 + " MB/秒");
//                    }
//
//                    //if you don't need this method, don't override this methd. It isn't an abstract method, just an empty method.
//                    @Override
//                    public void onUIProgressFinish() {
//                        super.onUIProgressFinish();
//                        Log.e("TAG", "onUIProgressFinish:");
//                        Toast.makeText(getApplicationContext(), "结束下载", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                BufferedSource source = responseBody.source();
//
//                File outFile = new File("sdcard/temp.apk");
//                outFile.delete();
//                outFile.getParentFile().mkdirs();
//                outFile.createNewFile();
//
//                BufferedSink sink = Okio.buffer(Okio.sink(outFile));
//                source.readAll(sink);
//                sink.flush();
//                source.close();
//            }
//        });

    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    public void addSongEntrty(SongEntity entrty){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }
        mListSongEntrty.add(entrty);
    }

    public List<SongEntity> getListSongEntrty(){
        return mListSongEntrty;
    }

    public void addSongEntrty(List<SongEntity> list){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }
        mListSongEntrty.clear();
        mListSongEntrty.addAll(list);
    }

    public void removeSongEntrty(SongEntity entrty){
        if(mListSongEntrty == null){
            mListSongEntrty = new ArrayList<>();
        }

        int iSize = mListSongEntrty.size();
        for(int i = 0; i< iSize;i++){
            SongEntity temp = mListSongEntrty.get(i);
            if(temp == entrty){
                mListSongEntrty.remove(i);
                break;
            }
        }
    }
}