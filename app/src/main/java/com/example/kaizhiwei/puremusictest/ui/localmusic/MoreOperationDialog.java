package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import com.example.kaizhiwei.puremusictest.base.BaseDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by kaizhiwei on 16/11/26.
 */
public class MoreOperationDialog extends BaseDialog implements View.OnClickListener {
    public static final int MORE_ADD_NORMA = 1;
    public static final int MORE_ALBUM_NORMAL = 2;
    public static final int MORE_BELL_NORMAL = 3;
    public static final int MORE_DELETE_NORMAL = 4;
    public static final int MORE_DOWNLOAD_NORMAL = 5;
    public static final int MORE_HIDE_NORMAL = 6;
    public static final int MORE_LOVE_NORMAL = 7;
    public static final int MORE_MV_NORMAL = 8;
    public static final int MORE_NEXTPLAY_NORMAL = 9;
    public static final int MORE_PLAY_NORMAL = 10;
    public static final int MORE_REMOVE_NORMAL = 11;
    public static final int MORE_SHARE_NORMAL = 12;
    public static final int MORE_SONGER_NORMAL = 13;

    private List<MoreOperationAdapter.MoreOperationItemData>  mMoreOperAll;
    private List<MoreOperationAdapter.MoreOperationItemData> mListData = new ArrayList<>();
    private GridView gvMoreOperation;
    private MoreOperationAdapter mMoreAdapter;
    private Map<IMoreOperationDialogListener, Object> mMapListener;
    private String mKey;
    private int mLVAdapterType;

    public interface IMoreOperationDialogListener{
        void onMoreItemClick(MoreOperationDialog dialog, int tag);
    }

    public void setListener(IMoreOperationDialogListener listener){
        if(mMapListener == null){
            mMapListener = new HashMap<>();
        }

        mMapListener.put(listener,listener);
    }

    @Override
    public void onClick(View v) {
        for(IMoreOperationDialogListener key : mMapListener.keySet()){
            if(key != null){
                key.onMoreItemClick(this, (int)v.getTag());
            }
        }
    }

    public MoreOperationDialog(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        mMoreOperAll = new ArrayList<>();

        MoreOperationAdapter.MoreOperationItemData data = null;
        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_ADD_NORMA;
        data.strText = "添加到";
        data.resImage = R.drawable.ic_listmore_add_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_ALBUM_NORMAL;
        data.strText = "专辑";
        data.resImage = R.drawable.ic_listmore_album_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_BELL_NORMAL;
        data.strText = "铃声";
        data.resImage = R.drawable.ic_listmore_bell_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_DELETE_NORMAL;
        data.strText = "删除";
        data.resImage = R.drawable.ic_listmore_delete_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_DOWNLOAD_NORMAL;
        data.strText = "下载";
        data.resImage = R.drawable.ic_listmore_download_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.strText = "下一首播放";
        data.resImage = R.drawable.ic_listmore_nextplay_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_HIDE_NORMAL;
        data.strText = "隐藏";
        data.resImage = R.drawable.ic_listmore_filter_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_LOVE_NORMAL;
        data.strText = "喜欢";
        data.strSelText = "取消喜欢";
        data.resImage = R.drawable.ic_listmore_love_normal;
        data.resSelImage = R.drawable.ic_listmore_love_hl;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_MV_NORMAL;
        data.strText = "MV";
        data.resImage = R.drawable.ic_listmore_mv_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_NEXTPLAY_NORMAL;
        data.strText = "下一首播放";
        data.resImage = R.drawable.ic_listmore_nextplay_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_PLAY_NORMAL;
        data.strText = "播放";
        data.resImage = R.drawable.ic_listmore_play_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_REMOVE_NORMAL;
        data.strText = "移除";
        data.resImage = R.drawable.ic_listmore_remove_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_SHARE_NORMAL;
        data.strText = "分享";
        data.resImage = R.drawable.ic_listmore_share_normal;
        mMoreOperAll.add(data);

        data = new MoreOperationAdapter.MoreOperationItemData();
        data.id = MORE_SONGER_NORMAL;
        data.strText = "歌手";
        data.resImage = R.drawable.ic_listmore_songer_normal;
        mMoreOperAll.add(data);
    }

