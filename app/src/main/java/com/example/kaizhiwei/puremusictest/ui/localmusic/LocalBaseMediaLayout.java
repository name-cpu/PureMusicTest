package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.kaizhiwei.puremusictest.CommonUI.AndroidShare;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.presenter.LocalMusicPresenter;
import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.MediaData.PreferenceConfig;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.service.PlaybackService;
import com.example.kaizhiwei.puremusictest.widget.RecyclerViewDividerDecoration;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import me.yokeyword.indexablerv.EntityWrapper;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;
import me.yokeyword.indexablerv.RealAdapter;

/**
 * Created by kaizhiwei on 17/1/14.
 */
public class LocalBaseMediaLayout extends LinearLayout implements MediaLibrary.IMediaScanListener
        ,PlaybackService.Client.Callback, PlaybackService.Callback, MoreOperationDialog.IMoreOperationDialogListener,
        View.OnClickListener, LocalMusicContract.View, LocalAudioAdapter.ILocalAudioListener, IndexableAdapter.OnItemContentClickListener<LocalAudioAdapter.LocalAudioItemData>,IndexableAdapter.OnItemContentLongClickListener<LocalAudioAdapter.LocalAudioItemData> {
    private Context mContext;
    private IndexableLayout indexLayout;
    private LocalAudioAdapter mAdapter;

    private Handler mHandler = new Handler();
    private Vibrator vibrator;

    private PlaybackService.Client mClient = null;
    private PlaybackService mService;
    private MoreOperationDialog.Builder mMoreDialogbuilder = null;
    private MoreOperationDialog mMoreDialog = null;
    private List<Integer> mListMoreOperData;

    private LocalMusicPresenter mPresenter;

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

            List<MusicInfoDao> listMusicInfoDao = dialog.getMusicInfoDaoData();
            if(listMusicInfoDao == null || listMusicInfoDao.size() == 0)
                return;

            handleDeleteMedia(listMusicInfoDao, isDeleteFile);
        }
    };

    //隐藏对话框监听
    private AlertDialogHide.IAlertDialogHideListener mAlertDialogHideListener = new AlertDialogHide.IAlertDialogHideListener(){

        @Override
        public void onAlterDialogHideOk(String strFolderPath) {
            List<String> list = PreferenceConfig.getInstance().getScanFilterByFolderName();
            if(!list.contains(strFolderPath))
                list.add(strFolderPath);
            PreferenceConfig.getInstance().setScanFilterByFolderName(list);
        }
    };

    private LayoutType mType = LayoutType.ALLSONG;
    public enum LayoutType{
        ALLSONG, FOLDER, ARTIST, ALBUM
    }

    public LayoutType getType() {
        return mType;
    }

    public void setType(LayoutType mType) {
        this.mType = mType;
        if(mAdapter == null){
            return;
        }

        int contentResId;
        switch (mType){
            case ALLSONG:
                contentResId = R.layout.item_local_audio_allsong;
                indexLayout.showAllLetter(true);
                indexLayout.setIndexBarVisibility(true);
                break;
            case FOLDER:
                contentResId = R.layout.item_local_audio_folder;
                indexLayout.showAllLetter(false);
                indexLayout.setIndexBarVisibility(false);
                indexLayout.showIndexTitle(false);
                break;
            case ARTIST:
            case ALBUM:
                contentResId = R.layout.item_local_audio_artist_album;
                indexLayout.showAllLetter(false);
                indexLayout.setIndexBarVisibility(false);
                indexLayout.showIndexTitle(false);
                break;
            default:
                contentResId = R.layout.item_local_audio_allsong;
                break;
        }
        mAdapter.setContentResId(contentResId);
    }

    public interface ILocalBaseListener{
        void onFragmentInitFinish(LinearLayout fragment);
        void onMoreOperClick(LocalBaseMediaLayout layout, int flag, Object obj);
    }

    public LocalBaseMediaLayout(Context context) {
        this(context, null, 0);
    }

    public LocalBaseMediaLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
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
        mPresenter = new LocalMusicPresenter(this);

        View rootView = inflater.inflate(R.layout.fragment_all_media, null);
        mClient = new PlaybackService.Client(mContext, this);
        vibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);

        indexLayout = (IndexableLayout) rootView.findViewById(R.id.indexLayout);
        indexLayout.setFastCompare(true);
        indexLayout.setStickyEnable(false);
        indexLayout.setLayoutManager(new LinearLayoutManager(mContext));
        indexLayout.setOverlayStyle_Center();
        indexLayout.setVisibility(View.GONE);
        indexLayout.getRecyclerView().addItemDecoration(new RecyclerViewDividerDecoration(mContext, RecyclerViewDividerDecoration.HORIZONTAL_LIST));

        mAdapter = new LocalAudioAdapter(mContext);
        mAdapter.setListener(this);
        mAdapter.setOnItemContentClickListener(this);
        mAdapter.setOnItemContentLongClickListener(this);
        indexLayout.setAdapter(mAdapter);

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
//        MediaLibrary.getInstance().registerListener(this);
//        mAllSongAdapter.registerListener(this);
    }

    public void onPause() {
//        MediaLibrary.getInstance().unregisterListener(this);
//        mAllSongAdapter.unregisterListener(this);
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

    public void setSearchKey(String strKey){
//        if(mAllSongAdapter != null){
//            if(TextUtils.isEmpty(strKey)){
//                mAllSongAdapter.clearSearchkKey();
//            }
//            else{
//                mAllSongAdapter.setSearchKey(strKey);
//            }
//        }
    }

    public void setFilterFolder(String strFolderName){
//        if(mAllSongAdapter != null){
//            mAllSongAdapter.setFilterFolder(strFolderName);
//        }
    }

    public void setFilterArtist(String strArtistrName){
//        if(mAllSongAdapter != null){
//            mAllSongAdapter.setFilterArtist(strArtistrName);
//        }
    }

    public void setFilterAlbum(String strAlbumName){
//        if(mAllSongAdapter != null){
//            mAllSongAdapter.setFilterAlbum(strAlbumName);
//        }
    }

    public void initAdapterData(){
        if(mType == LayoutType.ALLSONG){
            mPresenter.getAllMusicInfos();
        }
        else if(mType == LayoutType.FOLDER){
            mPresenter.getMusicInfosByFolder("");
        }
        else if(mType == LayoutType.ARTIST){
            mPresenter.getMusicInfosByArtist("");
        }
        else if(mType == LayoutType.ALBUM){
            mPresenter.getMusicInfosByAlbum("");
        }
    }

    public void setMoreOperDialogData(List<Integer> list){
        if(list == null || list.size() ==0)
            return;

        if(mListMoreOperData == null){
            mListMoreOperData = new ArrayList<>();
        }
        mListMoreOperData.addAll(list);
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetAllMusicInfos(final List<MusicInfoDao> list) {
        if(list.size() >= 0){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    indexLayout.setVisibility(View.VISIBLE);
                    rlLoading.setVisibility(View.GONE);

                    List<LocalAudioAdapter.LocalAudioItemData> listData = new ArrayList<>();
                    for(int i = 0; i < list.size();i++){
                        LocalAudioAdapter.LocalAudioItemData itemData = new LocalAudioAdapter.LocalAudioItemData();
                        itemData.setStrMain(list.get(i).getTitle());
                        itemData.setStrSub(list.get(i).getArtist());
                        itemData.setId(list.get(i).get_id());
                        listData.add(itemData);
                    }
                    mAdapter.setDatas(listData);

                    if(mService != null){
                        MusicInfoDao curMusicInfoDao = mService.getCurrentMedia();
                        //if(curMusicInfoDao != null){
                        //   mAllSongAdapter.setItemPlayState(curMusicInfoDao, true);
                        //}
                    }
                }
            }, 50);
        }
        else{

        }
    }

    @Override
    public void onGetMusicInfosByFolder(List<MusicInfoDao> list) {
        indexLayout.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);

        List<LocalAudioAdapter.LocalAudioItemData> listData = new ArrayList<>();
        Map<String, List<MusicInfoDao>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        for(int i = 0; i < list.size();i++){
            List<MusicInfoDao> listDaos = map.get(list.get(i).getSave_path());
            if(listDaos == null){
                listDaos = new ArrayList<>();
            }
            listDaos.add(list.get(i));
            map.put(list.get(i).getSave_path(), listDaos);
        }

        Set<String> keys = map.keySet();
        for(String key : keys){
            String[] strs = key.split(File.separator);
            List<MusicInfoDao> listDaos = map.get(key);
            LocalAudioAdapter.LocalAudioItemData itemData = new LocalAudioAdapter.LocalAudioItemData();
            itemData.setStrMain(strs[strs.length-1]);
            itemData.setStrSub(listDaos.size() + "首");
            itemData.setStrThird(key);
            listData.add(itemData);
        }
        mAdapter.setDatas(listData);
    }

    @Override
    public void onGetMusicInfosByArtist(List<MusicInfoDao> list) {
        indexLayout.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);

        List<LocalAudioAdapter.LocalAudioItemData> listData = new ArrayList<>();
        Map<String, List<MusicInfoDao>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        for(int i = 0; i < list.size();i++){
            List<MusicInfoDao> listDaos = map.get(list.get(i).getArtist());
            if(listDaos == null){
                listDaos = new ArrayList<>();
            }
            listDaos.add(list.get(i));
            map.put(list.get(i).getArtist(), listDaos);
        }

        Set<String> keys = map.keySet();
        for(String key : keys){
            String[] strs = key.split(File.separator);
            List<MusicInfoDao> listDaos = map.get(key);
            LocalAudioAdapter.LocalAudioItemData itemData = new LocalAudioAdapter.LocalAudioItemData();
            itemData.setStrMain(strs[strs.length-1]);
            itemData.setStrSub(listDaos.size() + "首");
            itemData.setStrThird(key);
            listData.add(itemData);
        }
        mAdapter.setDatas(listData);
    }

    @Override
    public void onGetMusicInfosByAlbum(List<MusicInfoDao> list) {
        indexLayout.setVisibility(View.VISIBLE);
        rlLoading.setVisibility(View.GONE);

        List<LocalAudioAdapter.LocalAudioItemData> listData = new ArrayList<>();
        Map<String, List<MusicInfoDao>> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        for(int i = 0; i < list.size();i++){
            List<MusicInfoDao> listDaos = map.get(list.get(i).getAlbum());
            if(listDaos == null){
                listDaos = new ArrayList<>();
            }
            listDaos.add(list.get(i));
            map.put(list.get(i).getAlbum(), listDaos);
        }

        Set<String> keys = map.keySet();
        for(String key : keys){
            String[] strs = key.split(File.separator);
            List<MusicInfoDao> listDaos = map.get(key);
            LocalAudioAdapter.LocalAudioItemData itemData = new LocalAudioAdapter.LocalAudioItemData();
            itemData.setStrMain(strs[strs.length-1]);
            itemData.setStrSub(listDaos.size() + "首");
            itemData.setStrThird(key);
            listData.add(itemData);
        }
        mAdapter.setDatas(listData);
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
    }

    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, LocalAudioAdapter.LocalAudioItemData entity) {
        List<MusicInfoDao> listMusicInfoDao = new ArrayList<>();
        listMusicInfoDao.add(mPresenter.getMusicInfoById(entity.getId()));

        if(mService != null){
            //mService.play(, originalPosition);
        }
    }

    @Override
    public void onMoreClick(LocalAudioAdapter adapter, int position) {
        if(adapter == null)
            return ;

        RealAdapter realAdapter = (RealAdapter) indexLayout.getRecyclerView().getAdapter();
        ArrayList<EntityWrapper> listReal = realAdapter.getItems();
        int originPosition = listReal.get(position).getOriginalPosition();

        if(mMoreDialogbuilder == null){
            mMoreDialogbuilder = new MoreOperationDialog.Builder(mContext);
        }

        if(mMoreDialog == null){
            mMoreDialog = (MoreOperationDialog)mMoreDialogbuilder.create();
            mMoreDialog.registerListener(this);
        }
        LocalAudioAdapter.LocalAudioItemData itemData = adapter.getItemData(originPosition);
        if(itemData == null)
            return;

        List<Integer> list = new ArrayList<>();
        if(mType == LayoutType.ALLSONG){
            mMoreDialog.setTitle(itemData.getStrMain());
            list.add(MoreOperationDialog.MORE_NEXTPLAY_NORMAL);
            list.add(MoreOperationDialog.MORE_LOVE_NORMAL);
            list.add(MoreOperationDialog.MORE_BELL_NORMAL);
            list.add(MoreOperationDialog.MORE_SHARE_NORMAL);
            list.add(MoreOperationDialog.MORE_ADD_NORMA);
            list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
        }
        else if(mType == LayoutType.FOLDER){
            mMoreDialog.setTitle(itemData.getStrMain());
            list.add(MoreOperationDialog.MORE_PLAY_NORMAL);
            list.add(MoreOperationDialog.MORE_ADD_NORMA);
            list.add(MoreOperationDialog.MORE_HIDE_NORMAL);
            list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
        }
        else{
            mMoreDialog.setTitle(itemData.getStrMain());
            list.add(MoreOperationDialog.MORE_PLAY_NORMAL);
            list.add(MoreOperationDialog.MORE_ADD_NORMA);
            list.add(MoreOperationDialog.MORE_DELETE_NORMAL);
        }

        //mMoreDialog.setMoreLVData(adapterType, itemData);
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
    public boolean onItemLongClick(View v, int originalPosition, int currentPosition, LocalAudioAdapter.LocalAudioItemData entity) {
        vibrator.vibrate(30);
        onMoreClick(mAdapter, currentPosition);
        return true;
    }

//    //AdapterView.OnItemClickListener
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        AudioListViewAdapter adapter = (AudioListViewAdapter)parent.getAdapter();
//        int adapterType = adapter.getAdapterType();
//        List<AudioListViewAdapter.AudioItemData> listAllData = adapter.getAdapterAllData();
//        if(listAllData == null || listAllData.size() == 0)
//            return;
//
//        AudioListViewAdapter.AudioItemData itemData = listAllData.get(position);
//        if(itemData == null)
//            return ;
//
//        if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG && itemData.mItemType == AudioListViewAdapter.AudioItemData.TYPE_MEDIA){
////            if(mService.isPlaying()){
////                mService.pause();
////            }
////            else
//            {
//                int realPosition = -1;
//                boolean bAdd = true;
//                List<MusicInfoDao> listMusicInfoDao = new ArrayList<>();
//                for(int i = 0;i < listAllData.size();i++){
//                    AudioListViewAdapter.AudioItemData tempData = listAllData.get(i);
//                    if(tempData == null)
//                        continue;
//
//                    if(tempData.mItemType != AudioListViewAdapter.AudioItemData.TYPE_MEDIA)
//                        continue;
//
//                    if(tempData.id == itemData.id){
//                        realPosition++;
//                        bAdd = false;
//                    }
//                    else{
//                        if(bAdd){
//                            realPosition++;
//                        }
//                    }
//
//                    if(tempData.mListMedia != null){
//                        listMusicInfoDao.addAll(tempData.mListMedia);
//                    }
//                }
//
//                if(realPosition >= 0 && mService != null){
//                    mService.play(listMusicInfoDao, realPosition);
//                }
//            }
////
////            mAllSongAdapter.setItemPlayState(position, true);
//        }
//        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
//            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
//                AudioListViewAdapter.AudioFolderItemData folderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
//                AudioFilterFragment audioFilterActivity = new AudioFilterFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(AudioFilterFragment.FILTER_NAME, folderItemData.mFolderPath);
//                bundle.putInt(AudioFilterFragment.FILTER_TYPE, adapterType);
//                bundle.putString(AudioFilterFragment.TITLE_NAME, folderItemData.mFolderName);
//                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                audioFilterActivity.setArguments(bundle);
//                android.support.v4.app.FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();
//                //HomeActivity.getInstance().getFragmentManager().executePendingTransactions();
//                transaction.replace(R.id.flContent, audioFilterActivity);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        }
//        else if(adapterType == AudioListViewAdapter.ADAPTER_TYPE_ARTIST || adapterType == AudioListViewAdapter.ADAPTER_TYPE_ALBUM){
//            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
//                AudioListViewAdapter.AudioArtistAlbumItemData artistItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
//                AudioFilterFragment audioFilterActivity = new AudioFilterFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString(AudioFilterFragment.FILTER_NAME, artistItemData.mArtistAlbumName);
//                bundle.putInt(AudioFilterFragment.FILTER_TYPE, adapterType);
//                bundle.putString(AudioFilterFragment.TITLE_NAME, artistItemData.mArtistAlbumName);
//                //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
//                audioFilterActivity.setArguments(bundle);
//                android.support.v4.app.FragmentTransaction transaction = HomeActivity.getInstance().getSupportFragmentManager().beginTransaction();
//                HomeActivity.getInstance().getFragmentManager().executePendingTransactions();
//                transaction.add(R.id.flContent, audioFilterActivity);
//                transaction.addToBackStack(null);
//                transaction.commit();
//            }
//        }
//    }
//
//    //AdapterView.OnScrollChangeListener
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    public void onScroll(AbsListView view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount) {
//
//        boolean enable = false;
//        if(view != null && view.getChildCount() > 0){
//            // check if the first item of the list is visible
//            boolean firstItemVisible = view.getFirstVisiblePosition() == 0;
//            // check if the top of the first item is visible
//            boolean topOfFirstItemVisible = view.getChildAt(0).getTop() == 0;
//            // enabling or disabling the refresh layout
//            enable = firstItemVisible && topOfFirstItemVisible;
//        }
//    }
//
//    @Override
//    public void onRandomPlayClick(AudioListViewAdapter adapter) {
//        if(adapter == null || mService == null)
//            return;
//
//        mService.setRepeatMode(PreferenceConfig.PLAYMODE_RANDOM);
//        mService.next(false);
//    }
//
//    @Override
//    public void onBatchMgrClick(AudioListViewAdapter adapter) {
//        Intent intent = new Intent(mContext, BatchMgrAudioActivity.class);
//        List<MusicInfoDao> listTemp = new ArrayList<>();
////        List<MusicInfoDao> temp = mAllSongAdapter.getAdapterOriginData();
////        if(temp != null){
////            listTemp.addAll(temp);
////        }
////        intent.putExtra(BatchMgrAudioActivity.INTENT_LIST_DATA, (Serializable)listTemp);
////        HomeActivity.getInstance().startActivity(intent);
////        HomeActivity.getInstance().overridePendingTransition(R.anim.anim_left_enter, R.anim.anim_right_exit);
//    }

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

//        if(event.type == MediaPlayer.Event.Playing){
//            MusicInfoDao curMusicInfoDao = mService.getCurrentMedia();
//            if(curMusicInfoDao != null && mAllSongAdapter != null){
//                mAllSongAdapter.setItemPlayState(curMusicInfoDao, true);
//            }
//        }
//        else if(event.type == MediaPlayer.Event.Stopped){
//            if(mAllSongAdapter != null){
//                MusicInfoDao tempEntity = new MusicInfoDao();
//                tempEntity.set_id(0);
//                mAllSongAdapter.setItemPlayState(tempEntity, false);
//            }
//        }
    }

    //MoreOperationDialog.IMoreOperationDialogListener
    @Override
    public void onMoreItemClick(MoreOperationDialog dialog, int tag) {
        if(dialog == null)
            return;

        AudioListViewAdapter.AudioSongItemData mLVSongItemData = null;
        AudioListViewAdapter.AudioFolderItemData mLVFolderItemData = null;
        AudioListViewAdapter.AudioArtistAlbumItemData mLVArtistAlbumItemData = null;

        List<MusicInfoDao> listOperMusicInfoDao = null;
        int lvAdapterType = dialog.getLVAdapterType();
        AudioListViewAdapter.AudioItemData itemData = dialog.getAduioItemData();
        if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
            if(itemData instanceof AudioListViewAdapter.AudioSongItemData){
                mLVSongItemData = (AudioListViewAdapter.AudioSongItemData)itemData;
                listOperMusicInfoDao = mLVSongItemData.mListMedia;
            }
        }
        else if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
                mLVFolderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
                listOperMusicInfoDao = mLVFolderItemData.mListMedia;
            }
        }
        else{
            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
                mLVArtistAlbumItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
                listOperMusicInfoDao = mLVArtistAlbumItemData.mListMedia;
            }
        }

        if(listOperMusicInfoDao == null || listOperMusicInfoDao.size() == 0){
            Toast.makeText(mContext, "data error", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (tag){
            case MoreOperationDialog.MORE_ADD_NORMA:
                FavoriteDialog.Builder builderFavorite = new FavoriteDialog.Builder(mContext);
                FavoriteDialog dialogFavorite = builderFavorite.create();
                dialogFavorite.setCancelable(true);
                dialogFavorite.setFavoritelistData(MediaLibrary.getInstance().getAllFavoriteEntity());
                dialogFavorite.setMusicInfoDaoData(listOperMusicInfoDao);
                dialogFavorite.show();
                dialogFavorite.setTitle("添加到歌单");
                break;
            case MoreOperationDialog.MORE_ALBUM_NORMAL:
                break;
            case MoreOperationDialog.MORE_BELL_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                if(listOperMusicInfoDao == null || listOperMusicInfoDao.size() > 1)
                    return;

                MusicInfoDao MusicInfoDao = listOperMusicInfoDao.get(0);
                if(MusicInfoDao == null)
                    return;

                String filePath = MusicInfoDao.getSave_path();
                ContentValues cv = new ContentValues();
                Uri uri = null, newUri = null;
                uri = MediaStore.Audio.Media.getContentUriForPath(filePath);
                Cursor cursor = mContext.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{filePath}, null);
                if(cursor.moveToFirst() && cursor.getCount() > 0){
                    String id = cursor.getString(0);
                    cv.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                    cv.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
                    cv.put(MediaStore.Audio.Media.IS_ALARM, false);
                    cv.put(MediaStore.Audio.Media.IS_MUSIC, false);

                    // 把需要设为铃声的歌曲更新铃声库
                    mContext.getContentResolver().update(uri, cv, MediaStore.MediaColumns.DATA + "=?",new String[] { filePath });
                    newUri = ContentUris.withAppendedId(uri, Long.valueOf(id));
                    RingtoneManager.setActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_RINGTONE, newUri);
                    String strPromt = String.format("已将歌曲\"%s\"设置为铃声",MusicInfoDao.getTitle());
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
                showDeleteAlterDialog(mContext, listOperMusicInfoDao, strTitle, false);
                break;
            case MoreOperationDialog.MORE_DOWNLOAD_NORMAL:
                break;
            case MoreOperationDialog.MORE_HIDE_NORMAL:
                AlertDialogHide hideOne = new AlertDialogHide(mContext);
                hideOne.show();
                hideOne.setMusicInfoDaoData(listOperMusicInfoDao);
                hideOne.setAlertDialogListener(mAlertDialogHideListener);
                break;
            case MoreOperationDialog.MORE_LOVE_NORMAL:
                if(mLVSongItemData == null)
                    return ;

                //多个文件时仅添加,单个文件可添加 可删除
                boolean isMutil = listOperMusicInfoDao.size() > 1 ? true : false;
                boolean isAddToFavorite = false;
                int iSuccessNum = 0;
                for(int i = 0;i < listOperMusicInfoDao.size();i++){
                    MusicInfoDao = listOperMusicInfoDao.get(i);
                    if(MusicInfoDao == null)
                        continue;

                    FavoritesMusicEntity favoritesMusicEntity = new FavoritesMusicEntity();
                    favoritesMusicEntity.musicinfo_id = MusicInfoDao.get_id();
                    favoritesMusicEntity.artist = MusicInfoDao.getTitle();
                    favoritesMusicEntity.album = MusicInfoDao.getAlbum();
                    favoritesMusicEntity.fav_time = System.currentTimeMillis();
                    favoritesMusicEntity.path = MusicInfoDao.get_data();
                    favoritesMusicEntity.title = MusicInfoDao.getTitle();
                    favoritesMusicEntity.favorite_id = MediaLibrary.getInstance().getDefaultFavoriteEntityId();

                    if(MediaLibrary.getInstance().queryIsFavoriteByMusicInfoDaoId(MusicInfoDao.get_id(), favoritesMusicEntity.favorite_id)){
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
                    Toast.makeText(mContext, "成功" + iSuccessNum + "首,失败" + (listOperMusicInfoDao.size() - iSuccessNum) + "首", Toast.LENGTH_SHORT).show();
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

                MusicInfoDao = MediaLibrary.getInstance().getMusicInfoDaoById(mLVSongItemData.id);
                if(MusicInfoDao == null)
                    return ;

                if(mLVSongItemData != null){
                    boolean bRet = mService.addSongToNext(MusicInfoDao);
                    if(bRet){
                        Toast.makeText(mContext, "已添加到播放列表", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MoreOperationDialog.MORE_PLAY_NORMAL:
                if(mService != null){
                    mService.play(listOperMusicInfoDao, 0);
                    //mAllSongAdapter.setItemPlayState(listOperMusicInfoDao.get(0), true);
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
            mListener.onMoreOperClick(this, tag, listOperMusicInfoDao);
        }
    }

    @Override
    public void onClick(View v) {
    }

    public void showDeleteAlterDialog(Context context, List<MusicInfoDao> listOperMusicInfoDao, String strTitle, boolean isNeedReCreate){
        if(isNeedReCreate){
            mAlertDialogDeleteOne = new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        if(mAlertDialogDeleteOne == null){
            mAlertDialogDeleteOne =  new AlertDialogDeleteOne(context, mAlertDialogDeleteListener);
        }

        mAlertDialogDeleteOne.show();
        mAlertDialogDeleteOne.setMusicInfoDaoData(listOperMusicInfoDao);
        mAlertDialogDeleteOne.setTitle(strTitle);
    }

    public void handleDeleteMedia(List<MusicInfoDao> listMusicInfoDao, boolean isDeleteFile){
        if(listMusicInfoDao == null)
            return;

        //是否包含正在播放的歌曲
        boolean isContainPlaying = false;
        MusicInfoDao curMusicInfoDao = null;
        curMusicInfoDao = mService.getCurrentMedia();

        int successNum = 0;
        for(int i = 0;i < listMusicInfoDao.size();i++){
            MusicInfoDao entity = listMusicInfoDao.get(i);
            if(entity == null || entity.get_id() < 0)
                continue;

            if(isDeleteFile){
                File file = new File(entity.get_data());
                if(file.exists()){
                    boolean bRet = file.delete();
                    if(bRet)
                        successNum++;
                }
            }
            else{
                successNum++;
            }

            if(curMusicInfoDao != null){
                if(curMusicInfoDao.get_id() == entity.get_id()){
                    isContainPlaying = true;
                }
            }
        }

        mService.mutilDeleteMediaByList(listMusicInfoDao);
        MediaLibrary.getInstance().mutilRemoveMusicInfoDao(listMusicInfoDao);

        String strPromt = "";
        if(successNum == 0){
            strPromt = "删除失败";
        }
        else if(successNum < listMusicInfoDao.size()){
            strPromt = "删除部分成功,部分失败";
        }
        else{
            strPromt = "删除成功";
        }
        Toast.makeText(mContext, strPromt, Toast.LENGTH_SHORT).show();

        if(isContainPlaying && mService != null){
            mService.reCalNextPlayIndex();
        }
    }
}
