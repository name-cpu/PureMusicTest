package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistGetSongListBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistInfoBean;

/**
 * Created by kaizhiwei on 17/7/8.
 */

public interface ArtistGetAlbumInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getArtistAlbumInfo(String from, String version, String channel, int operator, String method,
                         String format,
                         String album_id);
    }

    interface View extends BaseContract.View {
        void onGetAlbumInfoSuccess(ArtistAlbumInfoBean bean);
    }
}
