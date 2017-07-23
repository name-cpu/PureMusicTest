package com.example.kaizhiwei.puremusictest.NetAudio.video;

import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.kaizhiwei.puremusictest.MediaData.VLCInstance;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.SongMvInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.MvInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.MvInfoPresenter;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import butterknife.Bind;


/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PlayMvActivity extends MyBaseActivity implements MvInfoContract.View {
    private MvInfoPresenter mPresenter;

    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    @Bind(R.id.surfaceView)
    SurfaceView surfaceView;
    @Bind(R.id.llControlBar)
    LinearLayout llControlBar;

    private MediaPlayer mMediaPlayer;
    private String mMVId;
    public static final String INTENT_MVID = "INTENT_MVID";

    @Override
    public int getLayoutId() {
        return R.layout.activity_play_mv;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MvInfoPresenter(this);
    }

    @Override
    public void initView() {
        systemBarTintManager.setStatusBarTintResource(R.color.black);
        surfaceView.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        mMediaPlayer = new MediaPlayer(VLCInstance.getInstance());
        mMVId = getIntent().getStringExtra(INTENT_MVID);
        mPresenter.getMvInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 0, "11,12",
                "baidu.ting.mv.playMV", PureMusicContant.FORMAT_JSON, mMVId, "", "");
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetMvInfoSuccess(SongMvInfoBean bean) {
        Log.e("weikaizhi", "bean " + bean.getResult().getFiles().get_$41().getFile_link());
        Media media = new Media(VLCInstance.getInstance(), Uri.parse(bean.getResult().getFiles().get_$41().getFile_link()));
        mMediaPlayer.setMedia(media);
        media.release();
        mMediaPlayer.getVLCVout().detachViews();
        //mMediaPlayer.getVLCVout().attachViews();
        mMediaPlayer.getVLCVout().setVideoView(surfaceView);
        mMediaPlayer.getVLCVout().attachViews();
        mMediaPlayer.getVLCVout().addCallback(new IVLCVout.Callback() {
            @Override
            public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
                Log.e("weikaizhi", "onNewLayout");
            }

            @Override
            public void onSurfacesCreated(IVLCVout vlcVout) {
                Log.e("weikaizhi", "onSurfacesCreated");
            }

            @Override
            public void onSurfacesDestroyed(IVLCVout vlcVout) {
                Log.e("weikaizhi", "onSurfacesDestroyed");
            }
        });

        surfaceView.setVisibility(View.VISIBLE);
        mMediaPlayer.setVideoTitleDisplay(MediaPlayer.Position.Disable, 0);
        mMediaPlayer.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.getVLCVout().detachViews();
        mMediaPlayer.stop();
    }
}
