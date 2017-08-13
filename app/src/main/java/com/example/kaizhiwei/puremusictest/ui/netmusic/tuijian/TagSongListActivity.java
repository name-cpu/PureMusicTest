package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.BaseSongInfoBean;
import com.example.kaizhiwei.puremusictest.bean.TagSongListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.TagSongListContract;
import com.example.kaizhiwei.puremusictest.presenter.TagSongListPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class TagSongListActivity extends MyBaseActivity implements TagSongListContract.View, SongListAdapter.OnSongItemListener {
    private TagSongListPresenter mPresenter;
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

    public static final String INTENT_TAGNAME = "INTENT_TAGNAME";
    private String mSelTagName;

    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_tagsonglist;
    }

    @Override
    public void initPresenter() {
        mPresenter = new TagSongListPresenter(this);
    }

    @Override
    public void initView() {
        rvSongList.setNestedScrollingEnabled(false);

        tvAlbumName.setText(mSelTagName);
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
        mSelTagName = getIntent().getStringExtra(INTENT_TAGNAME);
        mPresenter.getTagSongList(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "baidu.ting.tag.songlist", "json", mSelTagName, 100);
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetTagSongLiistSuccess(TagSongListBean bean) {
        if(bean == null || bean.getTaginfo() == null || bean.getTaginfo().getSonglist() == null)
            return;
        
        for(int i = 0;i < bean.getTaginfo().getSonglist().size();i++){
            SongListAdapter.SongItemData songItemData = new SongListAdapter.SongItemData();
            songItemData.has_mv = bean.getTaginfo().getSonglist().get(i).getHas_mv();
            songItemData.title = bean.getTaginfo().getSonglist().get(i).getTitle();
            songItemData.havehigh = bean.getTaginfo().getSonglist().get(i).getHavehigh();
            songItemData.song_id = bean.getTaginfo().getSonglist().get(i).getSong_id();
            songItemData.has_mv_mobile = bean.getTaginfo().getSonglist().get(i).getHas_mv_mobile();
            songItemData.author = bean.getTaginfo().getSonglist().get(i).getAuthor();
            songItemData.artist_id = bean.getTaginfo().getSonglist().get(i).getArtist_id();
            songItemData.album_title = bean.getTaginfo().getSonglist().get(i).getAlbum_title();
            songItemData.album_id = bean.getTaginfo().getSonglist().get(i).getAlbum_id();
            mSongListDatas.add(songItemData);
        }
        mAdapter.setDatas(mSongListDatas);
        mAdapter.notifyDataSetChanged();

        if(mPresenter != null && bean.getTaginfo().getSonglist().size() > 0){
            mPresenter.getSongBaseInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "bbaidu.ting.song.baseInfos", "json", bean.getTaginfo().getSonglist().get(0).getSong_id());
        }
    }

    @Override
    public void onGetSongBaseInfoSuccess(BaseSongInfoBean baseSongInfoBane) {
        Glide.with(this).load(baseSongInfoBane.getResult().getItems().get(0).getPic_small()).into(ivAlbumPic);
        Glide.with(this).load(baseSongInfoBane.getResult().getItems().get(0).getAvatar_big()).bitmapTransform(new BlurTransformation(HomeActivity.getInstance(), 50)).into(ivBigBack);

    }

    @Override
    public void onSongItemClick(SongListAdapter adapter, int position) {

    }

    @OnClick(R.id.ivBack)
    void onClicked(View view){
        if(view.getId() == R.id.ivBack){
            finish();
        }
    }
}