    @Override
    public void initView() {
        gvMoreOperation = (GridView)findViewById(R.id.gvMoreOperation);
    }

//    public void setMoreLVData(int lvAdapterType, AudioListViewAdapter.AudioItemData itemData){
//        mLVAdapterType = lvAdapterType;
//
//        mLVSongItemData = null;
//        mLVFolderItemData = null;
//        mLVArtistAlbumItemData = null;
//
//        if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
//            if(itemData instanceof AudioListViewAdapter.AudioSongItemData){
//                mLVSongItemData = (AudioListViewAdapter.AudioSongItemData)itemData;
//            }
//        }
//        else if(lvAdapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
//            if(itemData instanceof AudioListViewAdapter.AudioFolderItemData){
//                mLVFolderItemData = (AudioListViewAdapter.AudioFolderItemData)itemData;
//            }
//        }
//        else{
//            if(itemData instanceof AudioListViewAdapter.AudioArtistAlbumItemData){
//                mLVArtistAlbumItemData = (AudioListViewAdapter.AudioArtistAlbumItemData)itemData;
//            }
//        }
//
//        if(mLVSongItemData != null){
//            boolean bFavorite = MediaLibrary.getInstance().queryIsFavoriteByMusicInfoDaoId(mLVSongItemData.id, 1);
//            setFavorite(bFavorite);
//        }
//
//        if(mMoreAdapter != null){
//            mMoreAdapter.notifyDataSetChanged();
//        }
//    }
//
//    public int getLVAdapterType(){
//        return mLVAdapterType;
//    }
//
//    public AudioListViewAdapter.AudioItemData getAduioItemData(){
//        if(mLVAdapterType == AudioListViewAdapter.ADAPTER_TYPE_ALLSONG){
//            return mLVSongItemData;
//        }
//        else if(mLVAdapterType == AudioListViewAdapter.ADAPTER_TYPE_FOLDER){
//            return mLVFolderItemData;
//        }
//        else{
//            return mLVArtistAlbumItemData;
//        }
//    }

    public void setMoreOperData(List<Integer> listMore){
        List<MoreOperationAdapter.MoreOperationItemData> listData = new ArrayList<>();
        for(int i = 0;i < listMore.size();i++){
            for(int j = 0;j < mMoreOperAll.size();j++){
                if(listMore.get(i) == mMoreOperAll.get(j).id){
                    listData.add(mMoreOperAll.get(j));
                }
            }
        }

        if(listMore.size() < 4){
            gvMoreOperation.setNumColumns(listMore.size());
        }
        else{
            gvMoreOperation.setNumColumns(4);
        }

        mMoreAdapter = new MoreOperationAdapter(this, listData);
        gvMoreOperation.setAdapter(mMoreAdapter);
    }

    public boolean isFavorite(){
        for(int i = 0;i < mMoreOperAll.size();i++){
            if(mMoreOperAll.get(i).id == MORE_LOVE_NORMAL){
                return mMoreOperAll.get(i).isSelect ;
            }
        }

        return false;
    }

    public void setFavorite(boolean bFavorite){
        for(int i = 0;i < mMoreOperAll.size();i++){
            if(mMoreOperAll.get(i).id == MORE_LOVE_NORMAL){
                mMoreOperAll.get(i).isSelect = bFavorite;
            }
        }
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    protected void onStart() {
        super.onStart();
    }

    /**
     * Called to tell you that you're stopping.
     */
    protected void onStop() {
        super.onStop();
    }

    public static class Builder extends BaseDialog.Builder{

        public Builder(Context context) {
            super(context);
        }

        @Override
        protected <T extends BaseDialog> T createDialog() {
            final MoreOperationDialog dialog = new MoreOperationDialog(mContext);
            return (T) dialog;
        }

        @Override
        protected int getCustomeView() {
            return R.layout.more_operation_dialog;
        }
    }
}
