package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.dao.MusicInfoDao;

import java.util.List;
import java.util.Map;

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

        void queryMusicInfosByFolder(String songName);
        void queryMusicInfosByName(String songName);
        void queryMusicInfosByArist(String artist);
        void queryMuisicInfosByAlbum(String album);
    }

    interface View extends BaseContract.View {
        void onGetAllMusicInfos(List<MusicInfoDao> list);
        void onGetMusicInfosByFolder(Map<String, List<MusicInfoDao>> mapRet);
        void onGetMusicInfosByArtist(Map<String, List<MusicInfoDao>> mapRet);
        void onGetMusicInfosByAlbum(Map<String, List<MusicInfoDao>> mapRet);

        void onQueryMusicInfosByFolder(List<MusicInfoDao> list);
        void onQueryMusicInfosByName(List<MusicInfoDao> list);
        void onQueryMusicInfosByArist(List<MusicInfoDao> list);
        void onQueryMuisicInfosByAlbum(List<MusicInfoDao> list);
    }

}
