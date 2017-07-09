package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistInfoBean;
import com.example.kaizhiwei.puremusictest.bean.SongDetailInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetSongListContract;
import com.example.kaizhiwei.puremusictest.contract.SongDetailInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.ArtistGetSongListPresenter;
import com.example.kaizhiwei.puremusictest.presenter.SongDetailInfoPresenter;
import com.example.kaizhiwei.puremusictest.widget.CornersTransform;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import java.util.ArrayList;
import java.util.List;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.Bind;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/7/2.
 */

public class ArtistSongListActivity extends MyBaseActivity implements ArtistGetSongListContract.View, SongDetailInfoContract.View {
    private ArtistGetSongListContract.Presenter mPresenter;
    private SongDetailInfoContract.Presenter mSongDetailPresenter;
    private ArtistGetListBean.ArtistBean mArtistBean;
    private int mOffset = 0;
    private int mLimit = 50;
    private int mAlbumOffset = 0;
    private int mAlbumLimit = 30;
    public static final String ARTIST_BEAN = "ARTIST_BEAN";
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;
    @Bind(R.id.tvArtistName)
    TextView tvArtistName;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.vpSong)
    ViewPager vpSong;
    @Bind(R.id.ivArtistPic)
    ImageView ivArtistPic;
    @Bind(R.id.ivBigBack)
    ImageView ivBigBack;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvListen)
    TextView tvListen;
    @Bind(R.id.tvArtistArea)
    TextView tvArtistArea;

    private ArtistGetSongListBean mArtistSongListBean = new ArtistGetSongListBean();
    private ArtistAlbumListBean mAlbumListBean = new ArtistAlbumListBean();
    private ArtistInfoBean mArtistInfoBean;
    private XRecyclerView recyclerViewSongList;
    private SongListAdapter songListAdapter;
    private XRecyclerView recyclerViewAlbumList;
    private AlbumListAdapter albumListAdapter;
    private MyPageAdapter pageAdapter;
    private boolean mNeedClear = true;

    @Override
    public int getLayoutId() {
        return R.layout.activity_artist_song_list;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ArtistGetSongListPresenter(this);
        mSongDetailPresenter = new SongDetailInfoPresenter(this);
    }

    @Override
    public void initView() {
        if(mArtistBean == null)
            return;

        systemBarTintManager.setStatusBarTintColor(android.R.color.transparent);
        tvArtistName.setText(mArtistBean.getName());
        tabLayout.setTabTextColors(this.getResources().getColor(R.color.mainTextColor), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        tabLayout.setSelectedTabIndicatorColor(indicatorColor);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        int width = ivArtistPic.getLayoutParams().width;

        Glide.with(this).load(mArtistBean.getAvatar_middle()).transform(new CornersTransform(this,width/2)).into(ivArtistPic);
        Glide.with(this).load(mArtistBean.getAvatar_big()).bitmapTransform(new BlurTransformation(HomeActivity.getInstance(), 50)).into(ivBigBack);

        recyclerViewSongList = new XRecyclerView(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArtistSongListActivity.this);
        recyclerViewSongList.setLayoutManager(linearLayoutManager);
        recyclerViewSongList.addItemDecoration(new RecyclerViewDividerDecoration(ArtistSongListActivity.this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        recyclerViewSongList.setPullRefreshEnabled(false);
        recyclerViewSongList.setLoadingMoreEnabled(true);
        recyclerViewSongList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mNeedClear = false;
                mOffset++;
                getSongList();
            }
        });
        songListAdapter = new SongListAdapter();
        recyclerViewSongList.setAdapter(songListAdapter);
        recyclerViewSongList.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

        recyclerViewAlbumList = new XRecyclerView(this);
        linearLayoutManager = new LinearLayoutManager(ArtistSongListActivity.this);
        recyclerViewAlbumList.setLayoutManager(linearLayoutManager);
        recyclerViewAlbumList.addItemDecoration(new RecyclerViewDividerDecoration(ArtistSongListActivity.this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
        recyclerViewAlbumList.setPullRefreshEnabled(false);
        recyclerViewAlbumList.setLoadingMoreEnabled(true);
        recyclerViewAlbumList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mNeedClear = false;
                mAlbumOffset++;
                getAlbumList();
            }
        });

        albumListAdapter = new AlbumListAdapter();
        recyclerViewAlbumList.setAdapter(albumListAdapter);
        recyclerViewAlbumList.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

        pageAdapter = new MyPageAdapter();
        vpSong.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(vpSong);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                mArtistBean = bundle.getParcelable(ARTIST_BEAN);
            }
        }

        if(mArtistBean != null && mPresenter != null){
            mPresenter.getArtistInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 2, "baidu.ting.artist.getinfo"
                    , PureMusicContant.FORMAT_JSON, mArtistBean.getTing_uid(), mArtistBean.getArtist_id());
        }
        getSongList();
        getAlbumList();
    }

    private void getSongList(){
        if(mArtistBean != null && mPresenter != null){
            mPresenter.getSongList(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 2, "baidu.ting.artist.getSongList"
                    , PureMusicContant.FORMAT_JSON, 2 + "", mArtistBean.getTing_uid(), mArtistBean.getArtist_id(), mOffset*mLimit, mLimit);
           }
    }

    private void getAlbumList(){
        if(mArtistBean != null && mPresenter != null){
            mPresenter.getArtistAlbumList(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 2, "baidu.ting.artist.getAlbumList"
                    , PureMusicContant.FORMAT_JSON, 1 + "", mArtistBean.getTing_uid(), mAlbumOffset*mAlbumLimit, mAlbumLimit);
        }
    }

    @OnClick(R.id.ivBack)
    void onClick(View view){
        if(view.getId() == R.id.ivBack){
            finish();
        }
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
        recyclerViewAlbumList.loadMoreComplete();
        recyclerViewSongList.loadMoreComplete();
    }

    @Override
    public void onGetSongListSuccess(ArtistGetSongListBean bean) {
        recyclerViewSongList.loadMoreComplete();

        if(bean == null || bean.getSonglist() == null)
            return;

        if(mArtistSongListBean.getSonglist() == null){
            List<ArtistGetSongListBean.SonglistBean> list = new ArrayList<>();
            mArtistSongListBean.setSonglist(list);
        }

        if(mNeedClear){
            mArtistSongListBean.getSonglist().clear();
        }
        mArtistSongListBean.getSonglist().addAll(bean.getSonglist());
        songListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetArtistInfoSuccess(ArtistInfoBean bean) {
        if(bean == null)
            return;

        String country = bean.getCountry();
        if(TextUtils.isEmpty(bean.getCountry())){
            country = getResources().getString(R.string.unknown);
        }
        country += getResources().getString(R.string.songer);
        tvArtistArea.setText(country);
        tvListen.setText(bean.getAlbums_total());
        mArtistInfoBean = bean;
        pageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetArtistAlbumListSuccess(ArtistAlbumListBean bean) {
        recyclerViewAlbumList.loadMoreComplete();
        if(bean == null || bean.getAlbumlist() == null)
            return;

        if(mAlbumListBean.getAlbumlist() == null){
            List<ArtistAlbumListBean.AlbumlistBean> list = new ArrayList<>();
            mAlbumListBean.setAlbumlist(list);
        }

        if(mNeedClear){
            mAlbumListBean.getAlbumlist().clear();
        }
        mAlbumListBean.getAlbumlist().addAll(bean.getAlbumlist());
        albumListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tvKnowMore)
    void onClicked(View view){

    }

    @Override
    public void onGetSongDetailInfoSuccess(SongDetailInfoBean bean) {
        Log.e("onGetSongDetail", bean.getSonginfo().getAll_rate());
    }

    private class MyPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(mArtistInfoBean == null)
                return "";

            if(position == 0){
                return String.format("歌曲(%s)", mArtistInfoBean.getSongs_total());
            }
            else if(position == 1){
                return String.format("专辑(%s)", mArtistInfoBean.getAlbums_total());
            }

            return "";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            if(position == 0){
                container.addView(recyclerViewSongList);
                return recyclerViewSongList;
            }
            else{
                container.addView(recyclerViewAlbumList);
                return recyclerViewAlbumList;
            }
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

    private class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder>{

        @Override
        public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ArtistSongListActivity.this).inflate(R.layout.item_artist_songlist, parent, false);
            return new SongListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SongListViewHolder holder, int position) {
            final ArtistGetSongListBean.SonglistBean songlistBean = mArtistSongListBean.getSonglist().get(position);
            holder.tvSongName.setText(songlistBean.getTitle());
            String strAlbumTitle = songlistBean.getAlbum_title();
            if(TextUtils.isEmpty(strAlbumTitle)){
                holder.tvAlbumName.setText(ArtistSongListActivity.this.getString(R.string.solo));
            }
            else{
                holder.tvAlbumName.setText("《" + strAlbumTitle + "》");
            }

            if(songlistBean.getHas_mv() == 1){
                holder.ivMV.setVisibility(View.VISIBLE);
            }
            else{
                holder.ivMV.setVisibility(View.GONE);
            }

            if(songlistBean.getHavehigh() == 2){
                holder.ivSQ.setVisibility(View.VISIBLE);
            }
            else{
                holder.ivSQ.setVisibility(View.GONE);
            }

            holder.llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mSongDetailPresenter != null){
                        long timeStamp = 1499570577194L; //System.currentTimeMillis();
                        mSongDetailPresenter.getSongDetailInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "baidu.ting.song.getInfos"
                                , PureMusicContant.FORMAT_JSON, "qqqqqq", timeStamp, "%2Blok1Cpy4gCBBj6rXQ4QnXmjJ7U0WCkfwOIhDHWwvQY%3D", 1, 0, 0, 0, "", 0, 0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mArtistSongListBean == null || mArtistSongListBean.getSonglist() == null)
                return 0;

            return mArtistSongListBean.getSonglist().size();
        }
    }

    private class SongListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSongName;
        private TextView tvAlbumName;
        private LinearLayout llContent;
        private ImageView ivMV;
        private ImageView ivSQ;
        public SongListViewHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout)itemView.findViewById(R.id.llContent);
            tvSongName = (TextView)itemView.findViewById(R.id.tvSongName);
            tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
            ivMV = (ImageView)itemView.findViewById(R.id.ivMV);
            ivSQ = (ImageView)itemView.findViewById(R.id.ivSQ);
        }
    }

    private class AlbumListAdapter extends RecyclerView.Adapter<AlbumListViewHolder>{

        @Override
        public AlbumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ArtistSongListActivity.this).inflate(R.layout.item_artist_albumlist, parent, false);
            return new AlbumListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(AlbumListViewHolder holder, int position) {
            final ArtistAlbumListBean.AlbumlistBean albumlistBean = mAlbumListBean.getAlbumlist().get(position);
            if(albumlistBean == null)
                return;

            holder.tvAlbumName.setText(albumlistBean.getTitle());
            holder.tvPublishTime.setText(albumlistBean.getPublishtime());
            Glide.with(ArtistSongListActivity.this).load(albumlistBean.getPic_small()).error(R.drawable.default_live_ic).into(holder.ivAlbum);
            holder.llContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ArtistSongListActivity.this, ArtistAlbumInfoActivity.class);
                    intent.putExtra(ArtistAlbumInfoActivity.ALBUM_BEAN, albumlistBean);
                    ArtistSongListActivity.this.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mAlbumListBean == null || mAlbumListBean.getAlbumlist() == null)
                return 0;

            return mAlbumListBean.getAlbumlist().size();
        }
    }

    private class AlbumListViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAlbum;
        private TextView tvAlbumName;
        private TextView tvPublishTime;
        private LinearLayout llContent;

        public AlbumListViewHolder(View itemView) {
            super(itemView);
            llContent = (LinearLayout)itemView.findViewById(R.id.llContent);
            ivAlbum = (ImageView)itemView.findViewById(R.id.ivAlbum);
            tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
            tvPublishTime = (TextView)itemView.findViewById(R.id.tvPublishTime);
        }
    }
}
