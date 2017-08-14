package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.CommonUI.BaseFragment;
import com.example.kaizhiwei.puremusictest.CommonUI.MyTextView;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.ScanMedia.ScanMediaActivity;
import com.example.kaizhiwei.puremusictest.base.MyBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by kaizhiwei on 16/11/12.
 */
@TargetApi(Build.VERSION_CODES.M)
public class LocalAudioFragment extends MyBaseFragment implements ViewPager.OnLongClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    @Bind(R.id.tabLayout)
    TabLayout mTabLayout;

    private TabLayout.TabLayoutOnPageChangeListener mTVl;

    @Bind(R.id.viewPager)
    ViewPager mViewPager;

    private List<String> mListTitleData;
    private List<View> mListViewData;

    private LocalBaseMediaLayout mAllSongFragement;
    private LocalBaseMediaLayout mSongFolderFragement;
    private LocalBaseMediaLayout mArtistFragement;
    private LocalBaseMediaLayout mAlbumFragement;

    @Bind(R.id.tvTitle)
    TextView tvTitle;

    @Bind(R.id.tvRight)
    TextView tvRight;

    @Bind(R.id.llSearch)
    LinearLayout llSearch;

    @Bind(R.id.rlTitle)
    RelativeLayout rlTitle;

    @Bind(R.id.ivSearch)
    ImageView ivSearch;

    @Bind(R.id.ivScan)
    ImageView ivScan;

    @Bind(R.id.ivSort)
    ImageView ivSort;

    @Bind(R.id.etSearchKey)
    EditText etSearchKey;

    @Bind(R.id.tvCancel)
    TextView tvCancel;

    @Bind(R.id.mtvScanMedia)
    MyTextView mtvScanMedia;

    private TextWatcher tvSearchTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int curIndex = mViewPager.getCurrentItem();
            switch (curIndex){
                case 0:
                    mAllSongFragement.setSearchKey(s.toString());
                    break;
                case 1:
                    mSongFolderFragement.setSearchKey(s.toString());
                    break;
                case 2:
                    mArtistFragement.setSearchKey(s.toString());
                    break;
                case 3:
                    mAlbumFragement.setSearchKey(s.toString());
                    break;
            }
        }
    };

    private LocalBaseMediaLayout.ILocalBaseListener mSubFragmentListener= new LocalBaseMediaLayout.ILocalBaseListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }

        @Override
        public void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj) {

        }
    };

    public LocalAudioFragment(){

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_audio;
    }

    @Override
    protected void initView() {
        mTVl = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);

        mtvScanMedia.setOnClickListener(this);
        tvTitle.setText("本地音乐");
        tvRight.setVisibility(View.GONE);
        ivSearch.setOnClickListener(this);
        ivSearch.setVisibility(View.VISIBLE);
        ivScan.setOnClickListener(this);
        ivScan.setVisibility(View.VISIBLE);
        ivSort.setOnClickListener(this);
        ivSort.setVisibility(View.VISIBLE);
        etSearchKey.addTextChangedListener(tvSearchTextWatcher);
        etSearchKey.setFocusable(true);
        etSearchKey.setFocusableInTouchMode(true);
        etSearchKey.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                else{
                    InputMethodManager imm = (InputMethodManager) etSearchKey.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                }
            }
        });
        tvCancel.setOnClickListener(this);

        mListTitleData = new ArrayList<>();
        mListTitleData.add("歌曲");
        mListTitleData.add("文件夹");
        mListTitleData.add("歌手");
        mListTitleData.add("专辑");

        mAllSongFragement = new LocalBaseMediaLayout(this.getActivity());
        //mAllSongFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, true, true, true);
        mAllSongFragement.setBaseMediaListener(mSubFragmentListener);
        mAllSongFragement.setType(LocalBaseMediaLayout.LayoutType.ALLSONG);

        mSongFolderFragement = new LocalBaseMediaLayout(this.getActivity());
        mSongFolderFragement.setBaseMediaListener(mSubFragmentListener);
        //mSongFolderFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_FOLDER, false, false, true);
        mSongFolderFragement.setType(LocalBaseMediaLayout.LayoutType.FOLDER);

        mArtistFragement = new LocalBaseMediaLayout(this.getActivity());
        mArtistFragement.setBaseMediaListener(mSubFragmentListener);
        mArtistFragement.setType(LocalBaseMediaLayout.LayoutType.ARTIST);

        mAlbumFragement = new LocalBaseMediaLayout(this.getActivity());
        mAlbumFragement.setBaseMediaListener(mSubFragmentListener);
        mAlbumFragement.setType(LocalBaseMediaLayout.LayoutType.ALBUM);

        mListViewData = new ArrayList<>();
        mListViewData.add(mAllSongFragement);
        mListViewData.add(mSongFolderFragement);
        mListViewData.add(mArtistFragement);
        mListViewData.add(mAlbumFragement);

        mViewPager.setLongClickable(true);
        mViewPager.setOnLongClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(mListViewData.size());

        AudioViewPagerAdapter adapter = new AudioViewPagerAdapter(mListViewData, mListTitleData);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);

        mTabLayout.setTabTextColors(this.getResources().getColor(R.color.mainTextColor), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        mTabLayout.setSelectedTabIndicatorColor(indicatorColor);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {

    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    public void onDestory(){
        super.onDestroy();
        mAllSongFragement.onDestory();
        mSongFolderFragement.onDestory();
        mArtistFragement.onDestory();
        mAlbumFragement.onDestory();
    }

    public void onResume(){
        super.onResume();
        mAllSongFragement.onResume();
        mSongFolderFragement.onResume();
        mArtistFragement.onResume();
        mAlbumFragement.onResume();

        mAllSongFragement.initAdapterData();
        mSongFolderFragement.initAdapterData();
        mArtistFragement.initAdapterData();
        mAlbumFragement.initAdapterData();
    }

    public void onPause() {
        super.onPause();
        mAllSongFragement.onPause();
        mSongFolderFragement.onPause();
        mArtistFragement.onPause();
        mAlbumFragement.onPause();
    }

    public void onStart() {
        super.onStart();
        mAllSongFragement.onStart();
        mSongFolderFragement.onStart();
        mArtistFragement.onStart();
        mAlbumFragement.onStart();
    }

    public void onStop() {
        super.onStop();
        mAllSongFragement.onStop();
        mSongFolderFragement.onStop();
        mArtistFragement.onStop();
        mAlbumFragement.onStop();
    }

    public void onDetach() {
       super.onDetach();
    }

    //ViewPager.OnLongClickListener
    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    //ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mTVl.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mViewPager.setCurrentItem(position);
        mTVl.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mTVl.onPageScrollStateChanged(state);
    }

    @Override
    public void onClick(View v) {
        if(v == ivSearch){
            rlTitle.setVisibility(View.GONE);
            llSearch.setVisibility(View.VISIBLE);
            etSearchKey.requestFocus();
        }
        else if(v == ivScan || v == mtvScanMedia){
            Intent intent = new Intent(LocalAudioFragment.this.getActivity(), ScanMediaActivity.class);
            startActivity(intent);
        }
        else if(v == ivSort){

        }
        else if(v == tvCancel){
            etSearchKey.setText("");
            rlTitle.setVisibility(View.VISIBLE);
            llSearch.setVisibility(View.GONE);
            mAllSongFragement.setSearchKey("");
        }
    }
}
