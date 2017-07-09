package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.SongDetailInfoBean;
/**
 * Created by kaizhiwei on 17/7/9.
 */

public interface SongDetailInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getSongDetailInfo(String from, String version, String channel, int operator, String method,
                                String format,
                                String songId,
                                long ts,
                                String e,
                                int nw,
                                int ucf,
                                int res,
                                int l2p,
                                String lpb,
                                int usup,
                                int lebo);
    }

    interface View extends BaseContract.View {
        void onGetSongDetailInfoSuccess(SongDetailInfoBean bean);
    }
}
