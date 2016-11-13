package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kaizhiwei on 16/11/12.
 */
@TargetApi(Build.VERSION_CODES.M)
public class AudioActivity extends Activity implements ViewPager.OnLongClickListener, ViewPager.OnPageChangeListener, TabLayout.OnTabSelectedListener
        ,MediaLibrary.IMediaScanListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AudioListViewAdapter.IAudioListViewListener{
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SwipeRefreshLayout mSRLayout;

    private List<String> mListTitle;
    private List<View> mListView;
    private AudioListView mAllSongListView;
    private AudioListViewAdapter mAllSongAdapter;

    private AudioListView mArtistListView;
    private AudioListViewAdapter mArtistAdapter;

    private AudioListView mAlbumListView;
    private AudioListViewAdapter mAlbumAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        mTabLayout = (TabLayout) this.findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) this.findViewById(R.id.viewPager);
        mSRLayout = (SwipeRefreshLayout) this.findViewById(R.id.sfLayout);
        mSRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
           public void onRefresh() {
                MediaLibrary.getInstance().startScan();
            }
        });

        mListTitle = new ArrayList<String>();
        mListTitle.add("歌曲");
        mListTitle.add("艺术家");
        mListTitle.add("专辑");

        mAllSongListView = new AudioListView(this);
        mAllSongAdapter = new AudioListViewAdapter(MediaLibrary.getInstance().getAllMediaEntrty(), this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG);
        mAllSongListView.setAdapter(mAllSongAdapter);

        mArtistListView = new AudioListView(this);
        mArtistAdapter = new AudioListViewAdapter(MediaLibrary.getInstance().getAllMediaEntrty(), this, AudioListViewAdapter.ADAPTER_TYPE_ARTIST);
        mArtistListView.setAdapter(mArtistAdapter);

        mAlbumListView = new AudioListView(this);
        mAlbumAdapter = new AudioListViewAdapter(MediaLibrary.getInstance().getAllMediaEntrty(), this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG);
        mAlbumListView.setAdapter(mAlbumAdapter);

        mListView = new ArrayList<View>();
        mListView.add(mAllSongListView);
        mListView.add(mArtistListView);
        mListView.add(mAlbumListView);

        mViewPager.setLongClickable(true);
        mViewPager.setOnLongClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(mListView.size());
        AudioViewPagerAdapter adapter = new AudioViewPagerAdapter(mListView, mListTitle);
        mViewPager.setAdapter(adapter);

        mTabLayout.setTabsFromPagerAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(this);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MediaLibrary.getInstance().registerListener(this);
        mAllSongAdapter.registerListener(this);
        mArtistAdapter.registerListener(this);
        mAlbumAdapter.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaLibrary.getInstance().unregisterListener(this);
        mAllSongAdapter.unregisterListener(this);
        mArtistAdapter.unregisterListener(this);
        mAlbumAdapter.unregisterListener(this);
    }

    //TabLayout.OnTabSelectedListener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //ViewPager.OnLongClickListener
    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    //ViewPager.OnPageChangeListener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //MediaLibrary.IMediaScanListener
    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(String fileInfo, float progress) {

    }

    @Override
    public void onScanFinish() {
        mSRLayout.setRefreshing(false);
        final WeakReference<MediaLibrary> mlibrary = new WeakReference<MediaLibrary>(MediaLibrary.getInstance());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mlibrary.get() != null){
                    mAllSongAdapter = new AudioListViewAdapter(mlibrary.get().getAllMediaEntrty(), AudioActivity.this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG);
                    mAllSongListView.setAdapter(mAllSongAdapter);

                    mArtistAdapter = new AudioListViewAdapter(mlibrary.get().getAllMediaEntrty(), AudioActivity.this, AudioListViewAdapter.ADAPTER_TYPE_ARTIST);
                    mAllSongListView.setAdapter(mArtistAdapter);

                    mAlbumAdapter = new AudioListViewAdapter(mlibrary.get().getAllMediaEntrty(), AudioActivity.this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG);
                    mAllSongListView.setAdapter(mAlbumAdapter);
                }
            }
        });
    }

    //AdapterView.OnItemClickListener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AudioListViewAdapter adapter = (AudioListViewAdapter)parent.getAdapter();
        int adapterType = adapter.getAdapterType();
        AudioListViewAdapter.AudioItemData itemData = adapter.getAudioItemData(position);
        if(itemData == null)
            return ;

        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){

        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST){

        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){

        }
    }

    //AdapterView.OnScrollChangeListener
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        boolean enable = false;
        if(view != null && view.getChildCount() > 0){
            // check if the first item of the list is visible
            boolean firstItemVisible = view.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = view.getChildAt(0).getTop() == 0;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
        }
        mSRLayout.setEnabled(enable);
    }

    //AudioListViewAdapter.IAudioListViewListener
    @Override
    public void onItemClick(AudioListViewAdapter adapter, int position) {

    }
}
