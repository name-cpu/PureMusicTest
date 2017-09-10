package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;

import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;

import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/20.
 */

public class PlaylistModel {
    private static PlaylistModel mInstance;
    private Context mContext;
    private List<IPlaylistDataObserver> mObservers = new ArrayList<>();

    public static PlaylistModel getInstance(){
        if(mInstance == null){
            mInstance = new PlaylistModel();
        }

        return mInstance;
    }

    private PlaylistModel(){

    }

    public void init(Context context){
        mContext = context;
    }

    public void addObserver(IPlaylistDataObserver observer){
        if(observer == null)
            return;

        if(!mObservers.contains(observer)){
            mObservers.add(observer);
        }
    }

    public void removeObserver(IPlaylistDataObserver observer){
        if(observer == null)
            return;

        if(mObservers.contains(observer)){
            mObservers.remove(observer);
        }
    }

    private void notifyPlaylistChanged(long playlistId){
        for(int i = 0;i < mObservers.size();i++){
            mObservers.get(i).onPlaylistChanged(playlistId);
        }
    }

    private void notifyPlaylistMemberChanged(long playlistId, long musicId){
        for(int i = 0;i < mObservers.size();i++){
            mObservers.get(i).onPlaylistMemberChanged(playlistId, musicId);
        }
    }

    public boolean addPlaylist(PlaylistDao playlistDao){
        if(playlistDao == null)
            return false;

        try {
            DaoManager.getInstance().getDbManager().save(playlistDao);
            notifyPlaylistChanged(playlistDao.getList_id());
        }
        catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removePlaylist(long listId){
        try {
            DaoManager.getInstance().getDbManager().delete(PlaylistMemberDao.class, WhereBuilder.b("playlist_id" , " == ", listId));
            DaoManager.getInstance().getDbManager().delete(PlaylistDao.class, WhereBuilder.b("list_id", " == ", listId));
            //notifyPlaylistChanged(playlistDao);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updatePlaylist(PlaylistDao playlistDao){
        if(playlistDao == null)
            return false;

        try {
            DaoManager.getInstance().getDbManager().update(PlaylistDao.class, WhereBuilder.b("list_id", " == ", playlistDao.getList_id()),
                    new KeyValue("date_modified", playlistDao.getDate_modified()), new KeyValue("song_count", playlistDao.getSong_count()),
                    new KeyValue("img_url", playlistDao.getImg_url()), new KeyValue("sort", playlistDao.getSort()));
            notifyPlaylistChanged(playlistDao.getList_id());
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public List<PlaylistDao> queryPlaylistById(final long list_id){
        try {
            Selector<PlaylistDao> selector = DaoManager.getInstance().getDbManager().selector(PlaylistDao.class)
                    .where("list_id", " == ", list_id);
            List<PlaylistDao> list = selector.findAll();
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        };
        return null;
    }

    public List<PlaylistDao> getPlaylists(){
        try {
            List<PlaylistDao> list = DaoManager.getInstance().getDbManager().findAll(PlaylistDao.class);
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addPlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao){
        if(playlistMemberDao == null || playlistId < 0)
            return false;

        try {
            DaoManager.getInstance().getDbManager().save(playlistMemberDao);
            notifyPlaylistMemberChanged(playlistId, playlistMemberDao.getMusic_id());
        }
        catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removePlaylistMember(long playlistId, long musicId){
        try {
            DaoManager.getInstance().getDbManager().delete(PlaylistMemberDao.class, WhereBuilder.b("music_id" , " == ", musicId).and("playlist_id", " == ", playlistId));
            //notifyPlaylistMemberChanged(playlistId, musicId);
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public int removePlaylistMembers(long playlistId, List<Long> ids){
        if(ids == null || ids.size() == 0)
            return 0;

        int successNum = 0;
        for(int i = 0;i < ids.size();i++){
            boolean bRet = removePlaylistMember(playlistId, ids.get(i));
            if(bRet){
                successNum++;
            }
        }
        notifyPlaylistMemberChanged(playlistId, ids.get(0));
        return successNum;
    }

    public boolean updatePlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao){
        if(playlistMemberDao == null || playlistMemberDao.getPlaylist_id() < 0)
            return false;

        try {
            DaoManager.getInstance().getDbManager().update(PlaylistMemberDao.class, WhereBuilder.b("playlist_id", " == ", playlistId).and("music_id", " == ", playlistMemberDao.getMusic_id()),
                    new KeyValue("play_order", playlistMemberDao.getPlay_order()), new KeyValue("is_local", playlistMemberDao.getIs_local()));
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }
        notifyPlaylistMemberChanged(playlistId, playlistMemberDao.getMusic_id());
        return true;
    }

    public boolean isExistPlaylistMember(long playlistId, long musicId){
        try {
            Selector<PlaylistMemberDao> selector = DaoManager.getInstance().getDbManager().selector(PlaylistMemberDao.class)
                    .where("playlist_id", " == ", playlistId).and("music_id", " == ", musicId);
            List<PlaylistMemberDao> list = selector.findAll();
            if(list != null && list.size() > 0)
                return true;
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public List<PlaylistMemberDao> getPlaylistMembers(final long list_id){
        try {
            Selector<PlaylistMemberDao> selector = DaoManager.getInstance().getDbManager().selector(PlaylistMemberDao.class)
                    .where("playlist_id", " == ", list_id);
            List<PlaylistMemberDao> list = selector.findAll();
            return list;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
