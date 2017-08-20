
package com.example.kaizhiwei.puremusictest.ui.localmusic;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.MediaData.FavoritesMusicEntity;
import com.example.kaizhiwei.puremusictest.MediaData.MediaLibrary;
import com.example.kaizhiwei.puremusictest.R;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.base.BaseDialog;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.contract.PlaylistContract;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.presenter.LocalMusicPresenter;
import com.example.kaizhiwei.puremusictest.presenter.PlaylistPrensenter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kaizhiwei on 16/11/27.
 */
public class FavoriteDialog extends BaseDialog implements View.OnClickListener, AbsListView.OnItemClickListener, AlertDialogFavorite.OnAlterDialogFavoriteListener, FavoriteListViewAdapter.IFavoriteOperListener, PlaylistContract.View, LocalMusicContract.View {
    private ListView lvFavorite;
    private FavoriteListViewAdapter mFavoriteListAdapter;
    private List<MusicInfoDao> mListMusicInfoDao;
    private PlaylistPrensenter mPresenter;
    private LocalMusicPresenter localMusicPresenter;
    private String mStrKey;
    private LocalBaseMediaLayout.LayoutType mKeyType;
    private PlaylistDao mOperatePlaylist;

    public FavoriteDialog(Context context) {
        super(context);
    }

    public void setMusicInfoDaoData(List<MusicInfoDao> list){
        mListMusicInfoDao = list;
    }

    @Override
    public void initData() {
        mPresenter = new PlaylistPrensenter(this);
        mPresenter.getPlaylists();

        localMusicPresenter = new LocalMusicPresenter(this);
    }

    @Override
    public void initView() {
        lvFavorite = (ListView) this.findViewById(R.id.lvFavorite);
        lvFavorite.setOnItemClickListener(this);
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

    public String getStrKey() {
        return mStrKey;
    }

    public void setStrKey(String mStrKey) {
        this.mStrKey = mStrKey;
    }

    public LocalBaseMediaLayout.LayoutType getKeyType() {
        return mKeyType;
    }

    public void setKeyType(LocalBaseMediaLayout.LayoutType mKeyType) {
        this.mKeyType = mKeyType;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PlaylistDao playlistDao = (PlaylistDao)mFavoriteListAdapter.getItem(position);
        if(playlistDao == null)
            return;

        mOperatePlaylist = playlistDao;
        //新组歌单
        if(playlistDao.getList_id() == FavoriteListViewAdapter.ADD_ONE_LIST_ID){
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
            favoriteDialog.show();
            //favoriteDialog.setFavoriteEntity(entity);
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        }
        else if(playlistDao.getList_id() == FavoriteListViewAdapter.DEFAULT_LIST_ID){

        }
        //添加到已有的歌单
        else {
            if (TextUtils.isEmpty(mStrKey))
                return;

            if (mKeyType == LocalBaseMediaLayout.LayoutType.ALLSONG) {
                MusicInfoDao musicInfoDao = localMusicPresenter.getMusicInfoById(Long.parseLong(mStrKey));
                if (musicInfoDao == null)
                    return;

                List<PlaylistMemberDao> listMembers = new ArrayList<>();
                PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
                playlistMemberDao.setIs_local(1);
                playlistMemberDao.setMusic_id(musicInfoDao.get_id());
                playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
                playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
                listMembers.add(playlistMemberDao);
                handlerAddMembers(mOperatePlaylist, listMembers);
            } else if (mKeyType == LocalBaseMediaLayout.LayoutType.FOLDER) {
                localMusicPresenter.getMusicInfosByFolder(mStrKey);
            } else if (mKeyType == LocalBaseMediaLayout.LayoutType.ALBUM) {
                localMusicPresenter.getMusicInfosByAlbum(mStrKey);
            } else if (mKeyType == LocalBaseMediaLayout.LayoutType.ARTIST) {
                localMusicPresenter.getMusicInfosByArtist(mStrKey);
            }
        }

        dismiss();
    }

    @Override
    public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
        if(dialog == null || TextUtils.isEmpty(strFavoriteName))
            return;

        String strPromt = "";
        if(operType == AlertDialogFavorite.ADD_FAVORITE){
            PlaylistDao playlistDao = new PlaylistDao();
            playlistDao.setName(strFavoriteName);
            playlistDao.setList_id(System.currentTimeMillis());
            playlistDao.setDate_added(System.currentTimeMillis());

            boolean bRet = mPresenter.addPlaylist(playlistDao);

            if(bRet){
                strPromt = String.format("成功添加到\"%s\"", playlistDao.getName());
            }
            else{
                strPromt = String.format("添加失败");
            }
        }
        else if(operType == AlertDialogFavorite.MODIFY_FAVORITE){
            FavoriteEntity entity = dialog.getFavoriteEntity();
            if(entity == null)return;

            entity.strFavoriteName = strFavoriteName;
            boolean bSuccess = false;
            bSuccess = MediaLibrary.getInstance().modifyFavoriteEntity(entity);
            if(bSuccess){
                strPromt = String.format("修改成功");
            }
            else{
                strPromt = String.format("修改失败");
            }
        }
        Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
    }

