package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;

/**
 * Created by kaizhiwei on 17/8/13.
 */

public interface LocalMusicContract {
    interface Presenter extends BaseContract.Presenter{
        void getAllMusicInfos();
        MusicInfoDao getMusicInfoById(long id);
        void getMusicInfosByFolder(String folder);
        void getMusicInfosByArtist(String artist);
        void getMusicInfosByAlbum(String album);
    }

    interface View extends BaseContract.View {
        void onGetAllMusicInfos(List<MusicInfoDao> list);
        void onGetMusicInfosByFolder(List<MusicInfoDao> list);
        void onGetMusicInfosByArtist(List<MusicInfoDao> list);
        void onGetMusicInfosByAlbum(List<MusicInfoDao> list);
    }

}
