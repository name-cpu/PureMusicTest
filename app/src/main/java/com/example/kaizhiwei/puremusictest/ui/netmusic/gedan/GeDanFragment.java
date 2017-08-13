package com.example.kaizhiwei.puremusictest.ui.netmusic.gedan;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.GeDanListContract;
import com.example.kaizhiwei.puremusictest.presenter.GeDanListPresenter;
import com.example.kaizhiwei.puremusictest.widget.HSViewRecyclerViewScrollListener;
import com.example.kaizhiwei.puremusictest.widget.MaskImageView;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewSpaceDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public class GeDanFragment extends MyBaseFragment implements GeDanListContract.View{
    @Bind(R.id.rvSongList)
    XRecyclerView rvSongList;
    @Bind(R.id.llTitle)
    LinearLayout llTitle;
    private int pageSize =12;
    private int startPage =1;

    private GeDanListBean mGeDanListBean = new GeDanListBean();
    private GeDanAdapter mAdapetr;
    private GeDanListPresenter mPresenter;
    private HSViewRecyclerViewScrollListener mScrollListener = new HSViewRecyclerViewScrollListener(){

        @Override
        public void onShowControlView() {
            llTitle.bringToFront();
            float startY = llTitle.getY();
            float endY = llTitle.getY() + llTitle.getHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(startY, endY);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    llTitle.setY((float)animation.getAnimatedValue());
                }
            });
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }

        @Override
        public void onHideControlView() {
            float startY = llTitle.getY();
            float endY = llTitle.getY() - llTitle.getHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(startY, endY);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    llTitle.setY((float)animation.getAnimatedValue());
                }
            });
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setDuration(400);
            valueAnimator.start();
        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_gedan;
    }

    @Override
    protected void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        rvSongList.setLayoutManager(gridLayoutManager);
        rvSongList.setPullRefreshEnabled(false);
        rvSongList.setLoadingMoreEnabled(true);
        rvSongList.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

        int space = 1* DeviceUtil.getDensity(this.getActivity());
        RecyclerViewSpaceDecoration spaceDecoration = new RecyclerViewSpaceDecoration(0, 0, space, 5*space);
        spaceDecoration.setFirstItemTopSpcace(40*DeviceUtil.getDensity(this.getActivity()));
        rvSongList.addItemDecoration(spaceDecoration);
        rvSongList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                startPage++;
                mPresenter.getGeDanList(PureMusicContant.FORMAT_JSON, PureMusicContant.DEVICE_TYPE, "baidu.ting.diy.gedan", pageSize, startPage);
            }
        });
        mScrollListener.setmDistanceSlop(100*DeviceUtil.getDensity(this.getActivity()));
        rvSongList.setOnScrollListener(mScrollListener);

        mAdapetr = new GeDanAdapter();
        rvSongList.setAdapter(mAdapetr);
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        if(isVisible && mPresenter != null){
            mPresenter.getGeDanList(PureMusicContant.FORMAT_JSON, PureMusicContant.DEVICE_TYPE, "baidu.ting.diy.gedan", pageSize, startPage);
        }
        isFirst = true;
    }

    @Override
    protected void initData() {
        mPresenter = new GeDanListPresenter(this);
    }

    @Override
    public void onError(String strErrMsg) {
        showToast(strErrMsg);
        rvSongList.loadMoreComplete();
    }

    @Override
    public void onGetGeDanListSuccess(GeDanListBean bean) {
        rvSongList.loadMoreComplete();
        if(bean == null || bean.getContent() == null)
            return;

        List<GeDanListBean.SongListInfo> listInfos = mGeDanListBean.getContent();
        if(listInfos == null){
            listInfos = new ArrayList<>();
            mGeDanListBean.setContent(listInfos);
        }
        listInfos.addAll(bean.getContent());
        mAdapetr.notifyDataSetChanged();
    }

    private class GeDanAdapter extends RecyclerView.Adapter<GeDanViewHolder>{

        @Override
        public GeDanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(GeDanFragment.this.getActivity()).inflate(R.layout.item_gedan, parent, false);
            return new GeDanViewHolder(view);
        }

        @Override
        public void onBindViewHolder(GeDanViewHolder holder, int position) {
            final GeDanListBean.SongListInfo songListInfo = mGeDanListBean.getContent().get(position);
            holder.tvListener.setText(songListInfo.getListenum());
            holder.tvGeDanMain.setText(songListInfo.getTitle());
            holder.tvGeDanSub.setText(songListInfo.getDesc());
            holder.ivGeDan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GeDanFragment.this.getActivity(), GeDanSongListActivity.class);
                    intent.putExtra(GeDanSongListActivity.INTENT_GEDANINFO, songListInfo);
                    GeDanFragment.this.getActivity().startActivity(intent);
                }
            });
            Glide.with(GeDanFragment.this.getActivity()).load(songListInfo.getPic_300()).into(holder.ivGeDan);
        }

        @Override
        public int getItemCount() {
            if(mGeDanListBean == null || mGeDanListBean.getContent() == null)
                return 0;

            return mGeDanListBean.getContent().size();
        }
    }

    private class GeDanViewHolder extends RecyclerView.ViewHolder{
        public MaskImageView ivGeDan;
        public TextView tvListener;
        public ImageView ivGeDanOper;
        public TextView tvGeDanMain;
        public TextView tvGeDanSub;

        public GeDanViewHolder(View itemView) {
            super(itemView);
            ivGeDan = (MaskImageView)itemView.findViewById(R.id.ivGeDan);
            tvListener = (TextView)itemView.findViewById(R.id.tvListener);
            ivGeDanOper = (ImageView)itemView.findViewById(R.id.ivGeDanOper);
            tvGeDanMain = (TextView)itemView.findViewById(R.id.tvGeDanMain);
            tvGeDanSub = (TextView)itemView.findViewById(R.id.tvGeDanSub);

            Typeface typeFace = Typeface.createFromAsset(GeDanFragment.this.getActivity().getAssets(),"arial.ttf");
            tvListener.setTypeface(typeFace);
            tvGeDanMain.setTypeface(typeFace);
            tvGeDanSub.setTypeface(typeFace);
        }
    }
}
