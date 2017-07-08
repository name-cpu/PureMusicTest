package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.CommonUI.AlwaysMarqueeTextView;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetAlbumInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.ArtistGetAlbumInfoPresenter;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/7/8.
 */

public class ArtistAlbumInfoActivity extends MyBaseActivity implements ArtistGetAlbumInfoContract.View{
    private ArtistGetAlbumInfoContract.Presenter mPresenter;
    @Bind(R.id.ivBigBack)
    ImageView ivBigBack;
    @Bind(R.id.tvAlbumName)
    AlwaysMarqueeTextView tvAlbumName;
    @Bind(R.id.tvPublishTime)
    TextView tvPublishTime;
    @Bind(R.id.ivAlbumPic)
    ImageView ivAlbumPic;
    @Bind(R.id.tvArtistName)
    TextView tvArtistName;

    public static final String ALBUM_BEAN = "ALBUM_BEAN";
    private ArtistAlbumListBean.AlbumlistBean mAlbumSimpleInfoBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_artist_album_info;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ArtistGetAlbumInfoPresenter(this);
    }

    @Override
    public void initView() {
        if(mAlbumSimpleInfoBean == null)
            return;

        systemBarTintManager.setStatusBarTintColor(android.R.color.transparent);
        tvArtistName.setText(mAlbumSimpleInfoBean.getAuthor());
        tvAlbumName.setText(mAlbumSimpleInfoBean.getTitle());
        tvPublishTime.setText(getResources().getString(R.string.publish_time) + mAlbumSimpleInfoBean.getPublishtime());
        Glide.with(this).load(mAlbumSimpleInfoBean.getPic_small()).into(ivAlbumPic);
        Glide.with(this).load(mAlbumSimpleInfoBean.getPic_big()).bitmapTransform(new BlurTransformation(HomeActivity.getInstance(), 50)).into(ivBigBack);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                mAlbumSimpleInfoBean = bundle.getParcelable(ALBUM_BEAN);
            }
        }

        if(mAlbumSimpleInfoBean != null && mPresenter != null){
            mPresenter.getArtistAlbumInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 2, "baidu.ting.artist.getinfo"
                    , PureMusicContant.FORMAT_JSON, mAlbumSimpleInfoBean.getAlbum_id());
        }
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetAlbumInfoSuccess(ArtistAlbumInfoBean bean) {

    }
}
