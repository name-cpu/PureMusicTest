package com.example.kaizhiwei.puremusictest.ScanSong;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;
import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.Databases.MediaScanHelper;
import com.example.kaizhiwei.puremusictest.Databases.ScanFile;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeFragment1;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeFragment2;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeFragment3;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeFragment4;
import com.example.kaizhiwei.puremusictest.Welcome.WelcomeFragment5;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class ScanSongActivity extends FragmentActivity implements CommonTitleView.onTitleClickListener, View.OnClickListener {
    private CommonTitleView mTitleView;
    private Button btnStartScan;
    private Button btnSetScan;
    private ScanSetFragment mScanSetFragment;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_welcome_screen2);

        mTitleView = (CommonTitleView)findViewById(R.id.titleView);
        mTitleView.setOnClickListener(this);
        mTitleView.setRightBtnVisible(false);
        mTitleView.setTitleViewInfo("返回", "扫描歌曲","");

        btnStartScan = (Button)findViewById(R.id.btnStartScan);
        btnSetScan = (Button)findViewById(R.id.btnSetScan);
        btnStartScan.setOnClickListener(this);
        btnSetScan.setOnClickListener(this);
    }

    @Override
    public void onLeftBtnClicked() {

    }

    @Override
    public void onRightBtnClicked() {

    }

    @Override
    public void onClick(View v) {
        if(btnSetScan == v){
            FragmentManager fm = this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if(mScanSetFragment == null){
                mScanSetFragment = new ScanSetFragment();
                ft.add(R.id.mainContent, mScanSetFragment);
            }
            else{
                ft.show(mScanSetFragment);
            }
            ft.commit();

        }
        else if(btnStartScan == v){
            MediaScanHelper.getInstance().scanFile(this, "");
        }
    }
}
