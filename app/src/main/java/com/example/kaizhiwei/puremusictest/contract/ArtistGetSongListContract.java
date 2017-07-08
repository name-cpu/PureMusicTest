package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistInfoBean;

/**
 * Created by kaizhiwei on 17/7/2.
 */

public interface ArtistGetSongListContract {
    interface Presenter extends BaseContract.Presenter{
        void getSongList(String from, String version, String channel, int operator, String method,
                         String format,
                         String order,
                         String tinguid,
                         String artistid,
                         int offset,
                         int limits);

        void getArtistInfo(String from, String version, String channel, int operator, String method,
                           String format,
                           String tinguid,
                           String artistid);

        void getArtistAlbumList(String from, String version, String channel, int operator, String method,
                                String format,
                                String order,
                                String tinguid,
                                int offset,
                                int limits);

    }

    interface View extends BaseContract.View {
        void onGetSongListSuccess(ArtistGetSongListBean bean);
        void onGetArtistInfoSuccess(ArtistInfoBean bean);
        void onGetArtistAlbumListSuccess(ArtistAlbumListBean bean);
    }
}
