package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.SongMvInfoBean;

/**
 * Created by kaizhiwei on 17/7/23.
 */

public interface MvInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getMvInfo(String from, String version, String channel, int operator, String provider,String method,
                       String format, String mv_id, String song_id, String definition);
    }

    interface View extends BaseContract.View {
        void onGetMvInfoSuccess(SongMvInfoBean bean);
    }
}
