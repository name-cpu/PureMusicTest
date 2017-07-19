package com.example.kaizhiwei.puremusictest.contract;

import com.example.kaizhiwei.puremusictest.bean.GeDanInfoBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanListBean;
import com.example.kaizhiwei.puremusictest.bean.GeDanSongDetailInfo;

import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kaizhiwei on 17/7/19.
 */

public interface GeDanInfoContract {
    interface Presenter extends BaseContract.Presenter{
        void getGeDanInfo(String format, String from, String method, String listid);

        void getGeDanSongDetail(String from, String version, String format, String method, String songid);
    }

    interface View extends BaseContract.View {
        void onGetGeDanInfoSuccess(GeDanInfoBean bean);
        void onGetGeDanSongDetailInfoSuccess(GeDanSongDetailInfo songDetailInfo);
    }
}
