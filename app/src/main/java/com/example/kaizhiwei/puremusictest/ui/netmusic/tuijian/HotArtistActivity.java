package com.example.kaizhiwei.puremusictest.ui.netmusic.tuijian;

import android.support.v7.widget.GridLayoutManager;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetListBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.ArtistGetArtistListInfoContract;
import com.example.kaizhiwei.puremusictest.presenter.ArtistGetArtistListInfoPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewSpaceDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/7/9.
 */

public class HotArtistActivity extends MyBaseActivity implements ArtistGetArtistListInfoContract.View {
    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;
    @Bind(R.id.rvArtistList)
    XRecyclerView rvArtistList;
    private ArtistGetArtistListInfoContract.Presenter mPresenter;
    private ArtistGetListBean mArtistGetListBean = new ArtistGetListBean();
    private HotArtistAdapter mAdapter;
    private int mCurPage;

    @Override
    public int getLayoutId() {
        return R.layout.activity_hot_artist;
    }

    @Override
    public void initPresenter() {
        mPresenter = new ArtistGetArtistListInfoPresenter(this);
        loadMoreData();
    }

    private void loadMoreData() {
        int offset = 0;
        int limit = 0;
        if(mCurPage == 0){
            offset = 0;
            limit = 48;
        }
        else{
            offset = (mCurPage + 1)*30;
            limit = 30;
        }
        mCurPage++;
        if(mPresenter != null){
            mPresenter.getArtistListInfo(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION, PureMusicContant.CHANNEL, "2",
                    "baidu.ting.artist.getList", PureMusicContant.FORMAT_JSON, offset + "", limit+"", "1", "0", "0");
        }
    }

    @Override
    public void initView() {
        commonTitle.setTitleVisible(false);
        commonTitle.setRightBtnVisible(false);
        commonTitle.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                finish();
            }

            @Override
            public void onRightBtnClicked() {
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rvArtistList.setLayoutManager(gridLayoutManager);
        rvArtistList.setPullRefreshEnabled(false);
        rvArtistList.setLoadingMoreEnabled(true);
        rvArtistList.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);
        rvArtistList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadMoreData();
            }
        });
        mAdapter = new HotArtistAdapter(this, mArtistGetListBean);
        rvArtistList.setAdapter(mAdapter);
        rvArtistList.addItemDecoration(new RecyclerViewSpaceDecoration(0, 0, 0, 5* DeviceUtil.getDensity(this)));
    }

    @Override
    public void initData() {
        List<ArtistGetListBean.ArtistBean> list = new ArrayList<>();
        mArtistGetListBean.setArtist(list);
    }

    @Override
    public void onError(String strErrMsg) {
        rvArtistList.loadMoreComplete();
        showToast(strErrMsg);
    }

    @Override
    public void onGetArtistListInfoSuccess(ArtistGetListBean bean) {
        rvArtistList.loadMoreComplete();
        if(bean == null || bean.getArtist() == null){
            showToast("data error");
            return;
        }

        mArtistGetListBean.getArtist().addAll(bean.getArtist());
        mAdapter.notifyDataSetChanged();
    }
}
