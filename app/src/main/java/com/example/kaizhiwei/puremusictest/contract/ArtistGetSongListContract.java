package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;

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

    }

    interface View extends BaseContract.View {
        void onGetSongListSuccess(ArtistGetSongListBean bean);
    }
}
