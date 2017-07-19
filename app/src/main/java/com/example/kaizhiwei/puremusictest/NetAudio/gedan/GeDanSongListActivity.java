package com.example.kaizhiwei.puremusictest.NetAudio.gedan;

import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.NetAudio.tuijian.SongListAdapter;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.GeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.GeDanInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.DownloadPresenter;
import com.example.kaizhiwei.puremusictest.presenter.GeDanInfoPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
/**
 * Created by kaizhiwei on 17/7/19.
 */

public class GeDanSongListActivity extends MyBaseActivity implements GeDanInfoContract.View, SongListAdapter.OnSongItemListener {
    private GeDanInfoPresenter mPresenter;
    private DownloadPresenter mDownloadPresenter;

    private SongListAdapter mAdapter;
    private List<SongListAdapter.SongItemData> mSongListDatas = new ArrayList<>();
    @Bind(R.id.rvSongList)
    XRecyclerView rvSongList;
    @Bind(R.id.ivBigBack)
    ImageView ivBigBack;
    @Bind(R.id.ivAlbumPic)
    ImageView ivAlbumPic;
    @Bind(R.id.tvAlbumName)
    TextView tvAlbumName;

    private GeDanListBean.SongListInfo mGeDanInfo;
    public static final String INTENT_GEDANINFO = "INTENT_GEDANINFO";

    @Override
    public int getLayoutId() {
        return R.layout.activity_gedan_songlist;
    }

    @Override
    public void initPresenter() {
        mPresenter = new GeDanInfoPresenter(this);
        mDownloadPresenter = new DownloadPresenter();
    }

    @Override
    public void initView() {
        tvAlbumName.setText(mGeDanInfo.getTitle());
        rvSongList.setNestedScrollingEnabled(false);

        systemBarTintManager.setStatusBarTintResource(android.R.color.transparent);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSongList.setLayoutManager(linearLayoutManager);
        rvSongList.addItemDecoration(new RecyclerViewDividerDecoration(this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        rvSongList.setPullRefreshEnabled(false);
        rvSongList.setLoadingMoreEnabled(true);
        rvSongList.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rvSongList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
//                mAlbumOffset++;
//                getAlbumList();
            }
        });

        mAdapter = new SongListAdapter(this);
        mAdapter.setListener(this);
        rvSongList.setAdapter(mAdapter);
    }

    @OnClick(R.id.ivBack)
    void onClicked(View view){
        if(view.getId() == R.id.ivBack){
            finish();
        }
    }

    @Override
    public void initData() {
        mGeDanInfo = getIntent().getParcelableExtra(INTENT_GEDANINFO);
        if(mPresenter != null){
            mPresenter.getGeDanInfo(PureMusicContant.FORMAT_JSON, PureMusicContant.DEVICE_TYPE, "baidu.ting.diy.gedanInfo", mGeDanInfo.getListid());
        }
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
    }

    @Override
    public void onGetGeDanInfoSuccess(GeDanInfoBean bean) {
        if(bean == null || bean.getContent() == null)
            return;

        for(int i = 0;i < bean.getContent().size();i++){
            SongListAdapter.SongItemData songItemData = new SongListAdapter.SongItemData();
            songItemData.has_mv = bean.getContent().get(i).getHas_mv();
            songItemData.title = bean.getContent().get(i).getTitle();
            songItemData.havehigh = bean.getContent().get(i).getHavehigh();
            songItemData.song_id = bean.getContent().get(i).getSong_id();
            songItemData.has_mv_mobile = bean.getContent().get(i).getHas_mv_mobile();
            songItemData.author = bean.getContent().get(i).getAuthor();
            songItemData.artist_id = bean.getContent().get(i).getAuthor();
            songItemData.album_title = bean.getContent().get(i).getAlbum_title();
            songItemData.album_id = bean.getContent().get(i).getAlbum_id();
            mSongListDatas.add(songItemData);
        }
        mAdapter.setDatas(mSongListDatas);
        mAdapter.notifyDataSetChanged();
        Glide.with(this).load(bean.getPic_300()).into(ivAlbumPic);
        Glide.with(this).load(bean.getPic_500()).bitmapTransform(new BlurTransformation(this, 50)).into(ivBigBack);
    }

    @Override
    public void onGetGeDanSongDetailInfoSuccess(GeDanSongDetailInfo songDetailInfo) {
        if(songDetailInfo == null || songDetailInfo.getBitrate() == null)
            return;

        Log.e("weikaizhi", songDetailInfo.getBitrate().getFile_link());

        String strMusicDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getPackageName() + File.separator;
        File fileMusicDir = new File(strMusicDir);
        if(fileMusicDir.exists() == false){
            fileMusicDir.mkdirs();
        }
        String fileName = getUrlFileName(songDetailInfo.getBitrate().getFile_link());
        mDownloadPresenter.downloadFile(songDetailInfo.getBitrate().getFile_link(), strMusicDir + fileName, null);

    }

    @Override
    public void onSongItemClick(SongListAdapter adapter, int position) {
        if(mPresenter != null){
            mPresenter.getGeDanSongDetail(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.FORMAT_JSON, "baidu.ting.song.play", mSongListDatas.get(position).song_id);
        }
    }

    private String getUrlFileName(String strUrl){
        int index = strUrl.lastIndexOf(File.separator);
        return strUrl.substring(index+1, strUrl.length());
    }
}
