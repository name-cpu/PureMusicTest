package com.example.kaizhiwei.puremusictest.NetAudio.video;

import android.net.Uri;

import com.example.kaizhiwei.puremusictest.MediaData.VLCInstance;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.MvCategoryBean;
import com.example.kaizhiwei.puremusictest.bean.MvSearchBean;
import com.example.kaizhiwei.puremusictest.bean.PlayMvBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.MvInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.MvInfoPresenter;
import com.example.purevideoplayer.PureVideoPlayer;
import org.videolan.libvlc.MediaPlayer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;


/**
 * Created by kaizhiwei on 17/7/23.
 */

public class PlayMvActivity extends MyBaseActivity implements MvInfoContract.View {
    private MvInfoPresenter mPresenter;

    @Bind(R.id.pureVideoPlayer)
    PureVideoPlayer pureVideoPlayer;

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
    }

    @Override
    public void initData() {
        mMediaPlayer = new MediaPlayer(VLCInstance.getInstance());
        mMVId = getIntent().getStringExtra(INTENT_MVID);
        mPresenter.getMvInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 0, "11,12",
                "baidu.ting.mv.playMV", PureMusicContant.FORMAT_JSON, mMVId, "", "");
    }

    @Override
    protected void doBeforeSetcontentView(){

    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetMvInfoSuccess(PlayMvBean bean) {
        if(bean == null || bean.getResult() == null)
            return;

        PlayMvBean.ResultBean.FilesBean filesBean = bean.getResult().getFiles();
        List<PlayMvBean.ResultBean.FilesBean.FileItemBean> list = filesBean.getListFileItems();
        if(list.size() > 0){
            Collections.sort(list, new Comparator<PlayMvBean.ResultBean.FilesBean.FileItemBean>() {
                @Override
                public int compare(PlayMvBean.ResultBean.FilesBean.FileItemBean lhs, PlayMvBean.ResultBean.FilesBean.FileItemBean rhs) {
                    return lhs.getFile_size().compareTo(rhs.getFile_size());
                }
            });

            pureVideoPlayer.setUri(Uri.parse(list.get(0).getFile_link()));
        }

        pureVideoPlayer.initView();
        pureVideoPlayer.setVideoTitle(bean.getResult().getMv_info().getTitle());
//        Media media = new Media(VLCInstance.getInstance(), Uri.parse(bean.getResult().getFiles().get_$41().getFile_link()));
//        mMediaPlayer.setMedia(media);
//        media.release();
//        mMediaPlayer.getVLCVout().detachViews();
//        //mMediaPlayer.getVLCVout().attachViews();
//        mMediaPlayer.getVLCVout().setVideoView(surfaceView.getTextureView());
//        mMediaPlayer.getVLCVout().attachViews();
//        mMediaPlayer.getVLCVout().addCallback(new IVLCVout.Callback() {
//            @Override
//            public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen) {
//                Log.e("weikaizhi", "onNewLayout");
//            }
//
//            @Override
//            public void onSurfacesCreated(IVLCVout vlcVout) {
//                Log.e("weikaizhi", "onSurfacesCreated");
//            }
//
//            @Override
//            public void onSurfacesDestroyed(IVLCVout vlcVout) {
//                Log.e("weikaizhi", "onSurfacesDestroyed");
//            }
//        });
//
//        surfaceView.setVisibility(View.VISIBLE);
//        mMediaPlayer.setVideoTitleDisplay(MediaPlayer.Position.Disable, 0);
//        mMediaPlayer.play();
    }

    @Override
    public void onGetMvCategorySuccess(MvCategoryBean bean) {

    }

    @Override
    public void onSearchMvSuccess(MvSearchBean bean) {

    }

    @Override
    public void onBackPressed() {
        if (pureVideoPlayer != null && !pureVideoPlayer.exitFullScreen()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mMediaPlayer.getVLCVout().detachViews();
//        mMediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        pureVideoPlayer.closeMediaPlayer();
    }
}
