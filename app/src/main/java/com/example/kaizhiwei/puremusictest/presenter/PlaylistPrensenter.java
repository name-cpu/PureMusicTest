package com.example.kaizhiwei.puremusictest.presenter;

import android.os.Message;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.contract.PlaylistContract;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import com.example.kaizhiwei.puremusictest.model.PlaylistModel;
import com.example.kaizhiwei.puremusictest.model.PlaylistModuleProxy;
import com.example.kaizhiwei.puremusictest.util.BusinessCode;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/20.
 */

public class PlaylistPrensenter implements PlaylistContract.Presenter {
    private PlaylistContract.View mView;

    public PlaylistPrensenter(PlaylistContract.View view){
        mView = view;
    }

    @Override
    public boolean addPlaylist(PlaylistDao playlistDao) {
        return PlaylistModel.getInstance().addPlaylist(playlistDao);
    }

    @Override
    public boolean removePlaylist(long listId) {
        return PlaylistModel.getInstance().removePlaylist(listId);
    }

    @Override
    public boolean updatePlaylist(PlaylistDao playlistDao) {
        return PlaylistModel.getInstance().updatePlaylist(playlistDao);
    }

    @Override
    public void getPlaylists() {
        PlaylistModuleProxy.getInstance().asyncGetPlaylists(new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistDao> list = (List<PlaylistDao>)(msg.obj);
                    if(mView != null){
                        mView.onGetPlaylists(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public void queryPlaylistById(long id) {
        PlaylistModuleProxy.getInstance().asyncQueryPlaylistById(id, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistDao> list = (List<PlaylistDao>)(msg.obj);
                    if(mView != null){
                        mView.onQueryPlaylistById(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }

    @Override
    public boolean isExistPlaylistMember(long playlistId, long id) {
        return PlaylistModel.getInstance().isExistPlaylistMember(playlistId, id);
    }

    @Override
    public boolean addPlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao) {
        return PlaylistModel.getInstance().addPlaylistMember(playlistId, playlistMemberDao);
    }

    @Override
    public boolean removePlaylistMember(long playlistId, long musicId) {
        return PlaylistModel.getInstance().removePlaylistMember(playlistId, musicId);
    }

    @Override
    public boolean updatePlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao) {
        return PlaylistModel.getInstance().updatePlaylistMember(playlistId, playlistMemberDao);
    }

    @Override
    public void getPlaylistMembers(long list_id) {
        PlaylistModuleProxy.getInstance().asyncGetPlaylistMembers(list_id, new BaseHandler() {
            @Override
            public void handleBusiness(Message msg) {
                int what = msg.what;
                if(what == BusinessCode.BUSINESS_CODE_SUCCESS){
                    List<PlaylistMemberDao> list = (List<PlaylistMemberDao>)(msg.obj);
                    if(mView != null){
                        mView.onGetPlaylistMembers(list);
                    }
                }
                else{
                    if(mView != null){
                        mView.onError("");
                    }
                }
            }
        });
    }
}
