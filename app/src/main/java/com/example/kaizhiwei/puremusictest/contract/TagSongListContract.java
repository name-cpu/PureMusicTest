package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.BaseSongInfoBean;
import com.example.kaizhiwei.puremusictest.bean.TagSongListBean;

/**
 * Created by kaizhiwei on 17/7/16.
 */

public interface TagSongListContract {
    interface Presenter extends BaseContract.Presenter{
        void getTagSongList(String from, String version, String channel, int operator, String method,
                           String format,  String tagname, int limit);

        void getSongBaseInfo(String from, String version, String channel, int operator, String method, String format, String song_id);
    }

    interface View extends BaseContract.View {
        void onGetTagSongLiistSuccess(TagSongListBean bean);
        void onGetSongBaseInfoSuccess(BaseSongInfoBean baseSongInfoBane);
    }
}
