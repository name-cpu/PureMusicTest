package com.example.kaizhiwei.puremusictest;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaDataBase;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.MediaScanHelper;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;

import java.util.HashMap;


/**
 * Created by kaizhiwei on 16/10/29.
 */
public class LaunchActivity extends FragmentActivity implements AsyncTaskScanPath.ScanResultListener {
    private Handler mHandler = new Handler();

    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
          /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launch_activity);

        if(PreferenceConfig.getInstance().getFirstLaunch()){
            MediaScanHelper.getInstance().addScanListener(this);
            MediaScanHelper.getInstance().scanFile(this, "");
        }
        else{
            MediaLibrary.getInstance().initData();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent launchIntent = getIntent();
                    if(launchIntent.getAction().equals(Intent.ACTION_VIEW)){

                    }
                    Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1000);
        }
        PreferenceConfig.getInstance().setFirstLaunch(false);
    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(int process, String strFilePath, boolean bAudioFile) {

    }

    @Override
    public void onScanCompleted(HashMap<String, String> mapResult, int filterNum) {
        MediaLibrary.getInstance().resetAllMediaEntityInfo(mapResult);
        //MediaLibrary.getInstance().initData();
        Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
