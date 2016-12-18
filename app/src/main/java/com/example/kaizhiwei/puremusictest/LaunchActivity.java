package com.example.kaizhiwei.puremusictest;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;

/**
 * Created by kaizhiwei on 16/10/29.
 */
public class LaunchActivity extends FragmentActivity {
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
            MediaLibrary.getInstance().startScan();
        }
        PreferenceConfig.getInstance().setFirstLaunch(false);

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
        },3000);

    }
}
