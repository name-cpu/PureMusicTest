package com.example.kaizhiwei.puremusictest.ScanMedia;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;
import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.MediaData.MediaScanHelper;
import com.example.kaizhiwei.puremusictest.R;

import java.util.HashMap;

/**
 * Created by kaizhiwei on 16/10/30.
 */
public class ScanSongActivity extends FragmentActivity implements CommonTitleView.onTitleClickListener, View.OnClickListener, AsyncTaskScanPath.ScanResultListener {
    private CommonTitleView mTitleView;
    private Button btnStartScan;
    private Button btnSetScan;
    private ScanSetFragment mScanSetFragment;
    private TextView textViewScanProgress;
    private TextView textViewScaningFile;
    private TextView textViewScanResult;
    private int mSongInc;

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

        textViewScanProgress = (TextView)findViewById(R.id.textViewScanProgress);
        textViewScaningFile = (TextView)findViewById(R.id.textViewScaningFile);
        textViewScanResult = (TextView)findViewById(R.id.textViewScanResult);

        MediaScanHelper.getInstance().addScanListener(this);
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

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(int process, String strFilePath,final boolean bAudioFile) {
        textViewScanProgress.setText("扫描中:"+process +"%");
        textViewScaningFile.setText("正在扫描:" + strFilePath);
        if(bAudioFile){
            mSongInc++;
            textViewScanResult.setText("扫描到"+mSongInc+"首歌曲");
        }
    }

    @Override
    public void onScanCompleted(HashMap<String, String> mapResult, int filterNum) {

    }
}
