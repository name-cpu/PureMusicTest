package com.example.kaizhiwei.puremusictest;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.AsyncTask.AsyncTaskScanPath;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaDataBase;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.MediaScanHelper;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;
import com.example.kaizhiwei.puremusictest.contract.UnStandardContract;
import com.example.kaizhiwei.puremusictest.presenter.UnStandardAdPrensenter;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by kaizhiwei on 16/10/29.
 */
public class LaunchActivity extends FragmentActivity implements AsyncTaskScanPath.ScanResultListener, UnStandardContract.View {
    private Handler mHandler = new Handler();
    private UnStandardContract.Presenter mPresenter;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.launcher_root_view)
    RelativeLayout launcher_root_view;

    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
          /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.launch_activity);
        ButterKnife.bind(this);
        mPresenter = new UnStandardAdPrensenter(this);
        mPresenter.getUnStandardAd(19, 1, 1, "android", "music", "5.9.9.6", "B11BCF66936DA386C6AC9CA228F2C", "ppzs", 2);
//        if(PreferenceConfig.getInstance().getFirstLaunch()){
//            MediaScanHelper.getInstance().addScanListener(this);
//            MediaScanHelper.getInstance().scanFile(this, "");
//        }
//        else{
//            MediaLibrary.getInstance().initData();
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent launchIntent = getIntent();
//                    if(launchIntent.getAction().equals(Intent.ACTION_VIEW)){
//
//                    }
//                    Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            },1000);
//        }
//        PreferenceConfig.getInstance().setFirstLaunch(false);
    }

    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(int process, String strFilePath, boolean bAudioFile) {

    }

    @Override
    public void onScanCompleted(HashMap<String, String> mapResult, int filterNum) {
//        MediaLibrary.getInstance().resetAllMediaEntityInfo(mapResult);
//        //MediaLibrary.getInstance().initData();
//        Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
//        startActivity(intent);
//        finish();
    }

    @Override
    public void onGetUnStandardAdSuccess(UnStandardAdBean bean) {
        if(bean != null && bean.getMaterial_map() != null){
            String strPicUrl = bean.getMaterial_map().get(0).getMaterials().getAd_mob_playerskin_turnround().getDisplay_content().get(0).getPicture();
            ivLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);
            launcher_root_view.setBackgroundResource(R.color.black);
            Glide.with(this).load(strPicUrl).into(ivLogo);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    public void onError(String strErrMsg) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
