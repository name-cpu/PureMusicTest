package com.example.kaizhiwei.puremusictest.NetAudio.bangdan;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.NetAudio.tuijian.SongListAdapter;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.BangDanListBean;
import com.example.kaizhiwei.puremusictest.bean.BangDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanSongDetailInfo;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.BangDanInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.BangDanInfoPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/7/22.
 */

public class BangDanSongListActivity extends MyBaseActivity implements BangDanInfoContract.View, SongListAdapter.OnSongItemListener {
    private SongListAdapter mAdapter;
    private List<SongListAdapter.SongItemData> mSongListDatas = new ArrayList<>();
    @Bind(R.id.rvSongList)
    XRecyclerView rvSongList;
    @Bind(R.id.ivBigBack)
    ImageView ivBigBack;
    @Bind(R.id.tvAlbumName)
    TextView tvAlbumName;
    @Bind(R.id.tvPublishTime)
    TextView tvPublishTime;

    private BangDanInfoPresenter mPresenter;
    private BangDanListBean.ContentBeanX mBangDanInfo;
    private int mBangDanType;
    private String mFields;
    public static final String INTENT_BANGDANINFO = "INTENT_BANGDANINFO";
    public static final String INTENT_BANGDANTYPE = "INTENT_BANGDANTYPE";

    @Override
    public int getLayoutId() {
        return R.layout.activity_bangdan_songlist;
    }

    @Override
    public void initPresenter() {
        mPresenter = new BangDanInfoPresenter(this);
    }

    @Override
    public void initView() {
        tvAlbumName.setText(mBangDanInfo.getName());
        //tvPublishTime.setText(mBangDanInfo.getComment());
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

    @Override
    public void initData() {
        mBangDanInfo = getIntent().getParcelableExtra(INTENT_BANGDANINFO);
        mBangDanType = getIntent().getIntExtra(INTENT_BANGDANTYPE, 0);
        mFields = Uri.encode("song_id,title,author,album_title,pic_big,pic_small,havehigh,all_rate,charge,has_mv_mobile,learn,song_source,korean_bb_song");
        if(mPresenter != null){
            mPresenter.getBangDanInfo(PureMusicContant.FORMAT_JSON, PureMusicContant.DEVICE_TYPE, "baidu.ting.billboard.billList", mBangDanType,  0, 100, mFields);
        }
    }

    @Override
    public void onError(String strErrMsg) {
        
    }

    @Override
    public void onGetBangDanInfoSuccess(BangDanSongDetailInfo bean) {
        if(bean == null || bean.getSong_list() == null)
            return;

        int rankLevelResId = R.drawable.img_king_mask1;
        for(int i = 0;i < bean.getSong_list().size();i++){
            SongListAdapter.SongItemData songItemData = new SongListAdapter.SongItemData();
            songItemData.has_mv = bean.getSong_list().get(i).getHas_mv();
            songItemData.title = bean.getSong_list().get(i).getTitle();
            songItemData.havehigh = bean.getSong_list().get(i).getHavehigh();
            songItemData.song_id = bean.getSong_list().get(i).getSong_id();
            songItemData.has_mv_mobile = bean.getSong_list().get(i).getHas_mv_mobile();
            songItemData.author = bean.getSong_list().get(i).getAuthor();
            songItemData.artist_id = bean.getSong_list().get(i).getAuthor();
            songItemData.album_title = bean.getSong_list().get(i).getAlbum_title();
            songItemData.album_id = bean.getSong_list().get(i).getAlbum_id();

            if(i == 0){
                rankLevelResId = R.drawable.img_king_mask01;
            }
            else if(i == 1){
                rankLevelResId = R.drawable.img_king_mask02;
            }
            else if(i == 2){
                rankLevelResId = R.drawable.img_king_mask03;
            }
            else{
                rankLevelResId = R.drawable.img_king_mask1;
            }
            songItemData.songRankLevelResId = rankLevelResId;
            songItemData.songRankNum = (i+1) + "";
            songItemData.songImageUrl = bean.getSong_list().get(i).getPic_small();

            mSongListDatas.add(songItemData);
        }
        mAdapter.setDatas(mSongListDatas);
        mAdapter.notifyDataSetChanged();
        Glide.with(this).load(mBangDanInfo.getPic_s444()).into(ivBigBack);
    }

    @Override
    public void onGetBangDanSongDetailInfoSuccess(GeDanSongDetailInfo bean) {
        
    }

    @Override
    public void onSongItemClick(SongListAdapter adapter, int position) {

    }
}
