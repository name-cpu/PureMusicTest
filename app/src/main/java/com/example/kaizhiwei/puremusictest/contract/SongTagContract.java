package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.AllTagInfoBean;
import com.example.kaizhiwei.puremusictest.bean.ArtistAlbumInfoBean;
import com.example.kaizhiwei.puremusictest.bean.HotTagInfoBean;

/**
 * Created by kaizhiwei on 17/7/15.
 */

public interface SongTagContract {
    interface Presenter extends BaseContract.Presenter{
        void getAllTagInfo(String from, String version, String channel, int operator, String method,
                                String format);
        void getHotTagInfo(String from, String version, String channel, int operator, String method,
                                                     String format, int nums);
    }

    interface View extends BaseContract.View {
        void onGetTagInfoSuccess(AllTagInfoBean bean);
        void onGetHotTagInfoSuccess(HotTagInfoBean bean);
    }
}
