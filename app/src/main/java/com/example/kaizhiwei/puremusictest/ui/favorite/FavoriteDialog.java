
package com.example.kaizhiwei.puremusictest.ui.favorite;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.kaizhiwei.puremusictest.MediaData.FavoriteEntity;
import com.example.kaizhiwei.puremusictest.R;

import android.widget.Toast;

import com.example.kaizhiwei.puremusictest.base.BaseDialog;
import com.example.kaizhiwei.puremusictest.contract.LocalMusicContract;
import com.example.kaizhiwei.puremusictest.contract.PlaylistContract;
import com.example.kaizhiwei.puremusictest.dao.FavoriteMusicDao;
import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.FavoriteMusicModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.presenter.LocalMusicPresenter;
import com.example.kaizhiwei.puremusictest.presenter.PlaylistPrensenter;
import com.example.kaizhiwei.puremusictest.ui.home.FavoriteViewAdpapter;
import com.example.kaizhiwei.puremusictest.ui.localmusic.LocalBaseMediaLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by kaizhiwei on 16/11/27.
 */
public class FavoriteDialog extends BaseDialog implements View.OnClickListener, AbsListView.OnItemClickListener,
        FavoriteListViewAdapter.IFavoriteOperListener, PlaylistContract.View, LocalMusicContract.View {
    private ListView lvFavorite;
    private FavoriteListViewAdapter mFavoriteListAdapter;
    private PlaylistPrensenter mPresenter;
    private LocalMusicPresenter localMusicPresenter;
    private String mStrKey;
    private LocalBaseMediaLayout.LayoutType mKeyType;
    private PlaylistDao mOperatePlaylist;
    private List<PlaylistMemberDao> mPlaylistMembers = new ArrayList<>();
    private List<FavoriteMusicDao> mFavoriteMusicDaos = new ArrayList<>();

    public FavoriteDialog(Context context) {
        super(context);
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

    public void setPlaylistMembers(List<PlaylistMemberDao> list){
        if(mKeyType == LocalBaseMediaLayout.LayoutType.CUSTOME){
            mPlaylistMembers.clear();
            mPlaylistMembers.addAll(list);
        }
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
            AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), new AlertDialogFavorite.OnAlterDialogFavoriteListener() {
                @Override
                public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
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

                        mOperatePlaylist = playlistDao;
                        for(int i = 0;i < mPlaylistMembers.size();i++){
                            mPlaylistMembers.get(i).setPlaylist_id(mOperatePlaylist.getList_id());
                        }
                        handlerAddMembers(mOperatePlaylist, mPlaylistMembers);
                    }
                    Toast.makeText(FavoriteDialog.this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
                }
            });
            favoriteDialog.show();
            //favoriteDialog.setFavoriteEntity(entity);
            favoriteDialog.setOperType(AlertDialogFavorite.ADD_FAVORITE);
        }
        //添加到我喜欢的单曲
        else if(playlistDao.getList_id() == FavoriteViewAdpapter.MY_FAVORITE_PLAYLIST_ID){
            if(mKeyType == LocalBaseMediaLayout.LayoutType.ALLSONG){
                List<FavoriteMusicDao> list = new ArrayList<>();
                MusicInfoDao musicInfoDao = localMusicPresenter.getMusicInfoById(Long.parseLong(mStrKey));
                if (musicInfoDao == null)
                    return;

                FavoriteMusicDao dao = new FavoriteMusicDao();
                dao.setMusicinfo_id(musicInfoDao.get_id());
                dao.setFav_time(System.currentTimeMillis());
                dao.setAlbum(musicInfoDao.getAlbum());
                dao.setArtist(musicInfoDao.getArtist());
                list.add(dao);
                handerAddToFavorite(mOperatePlaylist, list);
            }
            else if(mKeyType == LocalBaseMediaLayout.LayoutType.FOLDER){
                localMusicPresenter.getMusicInfosByFolder(mStrKey);
            }
            else if (mKeyType == LocalBaseMediaLayout.LayoutType.ALBUM) {
                localMusicPresenter.getMusicInfosByAlbum(mStrKey);
            }
            else if (mKeyType == LocalBaseMediaLayout.LayoutType.ARTIST) {
                localMusicPresenter.getMusicInfosByArtist(mStrKey);
            }
        }
        //添加到已有的歌单
        else {
            if(mKeyType == LocalBaseMediaLayout.LayoutType.ALLSONG){
                mPlaylistMembers.clear();
                MusicInfoDao musicInfoDao = localMusicPresenter.getMusicInfoById(Long.parseLong(mStrKey));
                if (musicInfoDao == null)
                    return;

                PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
                playlistMemberDao.setIs_local(1);
                playlistMemberDao.setMusic_id(musicInfoDao.get_id());
                playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
                //playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
                mPlaylistMembers.add(playlistMemberDao);
                handlerAddMembers(mOperatePlaylist, mPlaylistMembers);
            }
            else if(mKeyType == LocalBaseMediaLayout.LayoutType.FOLDER){
                localMusicPresenter.getMusicInfosByFolder(mStrKey);
            }
            else if (mKeyType == LocalBaseMediaLayout.LayoutType.ALBUM) {
                localMusicPresenter.getMusicInfosByAlbum(mStrKey);
            }
            else if (mKeyType == LocalBaseMediaLayout.LayoutType.ARTIST) {
                localMusicPresenter.getMusicInfosByArtist(mStrKey);
            }
        }

        dismiss();
    }

    //FavoriteListViewAdapter.IFavoriteOperListener
    @Override
    public void onFavoriteModifyClick(final int position) {
        if(position < 0)
            return;

        AlertDialogFavorite favoriteDialog = new AlertDialogFavorite(this.getContext(), new AlertDialogFavorite.OnAlterDialogFavoriteListener() {
            @Override
            public void OnFinish(AlertDialogFavorite dialog, int operType, String strFavoriteName) {
                if(dialog == null || TextUtils.isEmpty(strFavoriteName))
                    return;

                String strPromt = null;
                if(operType == AlertDialogFavorite.MODIFY_FAVORITE){
                    PlaylistDao newPlaylistDao = (PlaylistDao)mFavoriteListAdapter.getItem(position);
                    newPlaylistDao.setName(strFavoriteName);
                    newPlaylistDao.setDate_modified(System.currentTimeMillis());
                    boolean bSuccess;
                    bSuccess = PlaylistModel.getInstance().updatePlaylist(newPlaylistDao);
                    if(bSuccess){
                        strPromt = String.format("修改成功");
                    }
                    else{
                        strPromt = String.format("修改失败");
                    }
                }
                Toast.makeText(FavoriteDialog.this.getContext(), strPromt, Toast.LENGTH_SHORT).show();
            }
        });
        favoriteDialog.show();
    }

    @Override
    public void onFavoriteDeleteClick(int position) {
        if(position < 0)
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
    public void onQueryPlaylistById(List<PlaylistDao> list) {

    }

    @Override
    public void onGetPlaylistMembers(List<PlaylistMemberDao> list) {

    }

    @Override
    public void onGetAllMusicInfos(List<MusicInfoDao> list) {

    }

    @Override
    public void onGetMusicInfosByFolder(Map<String, List<MusicInfoDao>> map) {
        if(map == null || mOperatePlaylist == null)
            return;

        if(mOperatePlaylist.getList_id() == FavoriteViewAdpapter.MY_FAVORITE_PLAYLIST_ID){
            mFavoriteMusicDaos.clear();
            Set<String> keySet = map.keySet();
            for(String key : keySet){
                List<MusicInfoDao> list = map.get(key);
                for(int i = 0;i < list.size();i++){
                    MusicInfoDao musicInfoDao = list.get(i);
                    if(musicInfoDao == null)
                        continue ;

                    FavoriteMusicDao dao = new FavoriteMusicDao();
                    dao.setMusicinfo_id(musicInfoDao.get_id());
                    dao.setFav_time(System.currentTimeMillis());
                    dao.setAlbum(musicInfoDao.getAlbum());
                    dao.setArtist(musicInfoDao.getArtist());
                    mFavoriteMusicDaos.add(dao);
                }
            }
            handerAddToFavorite(mOperatePlaylist, mFavoriteMusicDaos);
        }
        else{
            mPlaylistMembers.clear();
            Set<String> keySet = map.keySet();
            for(String key : keySet){
                List<MusicInfoDao> list = map.get(key);
                for(int i = 0;i < list.size();i++){
                    MusicInfoDao musicInfoDao = list.get(i);
                    if(musicInfoDao == null)
                        continue ;

                    PlaylistMemberDao playlistMemberDao = new PlaylistMemberDao();
                    playlistMemberDao.setIs_local(0);
                    playlistMemberDao.setMusic_id(musicInfoDao.get_id());
                    playlistMemberDao.setPlaylist_id(mOperatePlaylist.getList_id());
                    //playlistMemberDao.setPlay_order(mOperatePlaylist.getSong_count());
                    mPlaylistMembers.add(playlistMemberDao);
                }
            }
            handlerAddMembers(mOperatePlaylist, mPlaylistMembers);
        }
    }

    @Override
    public void onGetMusicInfosByArtist(Map<String, List<MusicInfoDao>> map) {
        onGetMusicInfosByFolder(map);
    }

    @Override
    public void onGetMusicInfosByAlbum(Map<String, List<MusicInfoDao>> map) {
        onGetMusicInfosByFolder(map);
    }

    @Override
    public void onQueryMusicInfosByFolder(List<MusicInfoDao> list) {

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

    private void handerAddToFavorite(PlaylistDao playlistDao, List<FavoriteMusicDao> favoriteMusicDaos){
        if(playlistDao == null || favoriteMusicDaos == null)
            return;

        String strPromt;
        int successNum = 0;
        int realSuccessNum = 0;
        boolean bMutil = favoriteMusicDaos.size() > 1 ? true : false;
        for(int i = 0;i < favoriteMusicDaos.size();i++){
            FavoriteMusicDao favoriteMusicDao = favoriteMusicDaos.get(i);
            if(favoriteMusicDao == null)
                continue ;

            boolean isExist = FavoriteMusicModel.getInstance().isHasFavorite(favoriteMusicDaos.get(i).get_id());
            if(isExist){
                successNum++;
                continue;
            }
            boolean bRet = FavoriteMusicModel.getInstance().addFavoriteMusic(favoriteMusicDaos.get(i));
            if(bRet){
                successNum++;
                realSuccessNum++;
            }
        }

        if(!bMutil){
            if(successNum == favoriteMusicDaos.size()){
                strPromt = String.format("成功添加到\"%s\"", playlistDao.getName());
            }
            else if(successNum == favoriteMusicDaos.size()){
                strPromt = String.format("已经添加到\"%s\"", playlistDao.getName());
            }
            else{
                strPromt = String.format("添加失败");
            }

            playlistDao.setSong_count(playlistDao.getSong_count() + realSuccessNum);
        }
        else{
            strPromt = String.format("成功%d首,失败%d首", successNum, favoriteMusicDaos.size() - successNum);
            playlistDao.setSong_count(playlistDao.getSong_count() + realSuccessNum);
        }
        playlistDao.setDate_modified(System.currentTimeMillis());
        mPresenter.updatePlaylist(playlistDao);
        Toast.makeText(this.getContext(), strPromt, Toast.LENGTH_SHORT).show();

    }

    private void handlerAddMembers(PlaylistDao playlistDao, List<PlaylistMemberDao> playlistMemberDaos){
        if(playlistDao == null || playlistMemberDaos == null)
            return;

        String strPromt;
        int successNum = 0;
        int realSuccessNum = 0;
        boolean bMutil = playlistMemberDaos.size() > 1 ? true : false;
        for(int i = 0;i < playlistMemberDaos.size();i++){
            PlaylistMemberDao playlistMemberDao = playlistMemberDaos.get(i);
            if(playlistMemberDao == null)
                continue ;

            boolean isExist = mPresenter.isExistPlaylistMember(mOperatePlaylist.getList_id(), playlistMemberDao.getMusic_id());
            if(isExist){
                successNum++;
                continue;
            }
            boolean bRet = mPresenter.addPlaylistMember(mOperatePlaylist.getList_id(), playlistMemberDao);
            if(bRet){
                successNum++;
                realSuccessNum++;
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

            playlistDao.setSong_count(playlistDao.getSong_count() + realSuccessNum);
        }
        else{
            strPromt = String.format("成功%d首,失败%d首", successNum, playlistMemberDaos.size() - successNum);
            playlistDao.setSong_count(playlistDao.getSong_count() + realSuccessNum);
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

