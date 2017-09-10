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
        boolean removePlaylist(long listId);
        boolean updatePlaylist(PlaylistDao playlistDao);
        void getPlaylists();
        void queryPlaylistById(long id);

        boolean isExistPlaylistMember(long playlistId, long id);
        boolean addPlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao);
        boolean removePlaylistMember(long playlistId, long id);
        boolean updatePlaylistMember(long playlistId, PlaylistMemberDao playlistMemberDao);
        void getPlaylistMembers(long list_id);
    }

    interface View extends BaseContract.View {
        void onGetPlaylists(List<PlaylistDao> list);
        void onQueryPlaylistById(List<PlaylistDao> list);
        void onGetPlaylistMembers(List<PlaylistMemberDao> list);
    }
}
