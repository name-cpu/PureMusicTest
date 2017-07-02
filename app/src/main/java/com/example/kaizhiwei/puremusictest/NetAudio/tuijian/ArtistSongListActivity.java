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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetSongListContract;
import com.example.kaizhiwei.puremusictest.presenter.ArtistGetSongListPresenter;
import com.example.kaizhiwei.puremusictest.widget.CornersTransform;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import com.example.kaizhiwei.puremusictest.widget.CircleImageView;
import butterknife.Bind;
import butterknife.BindInt;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by kaizhiwei on 17/7/2.
 */

public class ArtistSongListActivity extends MyBaseActivity implements ArtistGetSongListContract.View {
    private ArtistGetSongListContract.Presenter mPresenter;
    private ArtistGetListBean.ArtistBean mArtistBean;
    private int mOffset;
    private int mLimit = 50;
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

    private ArtistGetSongListBean mArtistSongListBean;
    private Map<String, List<ArtistGetSongListBean.SonglistBean>> mMapAlbums;

    @Override
    public int getLayoutId() {
        return R.layout.activity_artist_song_list;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ArtistGetSongListPresenter(this);
    }

    @Override
    public void initView() {
        if(mArtistBean == null)
            return;

        tvArtistName.setText(mArtistBean.getName());
        tabLayout.setTabTextColors(this.getResources().getColor(R.color.mainTextColor), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        tabLayout.setSelectedTabIndicatorColor(indicatorColor);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        Glide.with(this).load(mArtistBean.getAvatar_middle()).transform(new CornersTransform(this,ivArtistPic.getWidth()/2)).into(ivArtistPic);
        Glide.with(this).load(mArtistBean.getAvatar_big()).bitmapTransform(new BlurTransformation(HomeActivity.getInstance(), 50)).into(ivBigBack);
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
            mPresenter.getSongList(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, 2, "baidu.ting.artist.getSongList"
                    , PureMusicContant.FORMAT_JSON, 2 + "", mArtistBean.getTing_uid(), mArtistBean.getArtist_id(), mOffset, mLimit);
        }

        mMapAlbums = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                int l = Integer.valueOf(lhs);
                int r = Integer.valueOf(rhs);
                return l - r;
            }
        });
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetSongListSuccess(ArtistGetSongListBean bean) {
        if(bean == null || bean.getSonglist() == null)
            return;

        mMapAlbums.clear();
        for(int i = 0;i < bean.getSonglist().size();i++){
            ArtistGetSongListBean.SonglistBean beanSong = bean.getSonglist().get(i);
            List<ArtistGetSongListBean.SonglistBean> list = mMapAlbums.get(beanSong.getAlbum_id());
            if(list == null){
                list = new ArrayList<>();
            }
            list.add(beanSong);
            mMapAlbums.put(beanSong.getAlbum_id(), list);
        }


        mArtistSongListBean = bean;
        MyPageAdapter adapter = new MyPageAdapter();
        vpSong.setAdapter(adapter);
        tabLayout.setupWithViewPager(vpSong);
    }

    private class MyPageAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return String.format("歌曲(%s)", mArtistBean.getSongs_total());
            }
            else if(position == 1){
                return String.format("专辑(%s)", mArtistBean.getAlbums_total());
            }

            return "";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public Object instantiateItem(ViewGroup container, int position) {
            RecyclerView recyclerView = new RecyclerView(ArtistSongListActivity.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ArtistSongListActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.addItemDecoration(new RecyclerViewDividerDecoration(ArtistSongListActivity.this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
            if(position == 0){
                SongListAdapter adapter = new SongListAdapter();
                recyclerView.setAdapter(adapter);
            }
            else{
                AlbumListAdapter albumListAdapter = new AlbumListAdapter();
                recyclerView.setAdapter(albumListAdapter);
            }
            container.addView(recyclerView);
            return recyclerView;
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
            ArtistGetSongListBean.SonglistBean songlistBean = mArtistSongListBean.getSonglist().get(position);
            holder.tvSongName.setText(songlistBean.getTitle());
            String strAlbumTitle = songlistBean.getAlbum_title();
            if(TextUtils.isEmpty(strAlbumTitle)){
                holder.tvAlbumName.setText(ArtistSongListActivity.this.getString(R.string.solo));
            }
            else{
                holder.tvAlbumName.setText("《" + strAlbumTitle + "》");
            }
        }

        @Override
        public int getItemCount() {
            return mArtistSongListBean.getSonglist().size();
        }
    }

    private class SongListViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSongName;
        private TextView tvAlbumName;
        public SongListViewHolder(View itemView) {
            super(itemView);
            tvSongName = (TextView)itemView.findViewById(R.id.tvSongName);
            tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
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
            Set<String> keys = mMapAlbums.keySet();
            int i = 0;
            String strKey = null;
            for(String key : keys){
                if(i == position){
                    strKey = key;
                    break;
                }
                i++;
            }

            List<ArtistGetSongListBean.SonglistBean> list = mMapAlbums.get(strKey);
            if(list == null || list.size() == 0)
                return;

            holder.tvAlbumName.setText(list.get(0).getAlbum_title());
            holder.tvPublishTime.setText(list.get(0).getPublishtime());
            Glide.with(ArtistSongListActivity.this).load(list.get(0).getPic_small()).error(R.drawable.default_live_ic).into(holder.ivAlbum);
        }

        @Override
        public int getItemCount() {
            return mMapAlbums.size();
        }
    }

    private class AlbumListViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivAlbum;
        private TextView tvAlbumName;
        private TextView tvPublishTime;

        public AlbumListViewHolder(View itemView) {
            super(itemView);
            ivAlbum = (ImageView)itemView.findViewById(R.id.ivAlbum);
            tvAlbumName = (TextView)itemView.findViewById(R.id.tvAlbumName);
            tvPublishTime = (TextView)itemView.findViewById(R.id.tvPublishTime);
        }
    }
}
