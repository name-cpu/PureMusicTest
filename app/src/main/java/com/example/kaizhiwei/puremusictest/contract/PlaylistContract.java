package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.base.BaseHandler;
import com.example.kaizhiwei.puremusictest.dao.PlaylistDao;
import com.example.kaizhiwei.puremusictest.dao.PlaylistMemberDao;
import java.util.List;

/**
 * Created by kaizhiwei on 17/8/20.
 */

public interface PlaylistContract {

    interface Presenter extends BaseContract.Presenter{
        boolean addPlaylist(PlaylistDao playlistDao);
        boolean removePlaylist(int listId);
        boolean updatePlaylist(PlaylistDao playlistDao);
        void getPlaylists();

        boolean addPlaylistMember(PlaylistMemberDao playlistMemberDao);
        boolean removePlaylistMember(long id);
        boolean updatePlaylistMember(PlaylistMemberDao playlistMemberDao);
        void getPlaylistMembers(int list_id);
    }

    interface View extends BaseContract.View {
        void onGetPlaylists(List<PlaylistDao> list);
        void onGetPlaylistMembers(List<PlaylistMemberDao> list);
    }
}
