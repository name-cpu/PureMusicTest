package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.example.kaizhiwei.puremusictest.CommonUI.SystemBarTintManager;
import com.example.kaizhiwei.puremusictest.MediaData.MediaDataBase;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by kaizhiwei on 16/11/12.
 */
@TargetApi(Build.VERSION_CODES.M)
public class AudioActivity extends Activity implements ViewPager.OnLongClickListener, ViewPager.OnPageChangeListener
        ,MediaLibrary.IMediaScanListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AudioListViewAdapter.IAudioListViewListener
        ,PlaybackService.Client.Callback, PlaybackService.Callback, AdapterView.OnItemLongClickListener {
    private TabLayout mTabLayout;
    private TabLayout.TabLayoutOnPageChangeListener mTVl;

    private ViewPager mViewPager;

    private List<String> mListTitleData;
    private List<View> mListViewData;

    private AudioListView mAllSongListView;
    private AudioListViewAdapter mAllSongAdapter;

    private AudioListView mSongFolderListView;
    private AudioListViewAdapter mSongFolderAdapter;

    private AudioListView mArtistListView;
    private AudioListViewAdapter mArtistAdapter;

    private AudioListView mAlbumListViewData;
    private AudioListViewAdapter mAlbumAdapter;
    private Handler mHandler = new Handler();
    private Vibrator vibrator;

    private PlaybackService.Client mClient = new PlaybackService.Client(this, this);
    private PlaybackService mService;
    MoreOperationDialog.Builder mMoreDialogbuilder = null;
    MoreOperationDialog mMoreDialog = null;

    //正在播放列表
    private NowPlayingLayout    mNowPlayingLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        initSystemBar();
        setContentView(R.layout.activity_audio);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        mTabLayout = (TabLayout) this.findViewById(R.id.tabLayout);
        mTVl = new TabLayout.TabLayoutOnPageChangeListener(mTabLayout);
        mViewPager = (ViewPager) this.findViewById(R.id.viewPager);

        mListTitleData = new ArrayList<String>();
        mListTitleData.add("歌曲");
        mListTitleData.add("文件夹");
        mListTitleData.add("歌手");
        mListTitleData.add("专辑");

        mAllSongListView = (AudioListView) this.findViewById(R.id.lvAllSong);
        mAllSongAdapter = new AudioListViewAdapter(this, AudioListViewAdapter.ADAPTER_TYPE_ALLSONG, true);
        mAllSongListView.setAdapter(mAllSongAdapter);
        mAllSongListView.setOnItemClickListener(this);
        mAllSongListView.setOnScrollListener(this);
        mAllSongListView.setOnItemLongClickListener(this);

        mSongFolderListView = (AudioListView) this.findViewById(R.id.lvSongFolder);
        mSongFolderAdapter = new AudioListViewAdapter(this, AudioListViewAdapter.ADAPTER_TYPE_FOLDER, false);
        mSongFolderListView.setAdapter(mSongFolderAdapter);
        mSongFolderListView.setOnItemClickListener(this);
        mSongFolderListView.setOnScrollListener(this);
        mSongFolderListView.setOnItemLongClickListener(this);

        mArtistListView = (AudioListView) this.findViewById(R.id.lvArtist);
        mArtistAdapter = new AudioListViewAdapter(this, AudioListViewAdapter.ADAPTER_TYPE_ARTIST, false);
        mArtistListView.setAdapter(mArtistAdapter);
        mArtistListView.setOnItemClickListener(this);
        mArtistListView.setOnScrollListener(this);
        mArtistListView.setOnItemLongClickListener(this);

        mAlbumListViewData = (AudioListView) this.findViewById(R.id.lvAlbum);
        mAlbumAdapter = new AudioListViewAdapter(this, AudioListViewAdapter.ADAPTER_TYPE_ALBUM, false);
        mAlbumListViewData.setAdapter(mAlbumAdapter);
        mAlbumListViewData.setOnItemClickListener(this);
        mAlbumListViewData.setOnScrollListener(this);
        mAlbumListViewData.setOnItemLongClickListener(this);

        mListViewData = new ArrayList<View>();
        mListViewData.add(mAllSongListView);
        mListViewData.add(mSongFolderListView);
        mListViewData.add(mArtistListView);
        mListViewData.add(mAlbumListViewData);

        mViewPager.setLongClickable(true);
        mViewPager.setOnLongClickListener(this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(mListViewData.size());
        AudioViewPagerAdapter adapter = new AudioViewPagerAdapter(mListViewData, mListTitleData);
        mViewPager.setAdapter(adapter);

        for(int i = 0;i < mListTitleData.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(mListTitleData.get(i)));
        }
        mTabLayout.setBackgroundResource(R.color.backgroundColor);
        mTabLayout.setTabTextColors(this.getResources().getColor(R.color.mainTextColor), this.getResources().getColor(R.color.tabSelectTextColor));
        int indicatorColor = this.getResources().getColor(R.color.tabSeperatorLineColor);
        mTabLayout.setSelectedTabIndicatorColor(indicatorColor);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mNowPlayingLayout = (NowPlayingLayout)this.findViewById(R.id.nowPlayingLayout);

        if(PreferenceConfig.getInstance().getLastFirstLaunch() == false){
            initAdapterData();
        }
    }

    private void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.tabSeperatorLineColor);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onResume(){
        super.onResume();
        MediaLibrary.getInstance().registerListener(this);
        mAllSongAdapter.registerListener(this);
        mSongFolderAdapter.registerListener(this);
        mArtistAdapter.registerListener(this);
        mAlbumAdapter.registerListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaLibrary.getInstance().unregisterListener(this);
        mAllSongAdapter.unregisterListener(this);
        mSongFolderAdapter.unregisterListener(this);
        mArtistAdapter.unregisterListener(this);
        mAlbumAdapter.unregisterListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    public void initAdapterData(){
        final WeakReference<MediaLibrary> mlibrary = new WeakReference<MediaLibrary>(MediaLibrary.getInstance());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                MediaLibrary library = mlibrary.get();
                if(library != null){
                    mAllSongAdapter.initData(library.getAllMediaEntrty());
                    mSongFolderAdapter.initData(library.getAllMediaEntrty());
                    mArtistAdapter.initData(library.getAllMediaEntrty());
                    mAlbumAdapter.initData(library.getAllMediaEntrty());
                }
            }
        });
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

    //MediaLibrary.IMediaScanListener
    @Override
    public void onScanStart() {

    }

    @Override
    public void onScaning(String fileInfo, float progress) {

    }

    @Override
    public void onScanFinish() {
        initAdapterData();
    }

    //AdapterView.OnItemClickListener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AudioListViewAdapter adapter = (AudioListViewAdapter)parent.getAdapter();
        int adapterType = adapter.getAdapterType();
        AudioListViewAdapter.AudioItemData itemData = adapter.getAudioItemData(position);
        if(itemData == null)
            return ;

        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG && itemData.mItemType == AudioListViewAdapter.AudioItemData.TYPE_MEDIA){
            mService.play(itemData.mListMedia, 0);
            mAllSongAdapter.setItemPlayState(position, true);
            mNowPlayingLayout.setPlayingMediaEntrty(itemData.mListMedia.get(0));
        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
                AudioListViewAdapter.AudioFolderItemData folderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;

                Intent intent = new Intent(AudioActivity.this, AudioFilterActivity.class);
                intent.putExtra(AudioFilterActivity.FILTER_NAME, folderItemData.mFolderPath);
                intent.putExtra(AudioFilterActivity.FILTER_TYPE, adapterType);
                intent.putExtra(AudioFilterActivity.TITLE_NAME, folderItemData.mFolderName);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent);
            }
        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST || adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){
            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
                AudioListViewAdapter.AudioArtistAlbumItemData artistItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;

                Intent intent = new Intent(AudioActivity.this, AudioFilterActivity.class);
                intent.putExtra(AudioFilterActivity.FILTER_NAME, artistItemData.mArtistAlbumName);
                intent.putExtra(AudioFilterActivity.FILTER_TYPE, adapterType);
                intent.putExtra(AudioFilterActivity.TITLE_NAME, artistItemData.mArtistAlbumName);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        vibrator.vibrate(50);
        onMoreBtnClick((AudioListViewAdapter) parent.getAdapter(), position);
        return true;
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
    }

    //AudioListViewAdapter.IAudioListViewListener
    @Override
    public void onMoreBtnClick(AudioListViewAdapter adapter, int position) {
        if(adapter == null)
            return ;

        if(mMoreDialogbuilder == null){
            mMoreDialogbuilder = new MoreOperationDialog.Builder(this);
        }

        if(mMoreDialog == null){
            mMoreDialog = mMoreDialogbuilder.create();
        }

        List<Integer> list = new ArrayList<>();
        int adapterType = adapter.getAdapterType();
        AudioListViewAdapter.AudioItemData itemData = adapter.getAudioItemData(position);
        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
            AudioListViewAdapter.AudioSongItemData songItemData = null;
            if(itemData instanceof AudioListViewAdapter.AudioSongItemData){
                songItemData = (AudioListViewAdapter.AudioSongItemData)itemData;
            }

            if(songItemData != null){
                mMoreDialog.setTitle(songItemData.mMainTitle);
                list.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
                list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
                list.add(MoreOperationDialog.MORE_BELL_NORMAL);
                list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
                list.add(MoreOperationDialog.MORE_ADD_NORMA);
                list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
            }
        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            AudioListViewAdapter.AudioFolderItemData folderItemData = null;
            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
                folderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
            }

            if(folderItemData != null){
                mMoreDialog.setTitle(folderItemData.mFolderName);
                list.add(MoreOperationDialog.MORE_PLAY_NORMAL);
                list.add(MoreOperationDialog.MORE_ADD_NORMA);
                list.add(MoreOperationDialog.MORE_HIDE_NORMAL);
                list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
            }
        }
        else{
            AudioListViewAdapter.AudioArtistAlbumItemData artistAlbumItemData = null;
            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
                artistAlbumItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
            }

            if(artistAlbumItemData != null){
                mMoreDialog.setTitle(artistAlbumItemData.mArtistAlbumName);
                list.add(MoreOperationDialog.MORE_PLAY_NORMAL);
                list.add(MoreOperationDialog.MORE_ADD_NORMA);
                list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
            }
        }

        mMoreDialog.setMoreLVData(adapterType, itemData);
        mMoreDialog.setMoreOperData(list);
        mMoreDialog.setCancelable(true);
        mMoreDialog.show();
    }

    //PlaybackService.Client.Callback
    @Override
    public void onConnected(PlaybackService service) {
        mService = service;
        mService.addCallback(this);
    }

    @Override
    public void onDisconnected() {
        mService = null;
    }

    //PlaybackService.Callback
    @Override
    public void update() {

    }

    @Override
    public void updateProgress() {
        if(mService == null || mNowPlayingLayout == null)
            return ;

        int iTime = (int)mService.getTIme();
        int iLengtht = (int)mService.getLength();
        //mNowPlayingLayout.updatePlayProgress(iTime, iLengtht);
    }

    @Override
    public void onMediaEvent(Media.Event event) {
        if(mService == null || event == null)
            return;

        //mNowPlayingLayout.setPlayPauseState(mService.isPlaying());
    }

    @Override
    public void onMediaPlayerEvent(MediaPlayer.Event event) {

    }
}
