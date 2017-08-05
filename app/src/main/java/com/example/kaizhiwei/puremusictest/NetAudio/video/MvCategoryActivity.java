package com.example.kaizhiwei.puremusictest.NetAudio.video;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.MvCategoryBean;
import com.example.kaizhiwei.puremusictest.bean.MvSearchBean;
import com.example.kaizhiwei.puremusictest.bean.PlayMvBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.MvInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.MvInfoPresenter;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewSpaceDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kaizhiwei on 17/7/31.
 */

public class MvCategoryActivity extends MyBaseActivity implements MvInfoContract.View, MvCatrgoryDialog.IMvCatrgoryDialogListener {
    private MvInfoPresenter mPresenter;

    @Bind(R.id.tvAll)
    TextView tvAll;
    @Bind(R.id.tvRecent)
    TextView tvRecent;
    @Bind(R.id.tvHot)
    TextView tvHot;
    @Bind(R.id.recyclerView)
    XRecyclerView recyclerView;
    private MvCategoryAdapter mAdapter;

    private int startPage = 1;
    private int pageSize = 20;
    private int order = 1;

    private MvCategoryBean mvCategoryBean;
    private MvSearchBean mvSearchBean = new MvSearchBean();
    private String mQueryKey;
    private boolean needClear = false;
    private MvCatrgoryDialog mMvCategoryDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mv_category;
    }

    @Override
    public void initPresenter() {
        mPresenter = new MvInfoPresenter(this);
    }

    @Override
    public void initView() {
        tvRecent.setSelected(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

        int space = 2*DeviceUtil.getDensity(this);
        RecyclerViewSpaceDecoration spaceDecoration = new RecyclerViewSpaceDecoration(0, 0, space, 10*space);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                startPage = 1;
                needClear = true;
                if(mPresenter != null){
                    mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                            PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                            PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
                }
            }

            @Override
            public void onLoadMore() {
                startPage++;
                if(mPresenter != null){
                    mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                            PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                            PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
                }
            }
        });

        mAdapter = new MvCategoryAdapter();
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        if(mPresenter != null){
            mPresenter.getMvCategory(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                    PureMusicContant.CHANNEL, 2, "baidu.ting.mv.getMVCategory", PureMusicContant.FORMAT_JSON);
        }

        MvSearchBean.ResultBean resultBean = new MvSearchBean.ResultBean();
        List<MvSearchBean.ResultBean.MvListBean> listBeanList = new ArrayList();
        resultBean.setMv_list(listBeanList);
        mvSearchBean.setResult(resultBean);
    }

    @OnClick({R.id.tvRecent, R.id.tvHot, R.id.tvAll})
    void onClicked(View view){
        if(view == tvRecent){
            if(tvRecent.isSelected())
                return;

            tvRecent.setSelected(true);
            tvHot.setSelected(false);
            needClear = true;
            startPage = 0;
            order = 1;
            if(mPresenter != null){
                mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                        PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                        PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
            }
        }
        else if(view == tvHot){
            if(tvHot.isSelected())
                return;

            tvRecent.setSelected(false);
            tvHot.setSelected(true);
            needClear = true;
            startPage = 0;
            order = 0;
            if(mPresenter != null){
                mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                        PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                        PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
            }
        }
        else if(view == tvAll){
            if(mMvCategoryDialog == null){
                MvCatrgoryDialog.Builder builder = new MvCatrgoryDialog.Builder(this);
                mMvCategoryDialog = (MvCatrgoryDialog) builder.create();
                mMvCategoryDialog.setListener(this);
            }
            mMvCategoryDialog.setTitle(getResources().getString(R.string.select_mv_category));
            mMvCategoryDialog.show();
            mMvCategoryDialog.setSelectValue(mQueryKey);
            mMvCategoryDialog.setData(mvCategoryBean.getResult());
        }
    }

    @Override
    public void onError(String strErrMsg) {
        recyclerView.loadMoreComplete();
        recyclerView.refreshComplete();
        showToast(strErrMsg);
    }

    @Override
    public void onGetMvInfoSuccess(PlayMvBean bean) {

    }

    @Override
    public void onGetMvCategorySuccess(MvCategoryBean bean) {
        if(bean == null || bean.getResult() == null)
            return;

        mvCategoryBean = bean;
        if(bean.getResult().size()> 0){
            mQueryKey = bean.getResult().get(0);
            tvAll.setText(mQueryKey);
            if(mPresenter != null){
                mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                        PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                        PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
            }
        }
    }

    @Override
    public void onSearchMvSuccess(MvSearchBean bean) {
        recyclerView.loadMoreComplete();
        recyclerView.refreshComplete();
        if(bean == null || bean.getResult() == null)
            return;

        if(needClear){
            mvSearchBean.getResult().getMv_list().clear();
        }
        needClear = false;
        mvSearchBean.getResult().getMv_list().addAll(bean.getResult().getMv_list());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, String value) {
        mQueryKey = value;
        needClear = true;
        startPage = 0;
        tvAll.setText(mQueryKey);
        if(mPresenter != null){
            mPresenter.getSearchMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                    PureMusicContant.CHANNEL, 2, "11,12", "baidu.ting.mv.searchMV",
                    PureMusicContant.FORMAT_JSON, order, startPage, pageSize, mQueryKey);
        }
    }

    private class MvCategoryAdapter extends RecyclerView.Adapter<MvCategoryViewHolder>{

        @Override
        public MvCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MvCategoryActivity.this).inflate(R.layout.fragment_net_audio_recommand_item, parent, false);
            return new MvCategoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MvCategoryViewHolder holder, int position) {
            final MvSearchBean.ResultBean.MvListBean mvInfo = mvSearchBean.getResult().getMv_list().get(position);
            holder.tvMain.setText(mvInfo.getTitle());
            holder.tvSub.setText(mvInfo.getArtist());
            holder.ivIcon.setMinimumHeight(100* DeviceUtil.getDensity(MvCategoryActivity.this));
            holder.ivIcon.setMaxHeight(100* DeviceUtil.getDensity(MvCategoryActivity.this));
            holder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MvCategoryActivity.this, PlayMvActivity.class);
                    intent.putExtra(PlayMvActivity.INTENT_MVID, mvInfo.getMv_id());
                    MvCategoryActivity.this.startActivity(intent);
                }
            });
            Glide.with(MvCategoryActivity.this).load(mvInfo.getThumbnail()).into(holder.ivIcon);
        }

        @Override
        public int getItemCount() {
            if(mvSearchBean == null || mvSearchBean.getResult() == null)
                return 0;

            return mvSearchBean.getResult().getMv_list().size();
        }
    }

    private class MvCategoryViewHolder extends RecyclerView.ViewHolder{
        public MaskImageView ivIcon;
        public TextView tvMain;
        public TextView tvSub;

        public MvCategoryViewHolder(View itemView) {
            super(itemView);
            ivIcon = (MaskImageView)itemView.findViewById(R.id.ivIcon);
            tvMain = (TextView)itemView.findViewById(R.id.tvMain);
            tvSub = (TextView)itemView.findViewById(R.id.tvSub);

            Typeface typeFace = Typeface.createFromAsset(MvCategoryActivity.this.getAssets(),"arial.ttf");
            tvMain.setTypeface(typeFace);
            tvSub.setTypeface(typeFace);
        }
    }
}
