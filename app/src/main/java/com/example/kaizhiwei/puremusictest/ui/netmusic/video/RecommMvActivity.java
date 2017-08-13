package com.example.kaizhiwei.puremusictest.ui.netmusic.video;

import android.support.v7.widget.GridLayoutManager;
import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Util.DeviceUtil;
import com.example.kaizhiwei.puremusictest.base.MyBaseActivity;
import com.example.kaizhiwei.puremusictest.bean.RecommMvBean;
import com.example.kaizhiwei.puremusictest.constant.PureMusicContant;
import com.example.kaizhiwei.puremusictest.contract.RecommMvContract;
import com.example.kaizhiwei.puremusictest.presenter.RecommMvPresenter;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewSpaceDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 17/8/5.
 */

public class RecommMvActivity extends MyBaseActivity implements RecommMvContract.View {
    @Bind(R.id.recyclerView)
    XRecyclerView recyclerView;
    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;

    private int startPage = 0;
    private int size = 10;
    private boolean needClear;
    private RecommMvPresenter mPresenter;
    private RecommMvBean recommMvBean = new RecommMvBean();
    private MvCommAdapter mAdapter;
    private List<MvCommAdapter.ItemData> mListDatas = new ArrayList<>();

    private String mParam = "ls4JwVxqCi%2F8IUzOqiohKJI3T25sTZPABP40fqvgRY5LPdMVqlz%2BI%2BO%2BumXaTrAc";
    private String mTs = "1501917966";
    private String mSign = "841fbcae80d388607bc86d3ea3d5f3e1";
    private int mId;
    private String mTitle;

    public static final String INTENT_PARAM = "INTENT_PARAM";
    public static final String INTENT_TS = "INTENT_TS";
    public static final String INTENT_SIGN = "INTENT_SIGN";
    public static final String INTENT_MID = "INTENT_MID";
    public static final String INTENT_TITLE = "INTENT_TITLE";

    @Override
    public int getLayoutId() {
        return R.layout.activity_recomm_mv;
    }

    @Override
    public void initPresenter() {
        mPresenter = new RecommMvPresenter(this);
    }

    @Override
    public void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setPullRefreshEnabled(true);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallPulse);

        int space = 2* DeviceUtil.getDensity(this);
        RecyclerViewSpaceDecoration spaceDecoration = new RecyclerViewSpaceDecoration(0, 0, space, 10*space);
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                startPage = 0;
                needClear = true;
                int offset = startPage*size > mListDatas.size() ? mListDatas.size() : startPage*size;
                if(mPresenter != null){
                    mPresenter.getRecommMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                            PureMusicContant.CHANNEL, 0, "baidu.ting.plaza.recommMV", "daily", 4, mId, size, offset, mParam
                            , mTs, mSign);
                }
            }

            @Override
            public void onLoadMore() {
                startPage++;
                int offset = startPage*size > mListDatas.size() ? mListDatas.size() : startPage*size;
                if(mPresenter != null){
                    mPresenter.getRecommMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                            PureMusicContant.CHANNEL, 0, "baidu.ting.plaza.recommMV", "daily", 4, mId, size, offset, mParam
                            , mTs, mSign);
                }
            }
        });

        mAdapter = new MvCommAdapter(this, mListDatas);
        recyclerView.setAdapter(mAdapter);

        commonTitle.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                finish();
            }

            @Override
            public void onRightBtnClicked() {

            }
        });
        commonTitle.setTitleViewInfo("", mTitle, "");
    }

    @Override
    public void initData() {
        mId = getIntent().getIntExtra(INTENT_MID, 0);
        mTitle = getIntent().getStringExtra(INTENT_TITLE);
        if(mPresenter != null){
            mPresenter.getRecommMv(PureMusicContant.DEVICE_TYPE, PureMusicContant.APP_VERSION,
                    PureMusicContant.CHANNEL, 0, "baidu.ting.plaza.recommMV", "daily", 4, mId, size, 0, mParam
                    , mTs, mSign);
        }

        RecommMvBean.ResultBean resultBean = new RecommMvBean.ResultBean();
        List<RecommMvBean.ResultBean.MvListBean> listBean = new ArrayList<>();
        resultBean.setMv_list(listBean);
        recommMvBean.setResult(resultBean);
    }

    @Override
    public void onError(String strErrMsg) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        showToast(strErrMsg);
    }

    @Override
    public void onGetRecommMvSuccess(RecommMvBean bean) {
        recyclerView.refreshComplete();
        recyclerView.loadMoreComplete();
        if(bean == null || bean.getResult() == null || bean.getResult().getMv_list() == null)
            return ;

        if(needClear){
            recommMvBean.getResult().getMv_list().clear();
        }
        needClear = false;
        mListDatas.clear();
        recommMvBean.getResult().getMv_list().addAll(bean.getResult().getMv_list());
        for(int i = 0;i < recommMvBean.getResult().getMv_list().size();i++){
            MvCommAdapter.ItemData itemData = new MvCommAdapter.ItemData();
            itemData.strMain = recommMvBean.getResult().getMv_list().get(i).getTitle();
            itemData.strSub = recommMvBean.getResult().getMv_list().get(i).getArtist();
            itemData.pic = recommMvBean.getResult().getMv_list().get(i).getThumbnail();
            itemData.key = recommMvBean.getResult().getMv_list().get(i).getMv_id();
            mListDatas.add(itemData);
        }
        mAdapter.notifyDataSetChanged();
    }


}
