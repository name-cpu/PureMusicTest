package com.example.kaizhiwei.puremusictest.ui.splash;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.bean.UnStandardAdBean;
import com.example.kaizhiwei.puremusictest.contract.UnStandardContract;
import com.example.kaizhiwei.puremusictest.presenter.UnStandardAdPrensenter;

import butterknife.Bind;


/**
 * Created by kaizhiwei on 16/10/29.
 */
public class SplashActivity extends MyBaseActivity implements UnStandardContract.View {
    private Handler mHandler = new Handler();
    private UnStandardContract.Presenter mPresenter;
    @Bind(R.id.ivLogo)
    ImageView ivLogo;
    @Bind(R.id.launcher_root_view)
    RelativeLayout launcher_root_view;

    @Override
    public int getLayoutId() {
        return R.layout.launch_activity;
    }

    @Override
    public void initPresenter() {
        mPresenter = new UnStandardAdPrensenter(this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mPresenter.getUnStandardAd(19, 1, 1, "android", "music", "5.9.9.6", "B11BCF66936DA386C6AC9CA228F2C", "ppzs", 2);
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
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    @Override
    public void onError(String strErrMsg) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
