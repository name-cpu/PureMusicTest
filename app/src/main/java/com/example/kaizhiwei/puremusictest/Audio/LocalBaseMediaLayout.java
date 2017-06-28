package com.example.kaizhiwei.puremusictest.Audio;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.kaizhiwei.puremusictest.CommonUI.AndroidShare;
import com.example.kaizhiwei.puremusictest.CommonUI.NestedListView;
import com.example.kaizhiwei.puremusictest.HomePage.HomeActivity;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.Service.PlaybackService;
import com.example.kaizhiwei.puremusictest.Util.BusinessCode;
import com.example.kaizhiwei.puremusictest.base.BaseHandler;

import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/1/14.
 */
public class LocalBaseMediaLayout extends LinearLayout implements MediaLibrary.IMediaScanListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, AudioListViewAdapter.IAudioListViewListener
        ,PlaybackService.Client.Callback, PlaybackService.Callback, AdapterView.OnItemLongClickListener, MoreOperationDialog.IMoreOperationDialogListener,
        View.OnClickListener {
    private Context mContext;
    private NestedListView mAllSongListView;
    private AudioListViewAdapter mAllSongAdapter;

    private Handler mHandler = new Handler();
    private Vibrator vibrator;

    private PlaybackService.Client mClient = null;
    private PlaybackService mService;
    private MoreOperationDialog.Builder mMoreDialogbuilder = null;
    private MoreOperationDialog mMoreDialog = null;
    private List<Integer> mListMoreOperData;

    //加载控件
    private RelativeLayout rlLoading;
    private ProgressBar view_loading;
    private Handler loadingHandler = new Handler();
    private ILocalBaseListener mListener;

    //删除文件对话框
    private AlertDialogDeleteOne mAlertDialogDeleteOne;
    private AlertDialogDeleteOne.IOnAlertDialogDeleteListener mAlertDialogDeleteListener = new AlertDialogDeleteOne.IOnAlertDialogDeleteListener() {
        @Override
        public void OnDeleteClick(AlertDialogDeleteOne dialog, boolean isDeleteFile) {
            if(dialog == null)
                return;

            List<MediaEntity> listMediaEntity = dialog.getMediaEntityData();
            if(listMediaEntity == null || listMediaEntity.size() == 0)
                return;

            handleDeleteMedia(listMediaEntity, isDeleteFile);
        }
    };

    //隐藏对话框监听
    private AlertDialogHide.IAlertDialogHideListener mAlertDialogHideListener = new AlertDialogHide.IAlertDialogHideListener(){

        @Override
        public void onAlterDialogHideOk(String strFolderPath) {
            initAdapterData();
            List<String> list = PreferenceConfig.getInstance().getScanFilterByFolderName();
            if(!list.contains(strFolderPath))
                list.add(strFolderPath);
            PreferenceConfig.getInstance().setScanFilterByFolderName(list);
        }
    };

    public interface ILocalBaseListener{
        void onFragmentInitFinish(LinearLayout fragment);
        void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj);
    }

    public LocalBaseMediaLayout(Context context) {
        super(context, null);
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(layoutInflater);
    }

    public LocalBaseMediaLayout(Context context, ILocalBaseListener listener) {
        super(context, null);
        mListener = listener;
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(layoutInflater);
    }

    public LocalBaseMediaLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(layoutInflater);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LocalBaseMediaLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(layoutInflater);
    }

    public void setBaseMediaListener(ILocalBaseListener listener){
        mListener = listener;
    }

    public View initView(LayoutInflater inflater) {
        View rootView = inflater.inflate(R.layout.fragment_all_media, null);
        mClient = new PlaybackService.Client(mContext, this);
        vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        mAllSongListView = (NestedListView) rootView.findViewById(R.id.lvAllSong);
        mAllSongListView.setOnItemClickListener(this);
        mAllSongListView.setOnScrollListener(this);
        mAllSongListView.setOnItemLongClickListener(this);
        mAllSongListView.setVisibility(View.GONE);
        rlLoading = (RelativeLayout)rootView.findViewById(R.id.rlLoading);
        rlLoading.setVisibility(View.VISIBLE);
        view_loading = (ProgressBar)rootView.findViewById(R.id.view_loading);
        if(mListener != null){
            mListener.onFragmentInitFinish(this);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.addView(rootView, params);
        return rootView;
    }

    public void onDestory(){
        if(mMoreDialog != null){
            mMoreDialog.unregisterListener(this);
        }
    }

    public void onResume(){
        MediaLibrary.getInstance().registerListener(this);
        mAllSongAdapter.registerListener(this);
    }

    public void onPause() {
        MediaLibrary.getInstance().unregisterListener(this);
        mAllSongAdapter.unregisterListener(this);
    }

    public void onStart() {
        mClient.connect();
    }

    public void onStop() {
        mClient.disconnect();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
//        if (keyCode == event.KEYCODE_BACK) {
//            FragmentManager fragmentManager = mContext.getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.hide(this);
//            fragmentTransaction.commit();
//            return true;
//        }

        return false;
    }

    public void setAdapterType(int adapterType, boolean isShowHead, boolean isShowOperBar, boolean isShowFooter){
        if(adapterType < AudioListViewAdapter.ADAPTER_TYPE_ALLSONG || adapterType > AudioListViewAdapter.ADAPTER_TYPE_NETWORK)
            return;

        mAllSongAdapter = new AudioListViewAdapter(mContext, adapterType, isShowHead);
        mAllSongAdapter.setShowOperBar(isShowOperBar);
        mAllSongAdapter.setShowFooter(isShowFooter);
        mAllSongListView.setAdapter(mAllSongAdapter);
    }

    public void setSearchKey(String strKey){
        if(mAllSongAdapter != null){
            if(TextUtils.isEmpty(strKey)){
                mAllSongAdapter.clearSearchkKey();
            }
            else{
                mAllSongAdapter.setSearchKey(strKey);
            }
        }
    }

    public void setFilterFolder(String strFolderName){
        if(mAllSongAdapter != null){
            mAllSongAdapter.setFilterFolder(strFolderName);
        }
    }

    public void setFilterArtist(String strArtistrName){
        if(mAllSongAdapter != null){
            mAllSongAdapter.setFilterArtist(strArtistrName);
        }
    }

    public void setFilterAlbum(String strAlbumName){
        if(mAllSongAdapter != null){
            mAllSongAdapter.setFilterAlbum(strAlbumName);
        }
    }

    public void initAdapterData(){
        MediaLibrary.getInstance().asyncGetAllMediaEntity(new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                   final List<MediaEntity> list = (List<MediaEntity>)msg.obj;
                    if(list.size() >= 0){
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mAllSongListView.setVisibility(View.VISIBLE);
                                rlLoading.setVisibility(View.GONE);
                                mAllSongAdapter.initData(list);
                                if(mService != null){
                                    MediaEntity curMediaEntity = mService.getCurrentMedia();
                                    if(curMediaEntity != null){
                                        mAllSongAdapter.setItemPlayState(curMediaEntity, true);
                                    }
                                }
                            }
                        }, 50);
                    }
                    else{

                    }
                }
            }
        });
    }

    public void initAdapterData(final List<MediaEntity> list){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAllSongListView.setVisibility(View.VISIBLE);
                rlLoading.setVisibility(View.GONE);
                mAllSongAdapter.initData(list);
                if(mService != null){
                    MediaEntity curMediaEntity = mService.getCurrentMedia();
                    if(curMediaEntity != null){
                        mAllSongAdapter.setItemPlayState(curMediaEntity, true);
                    }
                }
            }
        }, 50);
    }

    public void initNetworkAdapterData(final List<AudioListViewAdapter.AudioNetWorkItemData> list){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAllSongListView.setVisibility(View.VISIBLE);
                rlLoading.setVisibility(View.GONE);
                mAllSongAdapter.initNetworkData(list);
                if(mService != null){
                    MediaEntity curMediaEntity = mService.getCurrentMedia();
                    if(curMediaEntity != null){
                        mAllSongAdapter.setItemPlayState(curMediaEntity, true);
                    }
                }
            }
        }, 50);
    }

    public void setMoreOperDialogData(List<Integer> list){
        if(list == null || list.size() ==0)
            return;

        if(mListMoreOperData == null){
            mListMoreOperData = new ArrayList<>();
        }
        mListMoreOperData.addAll(list);
    }

    public List<MediaEntity> getAdapterOriginData(){
        if(mAllSongAdapter != null)
            return mAllSongAdapter.getAdapterOriginData();

        return null;
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
        List<AudioListViewAdapter.AudioItemData> listAllData = adapter.getAdapterAllData();
        if(listAllData == null || listAllData.size() == 0)
            return;

        AudioListViewAdapter.AudioItemData itemData = listAllData.get(position);
        if(itemData == null)
            return ;

        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG && itemData.mItemType == AudioListViewAdapter.AudioItemData.TYPE_MEDIA){
//            if(mService.isPlaying()){
//                mService.pause();
//            }
//            else
            {
                int realPosition = -1;
                boolean bAdd = true;
                List<MediaEntity> listMediaEntity = new ArrayList<>();
                for(int i = 0;i < listAllData.size();i++){
                    AudioListViewAdapter.AudioItemData tempData = listAllData.get(i);
                    if(tempData == null)
                        continue;

                    if(tempData.mItemType != AudioListViewAdapter.AudioItemData.TYPE_MEDIA)
                        continue;

                    if(tempData.id == itemData.id){
                        realPosition++;
                        bAdd = false;
                    }
                    else{
                        if(bAdd){
                            realPosition++;
                        }
                    }

                    if(tempData.mListMedia != null){
                        listMediaEntity.addAll(tempData.mListMedia);
                    }
                }

                if(realPosition >= 0 && mService != null){
                    mService.play(listMediaEntity, realPosition);
                }
            }

            mAllSongAdapter.setItemPlayState(position, true);
        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
                AudioListViewAdapter.AudioFolderItemData folderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
                AudioFilterFragment audioFilterActivity = new AudioFilterFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AudioFilterFragment.FILTER_NAME, folderItemData.mFolderPath);
                bundle.putInt(AudioFilterFragment.FILTER_TYPE, adapterType);
                bundle.putString(AudioFilterFragment.TITLE_NAME, folderItemData.mFolderName);
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                audioFilterActivity.setArguments(bundle);
                android.support.v4.app.FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();
                //HomeActivity.getInstance().getFragmentManager().executePendingTransactions();
                transaction.replace(R.id.flContent, audioFilterActivity);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST || adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){
            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
                AudioListViewAdapter.AudioArtistAlbumItemData artistItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
                AudioFilterFragment audioFilterActivity = new AudioFilterFragment();
                Bundle bundle = new Bundle();
                bundle.putString(AudioFilterFragment.FILTER_NAME, artistItemData.mArtistAlbumName);
                bundle.putInt(AudioFilterFragment.FILTER_TYPE, adapterType);
                bundle.putString(AudioFilterFragment.TITLE_NAME, artistItemData.mArtistAlbumName);
                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                audioFilterActivity.setArguments(bundle);
                android.support.v4.app.FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();
                HomeActivity.getInstance().getFragmentManager().executePendingTransactions();
                transaction.add(R.id.flContent, audioFilterActivity);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        vibrator.vibrate(30);
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
            mMoreDialogbuilder = new MoreOperationDialog.Builder(mContext);
        }

        if(mMoreDialog == null){
            mMoreDialog = mMoreDialogbuilder.create();
            mMoreDialog.registerListener(this);
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
        if(mListMoreOperData == null){
            mMoreDialog.setMoreOperData(list);
        }
        else{
            mMoreDialog.setMoreOperData(mListMoreOperData);
        }
        mMoreDialog.setCancelable(true);
        mMoreDialog.show();
    }

    @Override
    public void onRandomPlayClick(AudioListViewAdapter adapter) {
        if(adapter == null || mService == null)
            return;

        mService.setRepeatMode(PreferenceConfig.PLAYMODE_RANDOM);
        mService.next(false);
    }

    @Override
    public void onBatchMgrClick(AudioListViewAdapter adapter) {
        Intent intent = new Intent(mContext, BatchMgrAudioActivity.class);
        List<MediaEntity> listTemp = new ArrayList<>();
        List<MediaEntity> temp = mAllSongAdapter.getAdapterOriginData();
        if(temp != null){
            listTemp.addAll(temp);
        }
        intent.putExtra(BatchMgrAudioActivity.INTENT_LIST_DATA, (Serializable)listTemp);
        HomeActivity.getInstance().startActivity(intent);
        HomeActivity.getInstance().overridePendingTransition(R.anim.anim_left_enter, R.anim.anim_right_exit);
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
        if(mService == null)
            return ;
    }

    @Override
    public void onMediaEvent(Media.Event event) {
        if(mService == null || event == null)
            return;
    }

    @Override
    public void onMediaPlayerEvent(MediaPlayer.Event event) {
        if(mService == null || event == null)
            return;

        if(event.type == MediaPlayer.Event.Playing){
            MediaEntity curMediaEntity = mService.getCurrentMedia();
            if(curMediaEntity != null && mAllSongAdapter != null){
                mAllSongAdapter.setItemPlayState(curMediaEntity, true);
            }
        }
        else if(event.type == MediaPlayer.Event.Stopped){
            if(mAllSongAdapter != null){
                MediaEntity tempEntity = new MediaEntity();
                tempEntity._id = 0;
                mAllSongAdapter.setItemPlayState(tempEntity, false);
            }
        }
    }

    //MoreOperationDialog.IMoreOperationDialogListener
    @Override
    public void onMoreItemClick(MoreOperationDialog dialog, int tag) {
        if(dialog == null)
            return;

        AudioListViewAdapter.AudioSongItemData mLVSongItemData = null;
        AudioListViewAdapter.AudioFolderItemData mLVFolderItemData = null;
        AudioListViewAdapter.AudioArtistAlbumItemData mLVArtistAlbumItemData = null;

        List<MediaEntity> listOperMediaEntity = null;
        int lvAdapterType = dialog.getLVAdapterType();
        AudioListViewAdapter.AudioItemData itemData = dialog.getAduioItemData();
        if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
            if(itemData instanceof AudioListViewAdapter.AudioSongItemData){
                mLVSongItemData = (AudioListViewAdapter.AudioSongItemData)itemData;
                listOperMediaEntity = mLVSongItemData.mListMedia;
            }
        }
        else if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
                mLVFolderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
                listOperMediaEntity = mLVFolderItemData.mListMedia;
            }
        }
        else{
            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
                mLVArtistAlbumItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
                listOperMediaEntity = mLVArtistAlbumItemData.mListMedia;
            }
        }

        if(listOperMediaEntity == null || listOperMediaEntity.size() == 0){
            Toast.makeText(mContext, "data error", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (tag){
            case MoreOperationDialog.MORE_ADD_NORMA:
                FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(mContext);
                FavoriteDialog dialogFavorite = builderFavorite.create();
                dialogFavorite.setCancelable(true);
                dialogFavorite.setFavoritelistData(MediaLibrary.getInstance().getAllFavoriteEntity());
                dialogFavorite.setMediaEntityData(listOperMediaEntity);
                dialogFavorite.show();
                dialogFavorite.setTitle("添加到歌单");
                break;
            case MoreOperationDialog.MORE_ALBUM_NORMAL:
                break;
            case MoreOperationDialog.MORE_BELL_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                if(listOperMediaEntity == null || listOperMediaEntity.size() > 1)
                    return;

                MediaEntity mediaEntity = listOperMediaEntity.get(0);
                if(mediaEntity == null)
                    return;

                String filePath = mediaEntity.getFilePath();
                ContentValues cv = new ContentValues();
                Uri uri = null, newUri = null;
                uri = MediaStore.Audio.Media.getContentUriForPath(filePath);
                Cursor cursor = mContext.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{filePath}, null);
                if(cursor.moveToFirst() && cursor.getCount() > 0){
                    String _id = cursor.getString(0);
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, false);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

                    // 把需要设为铃声的歌曲更新铃声库
                    mContext.getContentResolver().update(uri, cv, MediaStore.MediaColumns.DATA + "=?",new String[] { filePath });
                    newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
                    RingtoneManager.setActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_RINGTONE, newUri);
                    String strPromt = String.format("已将歌曲\"%s\"设置为铃声",mediaEntity.title);
                    Toast.makeText(mContext, strPromt, Toast.LENGTH_SHORT).show();
                }
                break;
            case MoreOperationDialog.MORE_DELETE_NORMAL:
                String strTitle = "";
                if(mLVSongItemData != null){
                    strTitle = "确定删除\"" + mLVSongItemData.mMainTitle + "\"吗?";
                }
                else if(mLVFolderItemData != null){
                    strTitle = "确定删除\"" + mLVFolderItemData.mFolderName + "\"下的所有歌曲吗?";
                }
                else if(mLVArtistAlbumItemData != null){
                    strTitle = "确定删除该歌手/专辑的所有歌曲吗?";
                }
                showDeleteAlterDialog(mContext, listOperMediaEntity, strTitle, false);
                break;
            case MoreOperationDialog.MORE_DOWNLOAD_NORMAL:
                break;
            case MoreOperationDialog.MORE_HIDE_NORMAL:
                AlertDialogHide hideOne = new AlertDialogHide(mContext);
                hideOne.show();
                hideOne.setMediaEntityData(listOperMediaEntity);
                hideOne.setAlertDialogListener(mAlertDialogHideListener);
                break;
            case MoreOperationDialog.MORE_LOVE_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                //多个文件时仅添加,单个文件可添加 可删除
                boolean isMutil = listOperMediaEntity.size() > 1 ? true : false;
                boolean isAddToFavorite = false;
                int iSuccessNum = 0;
                for(int i = 0;i < listOperMediaEntity.size();i++){
                    mediaEntity = listOperMediaEntity.get(i);
                    if(mediaEntity == null)
                        continue;

                    FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
                    favoritesMusicEntity.musicinfo_id = mediaEntity._id;
                    favoritesMusicEntity.artist = mediaEntity.artist;
                    favoritesMusicEntity.album = mediaEntity.album;
                    favoritesMusicEntity.fav_time = System.currentTimeMillis();
                    favoritesMusicEntity.path = mediaEntity._data;
                    favoritesMusicEntity.title = mediaEntity.title;
                    favoritesMusicEntity.favorite_id = MediaLibrary.getInstance().getDefaultFavoriteEntityId();

                    if(MediaLibrary.getInstance().queryIsFavoriteByMediaEntityId(mediaEntity._id, favoritesMusicEntity.favorite_id)){
                        if(isMutil == false){
                            boolean bRet = MediaLibrary.getInstance().removeFavoriteMusicEntity(favoritesMusicEntity.musicinfo_id, favoritesMusicEntity.favorite_id);
                            if(bRet){
                                iSuccessNum++;
                                isAddToFavorite = false;
                            }
                        }
                    }
                    else{
                        boolean bRet = MediaLibrary.getInstance().addFavoriteMusicEntity(favoritesMusicEntity);
                        if(bRet){
                            iSuccessNum++;
                            isAddToFavorite = true;
                        }
                    }
                }

                if(isMutil){
                    Toast.makeText(mContext, "成功" + iSuccessNum + "首,失败" + (listOperMediaEntity.size() - iSuccessNum) + "首", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!isAddToFavorite){
                        Toast.makeText(mContext, "已取消喜欢", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(mContext, "已添加到我喜欢的单曲", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case MoreOperationDialog.MORE_MV_NORMAL:
                break;
            case MoreOperationDialog.MORE_NEXTPLAY_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                mediaEntity = MediaLibrary.getInstance().getMediaEntityById(mLVSongItemData.id);
                if(mediaEntity == null)
                    return ;

                if(mLVSongItemData != null){
                    boolean bRet = mService.addSongToNext(mediaEntity);
                    if(bRet){
                        Toast.makeText(mContext, "已添加到播放列表", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MoreOperationDialog.MORE_PLAY_NORMAL:
                if(mService != null){
                    mService.play(listOperMediaEntity, 0);
                    //mAllSongAdapter.setItemPlayState(listOperMediaEntity.get(0), true);
                }
                break;
            case MoreOperationDialog.MORE_REMOVE_NORMAL:
                break;
            case MoreOperationDialog.MORE_SHARE_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                AndroidShare as = new AndroidShare(
                        mContext,
                        "哈哈---超方便的分享！！！来自allen",
                        "http://img6.cache.netease.com/cnews/news2012/img/logo_news.png");
                as.show();
                as.setTitle("分享 - " + mLVSongItemData.mMainTitle);
                break;
            case MoreOperationDialog.MORE_SONGER_NORMAL:
                break;
        }
        mMoreDialog.dismiss();
        if(mListener != null){
            mListener.onMoreOperClick(this, tag, listOperMediaEntity);
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void showDeleteAlterDialog(Context context, List<MediaEntity> listOperMediaEntity, String strTitle, boolean isNeedReCreate){
        if(isNeedReCreate){
            mAlertDialogDeleteOne = new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        if(mAlertDialogDeleteOne == null){
            mAlertDialogDeleteOne =  new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        mAlertDialogDeleteOne.show();
        mAlertDialogDeleteOne.setMediaEntityData(listOperMediaEntity);
        mAlertDialogDeleteOne.setTitle(strTitle);
    }

    public void handleDeleteMedia(List<MediaEntity> listMediaEntity, boolean isDeleteFile){
        if(listMediaEntity == null)
            return;

        //是否包含正在播放的歌曲
        boolean isContainPlaying = false;
        MediaEntity curMediaEntity = null;
        curMediaEntity = mService.getCurrentMedia();

        int successNum = 0;
        for(int i = 0;i < listMediaEntity.size();i++){
            MediaEntity entity = listMediaEntity.get(i);
            if(entity == null || entity._id < 0)
                continue;

            if(isDeleteFile){
                File file = new File(entity._data);
                if(file.exists()){
                    boolean bRet = file.delete();
                    if(bRet)
                        successNum++;
                }
            }
            else{
                successNum++;
            }

            if(curMediaEntity != null){
                if(curMediaEntity._id == entity._id){
                    isContainPlaying = true;
                }
            }
        }

        mService.mutilDeleteMediaByList(listMediaEntity);
        MediaLibrary.getInstance().mutilRemoveMediaEntity(listMediaEntity);

        String strPromt = "";
        if(successNum == 0){
            strPromt = "删除失败";
        }
        else if(successNum < listMediaEntity.size()){
            strPromt = "删除部分成功,部分失败";
        }
        else{
            strPromt = "删除成功";
        }
        Toast.makeText(mContext, strPromt, Toast.LENGTH_SHORT).show();

        if(isContainPlaying && mService != null){
            mService.reCalNextPlayIndex();
        }
        initAdapterData();
    }
}