    //FavoriteListViewAdapter.IFavoriteOperListener
    @Override
    public void OnModifyClick(FavoriteListViewAdapter adapter, int position) {
        if(adapter == null || position < 0)
            return;

        AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), this);
        favoriteDialog.show();
        favoriteDialog.setFavoriteEntity((FavoriteEntity)adapter.getItem(position));
        favoriteDialog.setOperType(AlertDialogFavorite.MODIFY_FAVORITE);
    }

    @Override
    public void OnDeleteClick(FavoriteListViewAdapter adapter, int position) {
        if(adapter == null || position < 0)
            return;
    }

    @Override
    public void onError(String strErrMsg) {

    }

    @Override
    public void onGetPlaylists(List<PlaylistDao> list) {
        mFavoriteListAdapter = new FavoriteListViewAdapter(this.getContext(), list, FavoriteListViewAdapter.READONLY_MODE, false);
        mFavoriteListAdapter.notifyDataSetChanged();
        mFavoriteListAdapter.setFavoriteAdapterListener(this);
        lvFavorite.setAdapter(mFavoriteListAdapter);
    }

    @Override
    public void onGetPlaylistMembers(List<PlaylistMemberDao> list) {

    }

    @Override
    public void onGetAllMusicInfos(List<MusicInfoDao> list) {

    }

    @Override
    public void onGetMusicInfosByFolder(List<MusicInfoDao> list) {
        if(list == null || mOperatePlaylist == null)
            return;

        List<PlaylistMemberDao> listMembers = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            MusicInfoDao musicInfoDao = list.get(i);
            if(musicInfoDao == null)
                continue ;

            PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
            playlistMemberDao.setIs_local(0);
            playlistMemberDao.setMusic_id(musicInfoDao.getSong_id());
            playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
            playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
            listMembers.add(playlistMemberDao);
        }

        handlerAddMembers(mOperatePlaylist, listMembers);
    }

    @Override
    public void onGetMusicInfosByArtist(List<MusicInfoDao> list) {
        if(list == null || mOperatePlaylist == null)
            return;

        List<PlaylistMemberDao> listMembers = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            MusicInfoDao musicInfoDao = list.get(i);
            if(musicInfoDao == null)
                continue ;

            PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
            playlistMemberDao.setIs_local(0);
            playlistMemberDao.setMusic_id(musicInfoDao.getSong_id());
            playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
            playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
            listMembers.add(playlistMemberDao);
        }

        handlerAddMembers(mOperatePlaylist, listMembers);
    }

    @Override
    public void onGetMusicInfosByAlbum(List<MusicInfoDao> list) {
        if(list == null || mOperatePlaylist == null)
            return;

        List<PlaylistMemberDao> listMembers = new ArrayList<>();
        for(int i = 0;i < list.size();i++){
            MusicInfoDao musicInfoDao = list.get(i);
            if(musicInfoDao == null)
                continue ;

            PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
            playlistMemberDao.setIs_local(0);
            playlistMemberDao.setMusic_id(musicInfoDao.getSong_id());
            playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
            playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
            listMembers.add(playlistMemberDao);
        }

        handlerAddMembers(mOperatePlaylist, listMembers);
    }

    @Override
    public void onQueryMusicInfosByName(List<MusicInfoDao> list) {

    }

    @Override
    public void onQueryMusicInfosByArist(List<MusicInfoDao> list) {

    }

    @Override
    public void onQueryMuisicInfosByAlbum(List<MusicInfoDao> list) {

    }

    private void handlerAddMembers(PlaylistDao playlistDao, List<PlaylistMemberDao> playlistMemberDaos){
        if(playlistDao == null || playlistMemberDaos == null)
            return;

        String strPromt;
        int successNum = 0;
        boolean bMutil = playlistMemberDaos.size() > 1 ? true : false;
        for(int i = 0;i < playlistMemberDaos.size();i++){
            PlaylistMemberDao playlistMemberDao = playlistMemberDaos.get(i);
            if(playlistMemberDao == null)
                continue ;

            boolean bRet = mPresenter.addPlaylistMember(playlistMemberDao);
            if(bRet){
                successNum++;
            }
        }

        if(!bMutil){
            if(successNum == playlistMemberDaos.size()){
                strPromt = String.format("成功添加到\"%s\"", playlistDao.getName());
            }
            else if(successNum == playlistMemberDaos.size()){
                strPromt = String.format("已经添加到\"%s\"", playlistDao.getName());
            }
            else{
                strPromt = String.format("添加失败");
            }

            playlistDao.setSong_count(playlistDao.getSong_count() + 1);
        }
        else{
            strPromt = String.format("成功%d首,失败%d首", successNum, playlistMemberDaos.size() - successNum);
            playlistDao.setSong_count(playlistDao.getSong_count() + successNum);
        }
        playlistDao.setDate_modified(System.currentTimeMillis());
        mPresenter.updatePlaylist(playlistDao);
        Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
    }

    public static class Builder extends BaseDialog.Builder{
        public Builder(Context context) {
            super(context);
        }

        @Override
        protected <T extends BaseDialog> T createDialog() {
            return (T) new FavoriteDialog(mContext);
        }

        @Override
        protected int getCustomeView() {
            return R.layout.favorite_dialog;
        }
    }
}

