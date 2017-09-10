package com.example.kaizhiwei.puremusictest.model;

/**
 * Created by kaizhiwei on 17/9/10.
 */

public interface IPlaylistDataObserver {
    void onPlaylistChanged(long playlistId);
    void onPlaylistMemberChanged(long playlistId, long musicId);
}
