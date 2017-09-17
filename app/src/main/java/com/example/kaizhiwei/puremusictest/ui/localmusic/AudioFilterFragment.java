package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.kaizhiwei.puremusictest.CommonUI.CommonTitleView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.presenter.LocalMusicPresenter;
import com.example.kaizhiwei.puremusictest.ui.home.HomeActivity;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kaizhiwei on 16/11/21.
 */
public class AudioFilterFragment extends MyBaseFragment implements LocalMusicContract.View {
    @Bind(R.id.lbmLayout)
    LocalBaseMediaLayout lbmLayout;
    @Bind(R.id.llMain)
    LinearLayout llMain;
    @Bind(R.id.commonTitle)
    CommonTitleView commonTitle;

    private int mFilterType;
    private String mFilterData;
    private String mTitle;

    public static final int FILTER_BY_FOLDER = 1;
    public static final int FILTER_BY_ARTIST = 2;
    public static final int FILTER_BY_ALBUM = 3;

    private LocalMusicPresenter musicPresenter = new LocalMusicPresenter(this);

    private LocalBaseMediaLayout.ILocalBaseListener mSubFragmentListener = new LocalBaseMediaLayout.ILocalBaseListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }

        @Override
        public void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj) {

        }
    };

    @Override
    protected int getLayoutResource() {
        return R.layout.activty_audio_filter;
    }

    @Override
    protected void initView() {
        lbmLayout.setBaseMediaListener(mSubFragmentListener);
        lbmLayout.setType(LocalBaseMediaLayout.LayoutType.ALLSONG);
        commonTitle.setTitleViewInfo(mTitle, "", "");
        commonTitle.setTitleViewListener(new CommonTitleView.onTitleClickListener() {
            @Override
            public void onLeftBtnClicked() {
                HomeActivity.getInstance().popStackFragment();
            }

            @Override
            public void onRightBtnClicked() {

            }
        });
    }

    @Override
    protected void initData() {
        if (mFilterType == FILTER_BY_FOLDER) {
            musicPresenter.queryMusicInfosByFolder(mFilterData);
        } else if (mFilterType == FILTER_BY_ARTIST) {
            musicPresenter.queryMusicInfosByArist(mFilterData);
        } else if (mFilterType == FILTER_BY_ALBUM) {
            musicPresenter.queryMuisicInfosByAlbum(mFilterData);
        }
    }

    public void setFilterType(int type) {
        mFilterType = type;
    }

    public void setFilterData(String data) {
        mFilterData = data;
    }

    public void setTitle(String string){
        mTitle = string;
    }

    public void onDestory() {
        super.onDestroy();
        lbmLayout.onDestory();
    }

    public void onResume() {
        super.onResume();
        lbmLayout.onResume();
    }

    public void onPause() {
        super.onPause();
        lbmLayout.onPause();
    }

    public void onStart() {
        super.onStart();
        lbmLayout.onStart();
    }

    public void onStop() {
        super.onStop();
        lbmLayout.onStop();
    }

    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetAllMusicInfos(List<MusicInfoDao> list) {

    }

    @Override
    public void onGetMusicInfosByFolder(Map<String, List<MusicInfoDao>> mapRet) {

    }

    @Override
    public void onGetMusicInfosByArtist(Map<String, List<MusicInfoDao>> mapRet) {

    }

    @Override
    public void onGetMusicInfosByAlbum(Map<String, List<MusicInfoDao>> mapRet) {

    }

    @Override
    public void onQueryMusicInfosByFolder(List<MusicInfoDao> list) {
        lbmLayout.initAdaterData(list);
    }

    @Override
    public void onQueryMusicInfosByName(List<MusicInfoDao> list) {
        lbmLayout.initAdaterData(list);
    }

    @Override
    public void onQueryMusicInfosByArist(List<MusicInfoDao> list) {
        lbmLayout.initAdaterData(list);
    }

    @Override
    public void onQueryMuisicInfosByAlbum(List<MusicInfoDao> list) {
        lbmLayout.initAdaterData(list);
    }
}
