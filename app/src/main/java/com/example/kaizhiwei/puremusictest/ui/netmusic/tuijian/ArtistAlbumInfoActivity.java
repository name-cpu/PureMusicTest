package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;

import butterknife.Bind;
import butterknife.OnClick;
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
    @Bind(R.id.rvSongList)
    RecyclerView rvSongList;
    private SongListAdapter mAdapter;
    private ArtistAlbumInfoBean artistAlbumInfoBean;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvSongList.setLayoutManager(linearLayoutManager);
        rvSongList.addItemDecoration(new RecyclerViewDividerDecoration(this, RecyclerViewDividerDecoration.HORIZONTAL_LIST));
//        rvSongList.setPullRefreshEnabled(false);
//        rvSongList.setLoadingMoreEnabled(true);

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
            mPresenter.getArtistAlbumInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.PPZS, 2, "baidu.ting.album.getAlbumInfo"
                    , PureMusicContant.FORMAT_JSON, mAlbumSimpleInfoBean.getAlbum_id());
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
    }

    @Override
    public void onGetAlbumInfoSuccess(ArtistAlbumInfoBean bean) {
        if(bean == null || bean.getSonglist() == null)
            return;

        artistAlbumInfoBean = bean;
        mAdapter = new SongListAdapter();
        rvSongList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private class SongListAdapter extends RecyclerView.Adapter<SongListViewHolder>{

        @Override
        public SongListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ArtistAlbumInfoActivity.this).inflate(R.layout.item_artist_songlist, parent, false);
            return new SongListViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SongListViewHolder holder, int position) {
            ArtistAlbumInfoBean.SonglistBean songlistBean = artistAlbumInfoBean.getSonglist().get(position);
            holder.tvSongName.setText(songlistBean.getTitle());
            String strAlbumTitle = songlistBean.getAlbum_title();
            if(TextUtils.isEmpty(strAlbumTitle)){
                holder.tvAlbumName.setText(ArtistAlbumInfoActivity.this.getString(R.string.solo));
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

                }
            });
        }

        @Override
        public int getItemCount() {
            if(artistAlbumInfoBean == null || artistAlbumInfoBean.getSonglist() == null)
                return 0;

            return artistAlbumInfoBean.getSonglist().size();
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
}
