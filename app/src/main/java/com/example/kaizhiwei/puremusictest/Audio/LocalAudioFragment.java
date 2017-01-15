package com.example.kaizhiwei.puremusictest.Audio;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/12.
 */
@TargetApi(Build.VERSION_CODES.M)
public class LocalAudioFragment extends BaseFragment implements ViewPager.OnLongClickListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private TabLayout mTabLayout;
    private TabLayout.TabLayoutOnPageChangeListener mTVl;
    private ViewPager mViewPager;

    private List<String> mListTitleData;
    private List<View> mListViewData;

    private LocalBaseMediaLayout mAllSongFragement;
    private LocalBaseMediaLayout mSongFolderFragement;
    private LocalBaseMediaLayout mArtistFragement;
    private LocalBaseMediaLayout mAlbumFragement;

    //标题按钮
    private LinearLayout llSearch;
    private RelativeLayout rlTitle;
    private ImageView ivSearch;
    private ImageView ivScan;
    private ImageView ivSort;
    private EditText etSearchKey;
    private TextView tvCancel;
    private MyTextView mtvScanMedia;
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

    private LocalBaseMediaLayout.IFragmentInitListener mSubFragmentListener= new LocalBaseMediaLayout.IFragmentInitListener() {
        @Override
        public void onFragmentInitFinish(LinearLayout fragment) {

        }
    };

    public LocalAudioFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.activity_audio);

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tabLayout);
        mTVl = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        setTitle("本地音乐");

        mtvScanMedia = (MyTextView)rootView.findViewById(R.id.mtvScanMedia);
        mtvScanMedia.setOnClickListener(this);

        llSearch = (LinearLayout) rootView.findViewById(R.id.llSearch);
        rlTitle = (RelativeLayout) rootView.findViewById(R.id.rlTitle);
        ivSearch = (ImageView) rootView.findViewById(R.id.ivSearch);
        ivSearch.setOnClickListener(this);
        ivSearch.setVisibility(View.VISIBLE);
        ivScan = (ImageView) rootView.findViewById(R.id.ivScan);
        ivScan.setOnClickListener(this);
        ivScan.setVisibility(View.VISIBLE);
        ivSort = (ImageView) rootView.findViewById(R.id.ivSort);
        ivSort.setOnClickListener(this);
        ivSort.setVisibility(View.VISIBLE);
        etSearchKey = (EditText) rootView.findViewById(R.id.etSearchKey);
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
        tvCancel = (TextView) rootView.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(this);

        mListTitleData = new ArrayList<String>();
        mListTitleData.add("歌曲");
        mListTitleData.add("文件夹");
        mListTitleData.add("歌手");
        mListTitleData.add("专辑");

        mAllSongFragement = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        mAllSongFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, true, true, true);
        mSongFolderFragement = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        mSongFolderFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_FOLDER, false, false, true);
        mArtistFragement = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        mArtistFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ARTIST, false, false, true);
        mAlbumFragement = new LocalBaseMediaLayout(this.getActivity(), mSubFragmentListener);
        mAlbumFragement.setAdapterType(AudioListViewAdapter.ADAPTER_TYPE_ALBUM, false, false, true);

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
        return rootView;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAllSongFragement.initAdapterData();
        mSongFolderFragement.initAdapterData();
        mArtistFragement.initAdapterData();
        mAlbumFragement.initAdapterData();
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
        super.onClick(v);
    }
}
