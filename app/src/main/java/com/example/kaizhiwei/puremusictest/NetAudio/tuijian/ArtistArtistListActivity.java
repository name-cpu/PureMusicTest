package com.example.kaizhiwei.puremusictest.NetAudio.tuijian;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.CommonUI.BaseActivty;
import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ActiveIndexBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.bean.DiyGeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.PlazaIndexBean;
import com.example.kaizhiwei.puremusictest.bean.SceneCategoryListBean;
import com.example.kaizhiwei.puremusictest.bean.ShowRedPointBean;
import com.example.kaizhiwei.puremusictest.bean.SugSceneBean;
import com.example.kaizhiwei.puremusictest.bean.UgcdiyBaseInfoBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ResetServerContract;
import com.example.kaizhiwei.puremusictest.presenter.ResetServerPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/6/29.
 */

public class ArtistArtistListActivity extends MyBaseActivity implements ResetServerContract.View{
    @Bind(R.id.rvArtistList)
    RecyclerView rvArtistList;

    @Bind(R.id.tvSubType)
    TextView tvSubType;

    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;

    ResetServerContract.Presenter mPresenter;
    private ArtistGetListBean mArtistGetListBean = new ArtistGetListBean();
    private ArtistSelActivity.ArtistTagInfo mArtistTagInfo;
    private boolean mLoadingData;
    private int mCurPage = 0;

    public static final String BUNDLE_ARTISTTAGINFO= "BUNDLE_ArtistTagInfo";

    @Override
    public int getLayoutId() {
        return R.layout.activity_artist_artistlist;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ResetServerPresenter(this);
    }

    @Override
    public void initView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvArtistList.setLayoutManager(linearLayoutManager);
        rvArtistList.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) //向下滚动
                {
                    int visibleItemCount = linearLayoutManager.getChildCount();
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!mLoadingData && (visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        mLoadingData = true;
                        loadMoreDate();
                    }
                }
            }
        });
        if(mArtistTagInfo != null){
            commonTitle.setTitleViewInfo(mArtistTagInfo.title, "", "");
        }
    }

    private void loadMoreDate() {
        if(mPresenter != null){
            mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2", "baidu.ting.artist.getList"
                    , PureMusicContant.FORMAT_JSON, mCurPage*48 + "", "48", "1", mArtistTagInfo.area + "", mArtistTagInfo.sex + "");
        }
    }

    @Override
    public void initData() {
        List<ArtistGetListBean.ArtistBean> list = new ArrayList<>();
        mArtistGetListBean.setArtist(list);

        Intent intent = getIntent();
        if(intent == null)
            return;

        Bundle bundle = intent.getExtras();
        if(bundle == null)
            return;

        mArtistTagInfo = (ArtistSelActivity.ArtistTagInfo)bundle.getParcelable(BUNDLE_ARTISTTAGINFO);
        if(mPresenter != null){
            mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2", "baidu.ting.artist.getList"
                    , PureMusicContant.FORMAT_JSON, mCurPage*48 + "", "48", "1", mArtistTagInfo.area + "", mArtistTagInfo.sex + "");
        }
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetCatogaryListSuccess(SceneCategoryListBean bean) {

    }

    @Override
    public void onGetActiveIndexSuccess(ActiveIndexBean bean) {

    }

    @Override
    public void onShowRedPointSuccess(ShowRedPointBean bean) {

    }

    @Override
    public void onGetSugSceneSuccess(SugSceneBean bean) {

    }

    @Override
    public void onGetPlazaIndexSuccess(PlazaIndexBean bean) {

    }

    @Override
    public void onGetUgcdiyBaseInfoSuccess(UgcdiyBaseInfoBean baseInfoBean) {

    }

    @Override
    public void onGetDiyGeDanInfoSuccess(DiyGeDanInfoBean bean) {

    }

    @Override
    public void onGetArtistListInfoSuccess(ArtistGetListBean bean) {
        if(bean == null || bean.getArtist() == null){
            showToast("data error");
            return;
        }

        MyAdapter adapter = new MyAdapter();
        mArtistGetListBean.getArtist().addAll(bean.getArtist());
        rvArtistList.setAdapter(adapter);
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(ArtistArtistListActivity.this);
            View view = inflater.inflate(R.layout.item_artist_list_view, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            ArtistGetListBean.ArtistBean bean = mArtistGetListBean.getArtist().get(position);
            if(bean == null)
                return;

            holder.tvArtistName.setText(bean.getName());
            Glide.with(ArtistArtistListActivity.this).load(bean.getAvatar_middle()).into(holder.ivArtistPic);
        }

        @Override
        public int getItemCount() {
            return mArtistGetListBean.getArtist().size();
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivArtistPic;
        private TextView tvArtistName;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivArtistPic = (ImageView)itemView.findViewById(R.id.ivArtistPic);
            tvArtistName = (TextView)itemView.findViewById(R.id.tvArtistName);
        }
    }
}
