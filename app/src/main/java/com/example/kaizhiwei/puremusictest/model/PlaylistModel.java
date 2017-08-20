package com.example.kaizhiwei.puremusictest.model;

import android.content.Context;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.base.BaseRunnable;
import com.example.kaizhiwei.puremusictest.dao.DaoManager;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import org.xutils.common.util.KeyValue;
import org.xutils.db.Selector;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/20.
 */

public class PlaylistModel {
    private static PlaylistModel mInstance;
    private Context mContext;

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

    public boolean addPlaylist(PlaylistDao playlistDao){
        if(playlistDao == null)
            return false;

        try {
            DaoManager.getInstance().getDbManager().save(playlistDao);
        }
        catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removePlaylist(int listId){
        try {
            DaoManager.getInstance().getDbManager().delete(PlaylistMemberDao.class, WhereBuilder.b("playlist_id" , " = ", listId));
            DaoManager.getInstance().getDbManager().delete(PlaylistDao.class, WhereBuilder.b("listid", " == ", listId));
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
            DaoManager.getInstance().getDbManager().update(PlaylistDao.class, WhereBuilder.b("list_id", " = ", playlistDao.getList_id()),
                    new KeyValue("date_modified", playlistDao.getDate_modified()), new KeyValue("song_count", playlistDao.getSong_count()));
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void asyncGetPlaylists(final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                try {
                    List<PlaylistDao> list = DaoManager.getInstance().getDbManager().findAll(PlaylistDao.class);
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                    }
                } catch (DbException e) {
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    }
                    e.printStackTrace();
                }
            }
        };
    }

    public boolean addPlaylistMember(PlaylistMemberDao playlistMemberDao){
        if(playlistMemberDao == null || playlistMemberDao.getPlaylist_id() < 0)
            return false;

        try {
            DaoManager.getInstance().getDbManager().save(playlistMemberDao);
        }
        catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean removePlaylistMember(long id){
        try {
            DaoManager.getInstance().getDbManager().delete(PlaylistMemberDao.class, WhereBuilder.b("_id" , " == ", id));
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean updatePlaylistMember(PlaylistMemberDao playlistMemberDao){
        if(playlistMemberDao == null || playlistMemberDao.getPlaylist_id() < 0)
            return false;

        try {
            DaoManager.getInstance().getDbManager().update(PlaylistMemberDao.class, WhereBuilder.b("_id", " == ", playlistMemberDao.get_id()));
        } catch (DbException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void asyncGetPlaylistMembers(final int list_id, final BaseHandler handler){
        new BaseRunnable(handler){

            @Override
            public void doBusiness() throws Exception {
                try {
                    Selector<PlaylistMemberDao> selector = DaoManager.getInstance().getDbManager().selector(PlaylistMemberDao.class)
                            .where("playlist_id", " == ", list_id);
                    List<PlaylistMemberDao> list = selector.findAll();
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_SUCCESS, list).sendToTarget();
                    }
                } catch (DbException e) {
                    if(handler != null){
                        handler.obtainMessage(BusinessCode.BUSINESS_CODE_ERROR, null).sendToTarget();
                    }
                    e.printStackTrace();
                }
            }
        };
    }
}
